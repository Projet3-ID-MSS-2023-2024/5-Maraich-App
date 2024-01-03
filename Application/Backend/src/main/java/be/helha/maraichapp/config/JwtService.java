package be.helha.maraichapp.config;

import be.helha.maraichapp.dto.AuthentificationDTO;
import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.JwtRepository;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.repositories.ValidationRepository;
import be.helha.maraichapp.services.UserService;
import be.helha.maraichapp.services.ValidationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Transactional
@Service
public class JwtService {
    public static final String BEARER = "bearer";
    @Autowired
    JwtRepository jwtRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ValidationRepository validationRepository;
    @Autowired
    ValidationService validationService;
    @Autowired
    AuthenticationManager authenticationManager;
    private final String ENCRYPTION_KEY = "39a3d3ef7523663f81f23906dba75c9425c256cb55dc6d05f5cc75e5b524786d";
    @Autowired
    private UserService userService;
    public Jwt tokenByValue(String token) {
        return this.jwtRepository.findByValueAndIsDisableAndIsExpired(token, false, false).orElseThrow(()-> new RuntimeException("Token invalide ou inconnu"));
    }

    public Map<String, String> generate(String email) {
        Users users = (Users) this.userService.loadUserByUsername(email);
        this.disableTokens(users);
        Map<String, String> jwtMap = this.generateJwt(users);
        Jwt jwt = new Jwt(false, false, users, jwtMap.get(BEARER));
        this.jwtRepository.save(jwt);
        return jwtMap;
    }

    private void disableTokens(Users users) {
        List<Jwt> jwtList = this.jwtRepository.findByEmail(users.getEmail()).peek(
                jwt -> {
                    jwt.setDisable(true);
                    jwt.setExpired(true);
                }
        ).collect(Collectors.toList());

        this.jwtRepository.saveAll(jwtList);
    }

    public String extractEmail(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate =  this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> generateJwt(Users users){
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + (60 * 60 * 1000);
        final Map<String, Object> claims = Map.of(
                "name", users.getFirstName(),
                "idUser", users.getIdUser(),
                "rank", users.getRank().getName(),
                Claims.EXPIRATION,new Date(expirationTime),
                Claims.SUBJECT, users.getEmail()
        );
        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(users.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(BEARER, bearer);
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public void disconnection() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.findByUsersValidToken(users.getEmail(), false, false)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        jwt.setExpired(true);
        jwt.setDisable(true);
        this.jwtRepository.save(jwt);
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void removeUselessJwt() {
        log.info("Delete tokens at {}", Instant.now());
        this.jwtRepository.deleteAllByIsExpiredAndIsDisable(true, true);
    }

    public Map<String, String> connection(AuthentificationDTO authentificationDTO) {

        try{
            Authentication authentificate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentificationDTO.email(), authentificationDTO.password())
            );
            Users users = userRepository.findByEmail(authentificationDTO.email()).get();
            if(!users.isActif()){
                Instant expirationDate = validationRepository.findbyUser(authentificationDTO.email()).get().getExpirationDate();
                if(expirationDate.isBefore(Instant.now())){
                    validationRepository.deleteByUserEmail(users.getEmail());
                    validationService.createValidationProcess(users);
                    throw new RuntimeException("The user is not activate and the activation code has expired, a new one has been sent !");
                };
                throw new RuntimeException("The user is not activate, check your mail !");
            } else {
                if (authentificate.isAuthenticated()) {
                    return generate(authentificationDTO.email());
                }
            }
        } catch (Exception e) {
            Map<String, String> mapError = new HashMap<>();
            mapError.put("message", e.getMessage());
            return mapError;
        }
        return null;
    }
}

