package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Category, Integer> {
}
