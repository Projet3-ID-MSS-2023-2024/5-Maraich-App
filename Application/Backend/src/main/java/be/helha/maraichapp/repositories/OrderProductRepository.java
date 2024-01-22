package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProduct.OrderProductId> {
}