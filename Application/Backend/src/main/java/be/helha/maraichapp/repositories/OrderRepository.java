package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

    @Query("SELECT o FROM Orders o WHERE o.shopSeller.idShop = :shopSellerId")
    List<Orders> findByShopSellerId(int shopSellerId);

    @Query("SELECT o FROM Orders o WHERE o.customer.idUser = :customerID")
    List<Orders> findByCustomerId(int customerId);
}
