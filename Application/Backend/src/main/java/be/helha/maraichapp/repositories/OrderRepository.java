package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

}
