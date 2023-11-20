package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getShop(){ return categoryRepository.findAll(); }

    @PostMapping
    public Category addCategory(@RequestBody Category category) { return categoryRepository.save(category);}

    @PutMapping
    public Category updateCategory(@RequestBody Category category){
        if (categoryRepository.existsById(category.getIdCategory()))
            return categoryRepository.save(category);
        return null;
    }

    @DeleteMapping
    @RequestMapping("/{id}")
    public void deleteCategory(@PathVariable("id") int id) { categoryRepository.deleteById(id);}
}
