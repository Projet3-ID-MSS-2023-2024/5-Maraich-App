package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
