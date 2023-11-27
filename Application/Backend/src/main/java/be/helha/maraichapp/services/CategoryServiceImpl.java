package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        if (categoryRepository.existsById(category.getIdCategory())){
            return categoryRepository.save(category);
        }
        return null;
    }

    @Override
    public void deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
    }
}
