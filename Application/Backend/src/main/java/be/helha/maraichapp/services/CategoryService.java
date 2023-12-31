package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.repositories.CategoryRepository;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
    Category addCategory(Category category);
    Category updateCategory(Category category, int id);
    void deleteCategoryById(int id);
}
