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

    @GetMapping("/get/{id}")
    public Category getCategoryById(@PathVariable("id") int id){ return categoryService.getCategoryById(id); }

    @PostMapping("/new")
    public Category addCategory(@RequestBody Category category) { return categoryService.addCategory(category);}

    @PutMapping("/update/{id}")
    public Category updateCategory(@RequestBody Category category, @PathVariable("id") int id){
        return categoryService.updateCategory(category, id);
    }

    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public void deleteCategory(@PathVariable("id") int id) { categoryService.deleteCategoryById(id);}
}
