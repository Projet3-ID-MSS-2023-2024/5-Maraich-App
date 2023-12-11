package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Orders;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.OrderRepository;
import be.helha.maraichapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Orders addOrder(Orders orders) {
        // Verify if the customer already exists in the database
        Users customer = orders.getCustomer();
        if (!userRepository.existsById(customer.getIdUser())) {
            throw new RuntimeException("Customer doesn't exist in the database");
        }
        // Verify if the Shop owner already exists in the database
        Users owner = orders.getShopSeller().getOwner();
        if (!userRepository.existsById(owner.getIdUser())) {
            throw new RuntimeException("Owner doesn't exist in the database");
        }
        return this.orderRepository.save(orders);
    }

    @Override
    public List<Orders> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Orders getOrderById(int id) {
        Optional<Orders> orderOptional = this.orderRepository.findById(id);
        return orderOptional.orElse(null);
    }

    @Override
    public Orders updateOrder(Orders orders) {
        if (this.orderRepository.existsById(orders.getId())){
            return this.orderRepository.save(orders);
        }
        return null;
    }

    @Override
    public void deleteOrderById(int id) {
        this.orderRepository.deleteById(id);
    }

}
