package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Orders;

import java.util.List;

public interface OrderService{
    Orders addOrder(Orders orders);
    List<Orders> getAllOrders();
    List<Orders> getAllFromCustomer(int customerId);
    List<Orders> getAllFromShopSeller(int shopSellerId);
    Orders getOrderById(int id);
    Orders updateOrder(Orders orders);
    void deleteOrderById(int id);

}
