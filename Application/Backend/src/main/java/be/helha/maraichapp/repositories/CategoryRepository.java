package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
