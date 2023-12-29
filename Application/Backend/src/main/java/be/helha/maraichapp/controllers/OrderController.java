package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Orders;
import be.helha.maraichapp.services.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderServiceImpl orderService;

    @GetMapping("/get/{id}")
    public Orders getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }
    @GetMapping("/getAll")
    public List<Orders> getOrders() {
        return orderService.getAllOrders();
    }
    @PostMapping("addOrder")
    public Orders addOrder(@RequestBody Orders orders) {
        return orderService.addOrder(orders);
    }
    @PutMapping("/update/order")
    public Orders updateOrder(@RequestBody Orders orders) {
        return orderService.updateOrder(orders);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrderById(@PathVariable int id) {
        orderService.deleteOrderById(id);
    }
}
