package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.services.CategoryService;
import be.helha.maraichapp.services.ImageService;
import be.helha.maraichapp.services.ProductService;
import be.helha.maraichapp.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ImageService imageService;
    private final CategoryService categoryService;
    private final ShopService shopService;

    @Autowired
    public ProductController(ProductService productService, ImageService imageService, CategoryService categoryService, ShopService shopService) {
        this.productService = productService;
        this.imageService = imageService;
        this.categoryService = categoryService;
        this.shopService = shopService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/get-all-by-categories")
    public ResponseEntity<List<Product>> getAllProductsByCategories(@RequestParam("categoryId") int categoryId){
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null){
            List<Product> products = productService.getAllProductByCategories(category);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getQuantityAvailable/{id}")
    public double getQuantityAvailableByIdProduct(@PathVariable("id") int idProduct){
        return this.productService.getQuantityAvailableByIdProduct(idProduct);
    }

    @GetMapping("/get-all-by-shop/{id}")
    public ResponseEntity<List<Product>> getAllProductsByShop(@PathVariable("id") int shopId){
        Shop shop = shopService.getShopById(shopId);
        if (shop != null){
            List<Product> products = productService.getAllProductByShop(shop);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        try {
            Product addedProduct = productService.addProduct(product);
            return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
        }catch (Exception e){
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable("id") int id, @RequestBody Product updatedProduct){
        return productService.updateProduct(id, updatedProduct);
    }


    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") int id) {
        try {
            Product existingProduct = productService.getProductById(id);

            if (existingProduct != null){
                imageService.deleteFile(existingProduct.getPicturePath());

                productService.deleteProductById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
