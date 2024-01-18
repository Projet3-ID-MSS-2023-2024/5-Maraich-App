package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Requests;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class RequestServiceTest {

    @Autowired
    private RequestsService requestsService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private static Users testUser;

    @BeforeEach
    public void setUp() {
        testUser = userService.addUser(new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be", null, null));
    }

    @Test
    @Order(1)
    @Transactional
    public void addRequestTest() {
        userRepository.save(testUser);
        Requests testRequests = new Requests(testUser, "Test Request Create");

        // Adding the request to the databse
        Requests savedRequests = requestsService.addRequests(testRequests);

        // Retrieve the request from the database
        Requests retrievedRequests = requestsService.getRequestsById(savedRequests.getIdRequest());

        // Verify if the request is not null
        assertNotNull(retrievedRequests);

        // Verify if the properties of the request are corrects
        assertEquals(testRequests.getUser(), retrievedRequests.getUser());
        assertEquals(testRequests.getRequestBody(), retrievedRequests.getRequestBody());
    }

    @Test
    @Order(2)
    @Transactional
    public void updateRequestsTest() {
        Requests testRequests = new Requests(testUser, "Test Request");

        // Adding the request to the database
        Requests savedRequests = requestsService.addRequests(testRequests);

        // Update the properties of the request
        savedRequests.setRequestBody("Test Request Updated");

        // Update the request in the database
        Requests updatedRequests = requestsService.updateRequests(savedRequests);

        // Retrieve the updated request from the database
        Requests retrievedRequests = requestsService.getRequestsById(updatedRequests.getIdRequest());

        // Verify if the request has been correctly updated
        assertNotNull(retrievedRequests);
        assertEquals("Test Request Updated", retrievedRequests.getRequestBody());
    }
}