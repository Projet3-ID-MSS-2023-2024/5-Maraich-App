package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Integer> {

    boolean existsByName(String name);
    List<Shop> findByName(String name);
}
