package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
}
