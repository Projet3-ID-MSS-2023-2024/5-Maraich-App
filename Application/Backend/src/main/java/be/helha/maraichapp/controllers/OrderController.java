package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Order;
import be.helha.maraichapp.services.OrderService;
import be.helha.maraichapp.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    OrderServiceImpl orderService;

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }
    @GetMapping
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }
    @PostMapping("newOrder")
    public Order addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }
    @PutMapping("/update/order")
    public Order updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable int id) {
        orderService.deleteOrderById(id);
    }
}
