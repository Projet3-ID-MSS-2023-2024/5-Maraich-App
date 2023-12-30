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
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService, UserServiceInterface {
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
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private JwtRepository jwtRepository;


    public Map<String, String> inscription(Users users) {
        Map<String, String> mapError = new HashMap<>();
        try {
            boolean dataIsOk = dataUserVerification(users, true);

            String cryptpwd = this.passwordEncoder.encode(users.getPassword());
            users.setPassword(cryptpwd);
            RankEnum rankEnum;
            if (userRepository.findAll().isEmpty())
                rankEnum = RankEnum.ADMINISTRATOR;
            else
                rankEnum = RankEnum.CUSTOMER;

            Rank rank = rankRepository.findByName(rankEnum).orElseThrow(() -> new RuntimeException("Back issue: Rank initialization issue"));
            users.setRank(rank);
            users = this.userRepository.save(users);
            Validation validation = this.validationService.createValidationProcess(users);
            users.setValidation(validation);
            this.userRepository.save(users);
            mapError.put("message", "Well done!");
            return mapError;
        } catch (RuntimeException re) {
            mapError.put("message", re.getMessage());
            return mapError;
        }
    }

    public Map<String, String> activation(Map<String, String> activation) {
        Map<String, String> mapError = new HashMap<>();
        try {
            Validation validation = this.validationService.readWithCode(activation.get("code"));
            if (validation.getActivationDate() == null) {
                if (Instant.now().isAfter(validation.getExpirationDate())) {
                    throw new RuntimeException("Your validation code has expired !");
                }
                Users usersActiver = this.userRepository.findById(validation.getUsers().getIdUser()).orElseThrow(() -> new RuntimeException("Unknown user"));
                usersActiver.setActif(true);
                this.userRepository.save(usersActiver);
                validation.setActivationDate(Instant.now());
                this.validationRepository.save(validation);
                this.emailSender.sendInscriptionIsConfirm(validation.getUsers());
                mapError.put("message", "Well done!");
            } else {
                throw new RuntimeException("This code is already activated !");
            }
            return mapError;
        } catch (RuntimeException re) {
            mapError.put("message", re.getMessage());
            return mapError;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("None match this email address"));
    }

    public boolean dataUserVerification(Users users, boolean checkMailUsed) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        String nameRegex = "^[a-zA-ZÀ-ÿ-]+$";
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        String roadRegex = "^[a-zA-Z0-9\\s\\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$";
        String postCodeRegex = "^[a-zA-Z0-9\\s\\-]+$";
        String numberRegex = "^[a-zA-Z0-9\\s\\-.,'()&]+$";
        String cityRegex = "^[a-zA-Z\\s\\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$";
        String phoneNumberRegex = "^[0-9]+$";

        if (!Pattern.compile(emailRegex).matcher(users.getEmail()).matches()) {
            throw new RuntimeException("Your email is invalid !");
        }

        Optional<Users> usersOptional = this.userRepository.findByEmail(users.getEmail());

        if (checkMailUsed && usersOptional.isPresent() && !Integer.valueOf(usersOptional.get().getIdUser()).equals(users.getIdUser())) {
            throw new RuntimeException("Your email is already used !");
        }

        if (!Pattern.compile(passwordRegex).matcher(users.getPassword()).matches()) {
            throw new RuntimeException("The password must contain minimum: 8 characters, 1 uppercase and 1 digit !");
        }

        if (!Pattern.compile(nameRegex).matcher(users.getFirstName()).matches()) {
            throw new RuntimeException("FirstName and can only contain letters !");
        }

        if (!Pattern.compile(nameRegex).matcher(users.getSurname()).matches()) {
            throw new RuntimeException("Surname and can only contain letters !");
        }

        if (!Pattern.compile(postCodeRegex).matcher(users.getAddress().getPostCode()).matches()) {
            throw new RuntimeException("The postal code is invalid !");
        }

        if (!Pattern.compile(numberRegex).matcher(users.getAddress().getNumber()).matches()) {
            throw new RuntimeException("The house number is invalid !");
        }

        if (!Pattern.compile(cityRegex).matcher(users.getAddress().getCity()).matches()) {
            throw new RuntimeException("The city is invalid !");
        }

        if (!Pattern.compile(roadRegex).matcher(users.getAddress().getRoad()).matches()) {
            throw new RuntimeException("The road is invalid !");
        }

        if (!Pattern.compile(phoneNumberRegex).matcher(users.getPhoneNumber()).matches()) {
            throw new RuntimeException("The phone number is invalid !");
        }

        return true;
    }


    /**
     * Add a new user
     * (only the admin can add here)
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Users addUser(Users user) {
        //on vérifie si les données sont valide
        boolean dataIsOk = dataUserVerification(user, true);
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
        Users users = userRepository.save(user);
        this.validationService.createValidationProcess(users);
        users = userRepository.save(user);
        return users;
    }

    /**
     * Admin update users
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Users updateUserAdmin(Users user) {
        // Verify if the data is valid
        boolean dataIsOk = dataUserVerification(user, false);
        if (!dataIsOk) {
            throw new RuntimeException("Invalid user data");
        }

        // Verify if the user exist
        Optional<Users> existingUserOptional = userRepository.findById(user.getIdUser());
        if (existingUserOptional.isPresent()) {
            Users existingUser = existingUserOptional.get();

            // Verify if the password was change
            if (!existingUser.getPassword().equals(user.getPassword())) {
                // Hash the new password
                String hashedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(hashedPassword);  // Update the crypted password in the user
            }

             user.setValidation(existingUser.getValidation());
             user.setShop(existingUser.getShop());
             user.setRequests(existingUser.getRequests());

            Users updatedUser = userRepository.save(user);
            Users userDb = userRepository.findById(user.getIdUser()).orElseThrow(() -> new RuntimeException("User not found !"));
            if(user.getRank().getName() == RankEnum.MARAICHER &&
                    userDb.getRank().getName() == RankEnum.CUSTOMER){
                if(shopRepository.existsByOwnerId(user.getIdUser())){
                    Shop shop = userDb.getShop();
                    shopRepository.save(shop);
                    updatedUser.setShop(shop);
                }else{
                    shopService.addShopMinimal(updatedUser);
                }
            } else if(user.getRank().getName() == RankEnum.CUSTOMER &&
                    userDb.getRank().getName() == RankEnum.MARAICHER){
                Shop shop = userDb.getShop();
                shop.setEnable(false);
                shopRepository.save(shop);
                updatedUser.setShop(shop);
            }

            // Update the user
            return updatedUser;
        } else {
            throw new EntityNotFoundException("User not found with ID: " + user.getIdUser());
        }
    }

    /**
     * customer and market gardener level user update
     * (for example : their user profile)
     * @param updatedUser
     * @return
     */
    @Override
    @Transactional
    public Users updateUserRestricted(Users updatedUser) {
        // Vérifie si les données sont valides
        boolean dataIsOk = dataUserVerification(updatedUser, false);
        if (!dataIsOk) {
            throw new RuntimeException("Invalid user data");
        }

        // Verify if the user exist
        Optional<Users> existingUserOptional = userRepository.findById(updatedUser.getIdUser());

        if (existingUserOptional.isPresent()) {
            Users existingUser = existingUserOptional.get();

            // Modify authorize things
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setSurname(updatedUser.getSurname());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setAddress(updatedUser.getAddress());

            // Verify if the password was change
            if (!existingUser.getPassword().equals(updatedUser.getPassword())) {
                // Hash the new password
                String hashedPassword = passwordEncoder.encode(updatedUser.getPassword());
                existingUser.setPassword(hashedPassword);
            }

            // Update the user
            return userRepository.save(existingUser);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + updatedUser.getIdUser());
        }
    }

    /**
     * get a user by id
     * @param id
     * @return
     */
    @Override
    @Transactional
    public Users getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));//on lève une exception si pas trouvé
    }

    /**
     * return users by rank name
     * @param rankEnum
     * @return
     */
    @Override
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

    /**
     * Return all users
     * @return
     */
    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     *
     * Delete a user by id
     * @param id
     */
    @Override
    @Transactional
    public void deleteUserById(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }

    /**
     * Return all ranks
     * @return
     */
    @Override
    public List<Rank> getAllRanks() {
        return rankRepository.findAll();
    }
}

