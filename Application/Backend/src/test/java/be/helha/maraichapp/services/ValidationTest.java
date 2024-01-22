package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.models.Validation;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.repositories.ValidationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class ValidationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private ValidationService validationService;

    private static Users testUser;



    @BeforeEach
    public void addBeforeEach() {
        validationRepository.deleteAll();
        userRepository.deleteAll();
        testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be", null, null);
        testUser = userRepository.save(testUser);
    }

    @Test
    @Order(1)
    public void testCreateValidationProcess() {

        Validation validation = validationService.createValidationProcess(testUser);


        assertNotNull(validation);
        assertNotNull(validation.getCode());
        assertNotNull(validation.getCreationDate());
        assertNotNull(validation.getExpirationDate());
        assertEquals(testUser, validation.getUsers());
    }

    @Test
    @Order(2)
    public void testReadWithCodeValidCode() {
        validationService.createValidationProcess(testUser);
        String validationCode = testUser.getValidation().getCode();

        Users resultUser = validationService.readWithCode(validationCode).getUsers();

        assertNotNull(resultUser);
        assertEquals(testUser.getEmail(), resultUser.getEmail());
    }

    @Test
    @Order(3)
    public void testReadWithCodeInvalidCode() {
        // Arrange
        String invalidCode = "999999";

        // Act and Assert
        assertThrows(RuntimeException.class, () -> validationService.readWithCode(invalidCode));
    }
}
