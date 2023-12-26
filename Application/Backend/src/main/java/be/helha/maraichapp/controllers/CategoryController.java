package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getCategory(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") int id){
        Category category = categoryService.getCategoryById(id);
        if (category != null){
            return new ResponseEntity<>(category, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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
