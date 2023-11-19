package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.repositories.CategoryRepository;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategoryById(int id);
}
