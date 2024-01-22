package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.CategoryRepository;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ShopRepository;
import be.helha.maraichapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class OrdersServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @Order(1)
    @Transactional
    public void addOrderTest() {
        // Creating user for testing
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
        testUser = userRepository.save(testUser);
        // Creating shop for testing
        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bons légumes bien frais", testUser);
        testShop = shopRepository.save(testShop);
        // Creating category for testing
        Category testCategory = new Category("Légumes");
        testCategory = categoryRepository.save(testCategory);
        // Creating product for testing
        Product testProduct = new Product("Carotte",15,"C'est des carottes","",20,40,false,testCategory,testShop);
        testProduct =  productRepository.save(testProduct);
        // Creating order for testing
        Orders testOrders = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
        // Creating orderProduct for testing
        OrderProduct testOrderProduct = new OrderProduct(null, testProduct, 1);
        List<OrderProduct> orderProductsList = new ArrayList<>();
        orderProductsList.add(testOrderProduct);
        testOrders.setOrderProducts(orderProductsList);
        // Adding Order to the database
        testOrders = orderService.addOrder(testOrders);

        // We verify if the retrieved order from the database is the same order as the saved one before :
        Orders retrievedOrders = orderService.getOrderById(testOrders.getId());
        assertNotNull(retrievedOrders);
        assertEquals(testOrders.getCustomer(), retrievedOrders.getCustomer());
    }

    @Test
    @Order(2)
    @Transactional
    public void updateOrderTest() {
        // Creating user for testing
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
        testUser = userRepository.save(testUser);
        // Creating shop for testing
        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
        testShop =  shopRepository.save(testShop);
        // Creating category for testing
        Category testCategory = new Category("Légumes");
        testCategory = categoryRepository.save(testCategory);
        // Creating product for testing
        Product testProduct = new Product("Carotte",15,"C'est des carottes","",20,40,false,testCategory,testShop);
        testProduct =  productRepository.save(testProduct);
        // Creating order for testing
        Orders testOrders = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
        // Creating orderProduct for testing
        OrderProduct testOrderProduct = new OrderProduct(null, testProduct, 1);
        List<OrderProduct> orderProductsList = new ArrayList<>();
        orderProductsList.add(testOrderProduct);
        testOrders.setOrderProducts(orderProductsList);
        // Adding the test order to the database
        testOrders = orderService.addOrder(testOrders);

        // Modifying order datas (changing user)
        Users testUser2 = new Users("Logan",  "Dumont", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "logan@test.be");
        testUser2 = userRepository.save(testUser2);
        testOrders.setCustomer(testUser2);
        // Updating the user
        testOrders = orderService.updateOrder(testOrders);

        // Retrieve the updated order from the database
        Orders retrievedOrder = orderService.getOrderById(testOrders.getId());
        // Verify if the order has been correctly updated
        assertNotNull(retrievedOrder);
        assertEquals(testOrders.getCustomer(), retrievedOrder.getCustomer());

    }

    @Test
    @Order(3)
    @Transactional
    public void getAllOrdersTest() {
        // Creating the first order
        // Creating user for testing
        Users testUser1 = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
        testUser1 = userRepository.save(testUser1);
        // Creating shop for testing
        Shop testShop1 = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser1);
        testShop1 = shopRepository.save(testShop1);
        // Creating category for testing
        Category testCategory = new Category("Légumes");
        testCategory = categoryRepository.save(testCategory);
        // Creating product for testing
        Product testProduct1 = new Product("Carotte",15,"C'est des carottes","",20,40,false,testCategory,testShop1);
        testProduct1 =  productRepository.save(testProduct1);
        // Creating order for testing
        Orders testOrder1 = new Orders(Instant.now(), 50, Instant.now(),testUser1, testShop1);
        // Creating orderProduct for testing
        OrderProduct testOrderProduct1 = new OrderProduct(null, testProduct1, 1);
        List<OrderProduct> orderProductsList1 = new ArrayList<>();
        orderProductsList1.add(testOrderProduct1);
        testOrder1.setOrderProducts(orderProductsList1);
        // Saving the 1st order in the database
        orderService.addOrder(testOrder1);
        //Creating the second order
        //Creating the user
        Users testUser2 = new Users("Logan",  "Dumont", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "logan@test.be");
        testUser2 = userRepository.save(testUser2);
        //Creating the shop
        Shop testShop2 = new Shop("Chez Logan", "logan@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser2);
        testShop2 = shopRepository.save(testShop2);
        // Creating product for testing
        Product testProduct2 = new Product("Choux",15,"C'est des choux","",20,40,false,testCategory,testShop2);
        testProduct2 =  productRepository.save(testProduct2);
        //Creating the order
        Orders testOrder2 = new Orders(Instant.now(), 50, Instant.now(),testUser2, testShop2);
        // Creating orderProduct for testing
        OrderProduct testOrderProduct2 = new OrderProduct(null, testProduct1, 1);
        List<OrderProduct> orderProductsList2 = new ArrayList<>();
        orderProductsList2.add(testOrderProduct2);
        testOrder2.setOrderProducts(orderProductsList2);

        // Adding orders to the database
        orderService.addOrder(testOrder2);

        // Calling the getAllOrders method
        List<Orders> allOrders = orderService.getAllOrders();

        // Checking if the list is empty
        assertFalse(allOrders.isEmpty());
    }

    @Test
    @Order(4)
    @Transactional
    public void getOrderByIdTest() {
        // Creating user
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
        testUser = userRepository.save(testUser);
        // Creating shop
        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
        testShop = shopRepository.save(testShop);
        Orders testOrder = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
        // Creating category
        Category testCategory = new Category("Légumes");
        testCategory = categoryRepository.save(testCategory);
        // Creating product
        Product testProduct = new Product("Carotte",15,"C'est des carottes","",20,40,false,testCategory,testShop);
        testProduct =  productRepository.save(testProduct);
        // Creating order
        Orders testOrders = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
        // Creating orderProduct
        OrderProduct testOrderProduct = new OrderProduct(null, testProduct, 1);
        List<OrderProduct> orderProductsList = new ArrayList<>();
        orderProductsList.add(testOrderProduct);
        testOrder.setOrderProducts(orderProductsList);
        // Add the order to the database
        testOrders = orderService.addOrder(testOrder);

        // Call the getOrderById method
        Orders retrievedOrder = orderService.getOrderById(testOrders.getId());

        // Verify if the orders has the corrects attributes
        assertNotNull(retrievedOrder);
        assertEquals(testOrder.getCustomer(), retrievedOrder.getCustomer());
    }

    @Test
    @Order(5)
    @Transactional
    public void deleteOrderTest() {
        // Creating user
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be");
        testUser = userRepository.save(testUser);
        // Creating shop
        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
        testShop = shopRepository.save(testShop);
        // Creating category
        Category testCategory = new Category("Légumes");
        testCategory = categoryRepository.save(testCategory);
        // Creating product
        Product testProduct = new Product("Carotte",15,"C'est des carottes","",20,40,false,testCategory,testShop);
        testProduct =  productRepository.save(testProduct);
        // Creating order
        Orders testOrder = new Orders(Instant.now(), 50, Instant.now(),testUser, testShop);
        // Creating orderProduct
        OrderProduct testOrderProduct = new OrderProduct(null, testProduct, 1);
        List<OrderProduct> orderProductsList = new ArrayList<>();
        orderProductsList.add(testOrderProduct);
        testOrder.setOrderProducts(orderProductsList);

        // Add the order to the database
        testOrder = orderService.addOrder(testOrder);

        // Delete the order from the database
        orderService.deleteOrderById(testOrder.getId());

        // Verify if the user is properly deleted
        List<Orders> allOrdersAfterDeletion = orderService.getAllOrders();
        assertEquals(0, allOrdersAfterDeletion.size());
    }
}


