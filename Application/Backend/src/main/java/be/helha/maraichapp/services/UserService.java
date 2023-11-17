package be.helha.maraichapp.services;

import be.helha.maraichapp.repositories.*;
import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ValidationRepository validationRepository;


    public void inscription(Users users){
        boolean dataIsOk = dataVerification(users);

        String cryptpwd = this.passwordEncoder.encode(users.getPassword());
        users.setPassword(cryptpwd);
        RankEnum rankEnum;
        if(userRepository.findAll().isEmpty())
            rankEnum = RankEnum.ADMINISTRATOR;
        else
            rankEnum = RankEnum.CUSTOMER;

        Rank rank = rankRepository.findByName(rankEnum).orElseThrow(() -> new RuntimeException("Rank initialization issue"));
        users.setRank(rank);
        users = this.userRepository.save(users);
        this.validationService.createValidationProcess(users);
    }

    public void activation(Map<String, String> activation) {
        Validation validation = this.validationService.readWithCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpirationDate())){
            throw new RuntimeException("Your validation code has expired");
        }
        Users usersActiver = this.userRepository.findById(validation.getUsers().getIdUser()).orElseThrow(() -> new RuntimeException("Unknown user"));
        usersActiver.setActif(true);
        this.userRepository.save(usersActiver);
        validation.setActivationDate(Instant.now());
        this.validationRepository.save(validation);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("None match this email address"));
    }

    public boolean dataVerification(Users users){
        final String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        final String lettersOnlyRegex = "^[a-zA-Z]+$";
        final String lettersAndDigitOnlyRegex = "^[a-zA-Z0-9]+$";

        if(!Pattern.compile(emailRegex).matcher(users.getEmail()).matches()){
            throw new RuntimeException("Your email is invalid");
        }

        Optional<Users> usersOptional = this.userRepository.findByEmail(users.getEmail());

        if(usersOptional.isPresent()){
            throw new RuntimeException("Your email is already used");
        }

        if(!Pattern.compile(passwordRegex).matcher(users.getPassword()).matches()){
            throw new RuntimeException("The password must contain minimum: 8 characters, 1 uppercase and 1 digit");
        }
        Pattern pattern = Pattern.compile(lettersOnlyRegex);
        if(!pattern.matcher(users.getAddress().getCity()).matches()
                && !pattern.matcher(users.getAddress().getRoad()).matches()){
            throw new RuntimeException("The street and the city can only contain letters");
        }

        if(!pattern.matcher(users.getFirstName()).matches()
                && !pattern.matcher(users.getSurname()).matches()){
            throw new RuntimeException("Name and surname can only contain letters");
        }

        if(!Pattern.compile(lettersAndDigitOnlyRegex).matcher(users.getAddress().getNumber()).matches()
                && !Pattern.compile(lettersAndDigitOnlyRegex).matcher(users.getAddress().getPostCode()).matches()){
            throw new RuntimeException("Postal code and house number can only contain numbers and letters");
        }
        return true;
    }
}
