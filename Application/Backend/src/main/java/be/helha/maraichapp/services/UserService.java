package be.helha.maraichapp.services;

import be.helha.maraichapp.repositories.*;
import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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


    public void inscription(Users users) {
        boolean dataIsOk = dataUserVerification(users);

        String cryptpwd = this.passwordEncoder.encode(users.getPassword());
        users.setPassword(cryptpwd);
        RankEnum rankEnum;
        if (userRepository.findAll().isEmpty())
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
        if (Instant.now().isAfter(validation.getExpirationDate())) {
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

    public boolean dataUserVerification(Users users) {
        final String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        final String lettersOnlyRegex = "^[A-Za-zÀ-ÿ'\\- ]+$";
        final String lettersAndDigitOnlyRegex = "^[a-zA-Z0-9 ]+$";

        if (!Pattern.compile(emailRegex).matcher(users.getEmail()).matches()) {
            throw new RuntimeException("Your email is invalid");
        }

        Optional<Users> usersOptional = this.userRepository.findByEmail(users.getEmail());

        if (usersOptional.isPresent() && !Integer.valueOf(usersOptional.get().getIdUser()).equals(users.getIdUser())) {
            throw new RuntimeException("Your email is already used");
        }


        if (!Pattern.compile(passwordRegex).matcher(users.getPassword()).matches()) {
            throw new RuntimeException("The password must contain minimum: 8 characters, 1 uppercase and 1 digit");
        }
        Pattern pattern = Pattern.compile(lettersOnlyRegex);
        if (!pattern.matcher(users.getAddress().getCity()).matches()
                && !pattern.matcher(users.getAddress().getRoad()).matches()) {
            throw new RuntimeException("The street and the city can only contain letters");
        }

        if (!pattern.matcher(users.getFirstName()).matches()
                && !pattern.matcher(users.getSurname()).matches()) {
            throw new RuntimeException("Name and surname can only contain letters");
        }

        if (!Pattern.compile(lettersAndDigitOnlyRegex).matcher(users.getAddress().getNumber()).matches()
                && !Pattern.compile(lettersAndDigitOnlyRegex).matcher(users.getAddress().getPostCode()).matches()) {
            throw new RuntimeException("Postal code and house number can only contain numbers and letters");
        }
        return true;
    }


    // Ajouter un nouvel user (niveau admin)
    @Transactional
    public Users addUser(Users user) {
        //on vérifie si les données sont valide
        boolean dataIsOk = dataUserVerification(user);
        if (!dataIsOk) {
            throw new RuntimeException("Invalid user data");
        }
        //on chiffre le mdp
        String cryptpwd = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(cryptpwd);

        RankEnum rankEnum = RankEnum.CUSTOMER;

        //on vérifie que le role existe bien en BDD
        Rank rank = rankRepository.findByName(rankEnum).orElseThrow(() -> new RuntimeException("Rank initialization issue"));
        user.setRank(rank); //on set le role au user
        return userRepository.save(user);
    }

    // Mettre à jour un user existant
    @Transactional
    public Users updateUserAdmin(Users user) {
        // Vérifie si les données sont valides
        boolean dataIsOk = dataUserVerification(user);
        if (!dataIsOk) {
            throw new RuntimeException("Invalid user data");
        }

        // Vérifie si l'utilisateur existe
        Optional<Users> existingUserOptional = userRepository.findById(user.getIdUser());
        if (existingUserOptional.isPresent()) {
            Users existingUser = existingUserOptional.get();

            // Vérifie si le mot de passe a changé
            if (!existingUser.getPassword().equals(user.getPassword())) {
                // Hachez le nouveau mot de passe
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);  // Met à jour le mot de passe chiffré dans l'objet user
            }

            // Met à jour l'utilisateur
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + user.getIdUser());
        }
    }

    @Transactional
    public Users updateUserRestricted(Users updatedUser) {
        // Vérifie si les données sont valides
        boolean dataIsOk = dataUserVerification(updatedUser);
        if (!dataIsOk) {
            throw new RuntimeException("Invalid user data");
        }

        // Vérifie si l'utilisateur existe
        Optional<Users> existingUserOptional = userRepository.findById(updatedUser.getIdUser());

        if (existingUserOptional.isPresent()) {
            Users existingUser = existingUserOptional.get();

            // Restreint les modifications autorisées
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setSurname(updatedUser.getSurname());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setAddress(updatedUser.getAddress());

            // Vérifie si le mot de passe a changé
            if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
                // Hachez le nouveau mot de passe
                String hashedPassword = passwordEncoder.encode(updatedUser.getPassword());
                existingUser.setPassword(hashedPassword);
            }

            // Met à jour l'utilisateur
            return userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + updatedUser.getIdUser());
        }
    }

    //rechercher un user avec un id
    @Transactional
    public Users getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));//on lève une exception si pas trouvé
    }

    @Transactional
    public Optional<List<Users>> getUsersByRank(RankEnum rankEnum) {
        Rank rank = rankRepository.findByName(rankEnum)
                .orElseThrow(() -> new EntityNotFoundException("Rank not found with name: " + rankEnum));

        List<Users> usersList = userRepository.findByRank(rank);

        if (usersList == null || usersList.isEmpty()) {
            throw new EntityNotFoundException("Users not found with rank: " + rankEnum);
        }

        return Optional.of(usersList);
    }

    //retourne tout les users
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // Supprimer un utilisateur depuis son ID
    @Transactional
    public void deleteUserById(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }




}


