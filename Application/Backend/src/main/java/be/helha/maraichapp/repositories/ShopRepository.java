package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Integer> {

    boolean existsByName(String name);
    List<Shop> findByName(String name);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shop s WHERE s.owner.idUser = ?1")
    boolean existsByOwnerId(int userId);
}
