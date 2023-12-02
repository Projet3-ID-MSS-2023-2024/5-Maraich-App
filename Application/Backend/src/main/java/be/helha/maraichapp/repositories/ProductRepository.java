package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findProductByCategory(Category category);
    List<Product> findProductByShop(Shop shop);
}
