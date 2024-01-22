package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.RankRepository;
import be.helha.maraichapp.repositories.ShopRepository;
import be.helha.maraichapp.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShopServiceTest {

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RankRepository rankRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;

    private Shop testShop;
    private Shop testShop2;
    private Users testUser;
    private Users testUser2;

    @BeforeAll
    public void setUp() {
        testUser = userService.addUser(new Users("Dumont", "Logan", "0477700467", "Password1", "40", "Rue du Potiers", "6030", "Charleroi", "la216833@student.helha.be", null, null));
        testShop = shopService.addShop(new Shop("Test Shop", "testshop@example.com", new Address("Test Road", "12345", "Test City", "42"),
                "testshop-picture.jpg", "Test Shop Description", true, true, testUser, null, null));
    }

    @Test
    @Order(1)
    public void addShopTest() {
        testUser2 = userService.addUser(new Users("NewUser", "Logan", "0477700467", "Password1", "400", "Rue du Potiers", "6030", "Charleroi", "la216844@student.helha.be", null, null));
        testShop2 = new Shop("New Shop", "newshop@example.com", new Address("New Road", "12345", "New City", "42"),
                "newshop-picture.jpg", "New Shop Description", true, true, testUser2, null, null);
        shopService.addShop(testShop2);

        assertNotEquals(0,testShop2.getIdShop());
        Shop retrievedShop = shopRepository.findById(testShop2.getIdShop()).orElse(null);
        assertNotNull(retrievedShop);
        assertEquals("New Shop", retrievedShop.getName());
    }

    @Test
    @Order(2)
    public void updateShopTest() {
        testShop2.setName("Updated Shop");
        testShop2.setEmail("updatedshop@example.com");
        testShop2.setDescription("Updated Shop Description");

        Shop updatedShop = shopService.updateShop(testShop2);

        assertNotNull(updatedShop);
        assertEquals("Updated Shop", updatedShop.getName());
        assertEquals("updatedshop@example.com", updatedShop.getEmail());
        assertEquals("Updated Shop Description", updatedShop.getDescription());
    }

    @Test
    @Order(3)
    public void getShopByIdTest() {
        Shop retrievedShop = shopService.getShopById(testShop.getIdShop());

        assertNotNull(retrievedShop);
        assertEquals(testShop.getName(), retrievedShop.getName());
        assertEquals(testShop.getEmail(), retrievedShop.getEmail());
        assertEquals(testShop.getDescription(), retrievedShop.getDescription());
    }

    @Test
    @Order(4)
    public void getShopByNameTest() {
        List<Shop> foundShops = shopService.getShopByName("Test Shop");

        assertFalse(foundShops.isEmpty());
        assertEquals(testShop.getName(), foundShops.get(0).getName());
    }

    @Test
    @Order(5)
    public void getShopByIdOwnerTest() {
        Shop ownerShop = shopService.getShopByIdOwner(testUser.getIdUser());

        assertNotNull(ownerShop);
    }

    @Test
    @Order(6)
    public void deleteShopTest() {
        Optional<Shop> shopDB = shopRepository.findById(testShop.getIdShop());

        if (shopDB.isPresent()) {
            Shop shopToDelete = shopDB.get();
            Users usersDB = shopToDelete.getOwner();

            if (usersDB != null) {
                usersDB.setRank(rankRepository.findByName(RankEnum.CUSTOMER).orElseThrow(() -> new RuntimeException("Unknown rank")));
                usersDB.setShop(null);
                userService.updateUserAdmin(usersDB);
            }

            shopRepository.delete(shopToDelete);

            // Verify that the shop has been deleted
            assertThrows(RuntimeException.class, () -> shopService.getShopById(testShop.getIdShop()));
        } else {
            fail("Shop not found with ID: " + testShop.getIdShop());
        }
    }

    @Test
    @Order(7)
    public void turnOnOffTest() {
        boolean initialStatus = testShop2.isEnable();
        boolean newStatus = shopService.turnOnOff(testShop2.getIdShop());

        assertNotEquals(initialStatus, newStatus);
        assertEquals(!initialStatus, newStatus);
    }
}
