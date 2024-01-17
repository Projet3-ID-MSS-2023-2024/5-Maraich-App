package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    public Orders addOrder(Orders orders) {
        // Vérifier si le client existe déjà dans la base de données
        Users customer = userRepository.findById(orders.getCustomer().getIdUser())
                .orElseThrow(() -> new RuntimeException("Customer not found !"));
        orders.setCustomer(customer);

        // Vérifier si le vendeur du magasin existe dans la base de données
        Shop shop = shopRepository.findById(orders.getShopSeller().getIdShop())
                .orElseThrow(() -> new RuntimeException("Shop seller not found !"));
        orders.setShopSeller(shop);

        // Définir la date de commande
        orders.setOrderDate(Instant.now());

        // Associer les OrderProducts à l'entité Orders
        List<OrderProduct> orderProductList = orders.getOrderProducts();
        for (OrderProduct orderProduct : orderProductList) {
            orderProduct.setOrders(orders);

            // Vérifier si le produit associé à OrderProduct existe dans la base de données
            Product product = productRepository.findById(orderProduct.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found !"));
            orderProduct.setProduct(product);
        }
        for (OrderProduct orderProduct : orderProductList) {
            orderProduct.setId(new OrderProduct.OrderProductId(0, orderProduct.getProduct().getId()));
        }

        // Sauvegarder l'entité Orders avec les OrderProducts associés
        orders = orderRepository.save(orders);

        // Sauvegarder les OrderProducts après avoir sauvegardé Orders
        for (OrderProduct orderProduct : orderProductList) {
            orderProduct.setId(new OrderProduct.OrderProductId(orderProduct.getOrders().getId(), orderProduct.getProduct().getId()));
            orderProductRepository.save(orderProduct);
            Product product = productRepository.findById(orderProduct.getProduct().getId()).get();
            if(product.isUnity()){
                product.setQuantity(product.getQuantity()-orderProduct.getQuantity());
            } else {
                product.setWeight(product.getWeight()-orderProduct.getQuantity());
            }
            productRepository.save(product);

        }

        return orders;
    }





    @Override
    public List<Orders> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public List<Orders> getAllFromCustomer(int customerId) {
        return this.orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Orders> getAllFromShopSeller(int shopSellerId) {
        return this.orderRepository.findByShopSellerId(shopSellerId);
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
