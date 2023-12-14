package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Orders;
import be.helha.maraichapp.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }
    @GetMapping
    public List<Orders> getOrders() {
        return orderService.getAllOrders();
    }
    @PostMapping("newOrder")
    public Orders addOrder(@RequestBody Orders orders) {
        return orderService.addOrder(orders);
    }
    @PutMapping("/update/order")
    public Orders updateOrder(@RequestBody Orders orders) {
        return orderService.updateOrder(orders);
    }

    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable int id) {
        orderService.deleteOrderById(id);
    }
}
