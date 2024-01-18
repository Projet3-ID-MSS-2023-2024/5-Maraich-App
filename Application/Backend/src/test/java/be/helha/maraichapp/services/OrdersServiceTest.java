package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class OrdersServiceTest {
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private ShopService shopService;
//    @Autowired
//    private UserService userService;
//
//    @Test
//    @Order(1)
//    @Transactional
//    public void addOrderTest() {
//        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
//        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
//        Orders testOrders = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
//        // Adding Users and Shop to the database
//        userService.addUser(testUser);
//        shopService.addShop(testShop);
//        Orders savedOrders = orderService.addOrder(testOrders);
//
//        // We verify if the retrieved order from the database is the same order as the saved one before :
//        Orders retrievedOrders = orderService.getOrderById(savedOrders.getId());
//        assertNotNull(retrievedOrders);
//        assertEquals(testOrders.getCustomer(), retrievedOrders.getCustomer());
//    }
//
//    @Test
//    @Order(2)
//    @Transactional
//    public void updateOrderTest() {
//        // Creating shop, user and order for testing
//        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
//        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
//        Orders testOrders = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
//        // Adding the test order,user and shop to the database
//        userService.addUser(testUser);
//        shopService.addShop(testShop);
//        Orders savedOrders = orderService.addOrder(testOrders);
//
//        // Modifying order datas (changing user)
//        Users testUser2 = new Users("Logan",  "Dumont", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "logan@test.be");
//        savedOrders.setCustomer(testUser2);
//        userService.addUser(testUser2);
//        // Updating the user
//        Orders updateOrder = orderService.updateOrder(savedOrders);
//
//        // Retrieve the updated order from the database
//        Orders retrievedOrder = orderService.getOrderById(updateOrder.getId());
//        // Verify if the order has been correctly updated
//        assertNotNull(retrievedOrder);
//        assertEquals(savedOrders.getCustomer(), retrievedOrder.getCustomer());
//
//    }
//
//    @Test
//    @Order(3)
//    @Transactional
//    public void getAllOrdersTest() {
//        // Creating order objects for testing
//        Users testUser1 = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
//        Shop testShop1 = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser1);
//        Orders testOrder1 = new Orders(Instant.now(), 50, Instant.now(),testUser1, testShop1);
//
//        Users testUser2 = new Users("Logan",  "Dumont", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "logan@test.be");
//        Shop testShop2 = new Shop("Chez Logan", "logan@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser2);
//        Orders testOrder2 = new Orders(Instant.now(), 50, Instant.now(),testUser2, testShop2);
//
//        // Adding users,shops and orders to the database
//        userService.addUser(testUser1);
//        userService.addUser(testUser2);
//        shopService.addShop(testShop1);
//        shopService.addShop(testShop2);
//        orderService.addOrder(testOrder1);
//        orderService.addOrder(testOrder2);
//
//        // Calling the getAllOrders method
//        List<Orders> allOrders = orderService.getAllOrders();
//
//        // Checking if the list is empty
//        assertFalse(allOrders.isEmpty());
//    }
//
//    @Test
//    @Order(4)
//    @Transactional
//    public void getOrderByIdTest() {
//        // Creating order object for testing
//        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
//        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
//        Orders testOrder = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
//
//        // Add the user, shop and order to the database
//        userService.addUser(testUser);
//        shopService.addShop(testShop);
//        Orders savedOrder = orderService.addOrder(testOrder);
//
//        // Call the getOrderById method
//        Orders retrievedOrder = orderService.getOrderById(savedOrder.getId());
//
//        // Verify if the orders has the corrects attributes
//        assertNotNull(retrievedOrder);
//        assertEquals(testOrder.getCustomer(), retrievedOrder.getCustomer());
//    }
//
//    @Test
//    @Order(5)
//    @Transactional
//    public void deleteOrderTest() {
//        // Creating order object for testing
//        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
//        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
//        Orders testOrder = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
//
//        // Add the user, shop and order to the database
//        userService.addUser(testUser);
//        shopService.addShop(testShop);
//        Orders savedOrder = orderService.addOrder(testOrder);
//
//        // Delete the order from the database
//        orderService.deleteOrderById(savedOrder.getId());
//
//        // Verify if the user is properly deleted
//        List<Orders> allOrdersAfterDeletion = orderService.getAllOrders();
//        assertEquals(0, allOrdersAfterDeletion.size());
//    }
}


