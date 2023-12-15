package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Order;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.OrderRepository;
import be.helha.maraichapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Order addOrder(Order order) {
        ExampleMatcher userMatcher = ExampleMatcher.matching();
        // Verify if the customer already exists in the database
        Users customer = order.getCustomer();
        Example<Users> customerExample = Example.of(customer, userMatcher);
        boolean customerExist = userRepository.exists(customerExample);
        // Verify if the Shop owner already exists in the database
        Users owner = order.getShopSeller().getOwner();
        Example<Users> ownerExample = Example.of(owner, userMatcher);
        boolean ownerExist = userRepository.exists(ownerExample);
        // We verify if both of them are false, we throw an exception if that's the case
        if (!ownerExist && !customerExist) {
            throw new RuntimeException("Customer or Owner doesn't exists in the database");
        }
        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        Optional<Order> orderOptional = this.orderRepository.findById(id);
        return orderOptional.orElse(null);
    }

    @Override
    public Order updateOrder(Order order) {
        if (this.orderRepository.existsById(order.getId())){
            return this.orderRepository.save(order);
        }
        return null;
    }

    @Override
    public void deleteOrderById(int id) {
        this.orderRepository.deleteById(id);
    }

}
