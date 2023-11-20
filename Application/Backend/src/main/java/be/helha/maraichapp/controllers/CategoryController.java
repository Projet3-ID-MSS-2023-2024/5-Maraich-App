package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/getAll")
    public List<Category> getCategory(){ return categoryService.getAllCategories(); }

    @PostMapping("/new")
    public Category addCategory(@RequestBody Category category) { return categoryService.addCategory(category);}

    @PutMapping("/update")
    public Category updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public void deleteCategory(@PathVariable("id") int id) { categoryService.deleteCategoryById(id);}
}
