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
    @GetMapping("/get/customer/{customerId}")
    public List<Orders> getOrdersFromCustomer(@PathVariable("customerId") int customerId) {
        return orderService.getAllFromCustomer(customerId);
    }
    @GetMapping("/get/shop/{shopId}")
    public List<Orders> getOrdersFromShopSeller(@PathVariable("shopId") int shopId) {
        return orderService.getAllFromShopSeller(shopId);
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
    public void deleteOrderById(@PathVariable("id") int id) {
        orderService.deleteOrderById(id);
    }
}
