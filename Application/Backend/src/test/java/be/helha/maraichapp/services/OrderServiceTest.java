package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @AfterEach
    public void cleanUp() {
        List<Order> allOrders = orderService.getAllOrders();
        for (Order order : allOrders) {
            orderService.deleteOrderById(order.getId());
        }
    }

    @Test
    @Order(1)
    @Transactional
    public void addOrderTest() {
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be", rank);
        Shop testShop = new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "carrot.jpg", "des bon légumes bien frais", testUser);
        Order testOrder = new Order(Calendar.getInstance(), 50, Calendar.getInstance(),testUser, testShop);
        Order savedOrder = orderService.addOrder(testOrder);

        Order retrievedOrder = orderService.getOrderById(savedOrder.getId());
        assertEquals("Test", testOrder.getCustomer(), savedOrder.getCustomer());
    }
}
