package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Order;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService{
    Order addOrder(Order order);
    List<Order> getAllOrders();
    Order getOrderById(int id);
    Order updateOrder(Order order);
    void deleteOrderById(int id);

}
