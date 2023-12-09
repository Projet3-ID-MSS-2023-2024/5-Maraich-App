package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.services.ImageService;
import be.helha.maraichapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/getAll")
    public List<Product> getProduct(){ return productService.getAllProduct(); }

    @PostMapping("/getAllByCategories")
    public List<Product> getProductByCategories(@RequestBody Category category){ return productService.getAllProductByCategories(category); }

    @PostMapping("/new")
    public Product addProduct(@RequestPart("product") Product product,@RequestPart("file") MultipartFile file) {
        String fileName=imageService.saveFile(file);
        product.setPicturePath(fileName);
        return productService.addProduct(product);
    }

    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public void deleteProduct(@PathVariable("id") int id) { productService.deleteProductById(id);}
}
