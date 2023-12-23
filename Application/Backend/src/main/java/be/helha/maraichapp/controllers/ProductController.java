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
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/get-all-by-shop")
    public ResponseEntity<List<Product>> getAllProductsByShop(@RequestParam("shopId") int shopId){
        Shop shop = shopService.getShopById(shopId);
        if (shop != null){
            List<Product> products = productService.getAllProductByShop(shop);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*
    @PostMapping("/new")
    public ResponseEntity<Product> addProduct(@RequestPart("product") Product product,@RequestPart("file") MultipartFile file) {
        try {
            String fileName = imageService.saveFile(file);
            product.setPicturePath(fileName);

            Product addedProduct = productService.addProduct(product);
            return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
        }catch (Exception e){
            System.err.println("Error adding product: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestPart("product") Product updatedProduct, @RequestPart(name = "file", required = false) MultipartFile file){
        try {
            Product existingProduct = productService.getProductById(id);

            if (existingProduct != null){
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setDescription(updatedProduct.getDescription());
                existingProduct.setQuantity(updatedProduct.getQuantity());
                existingProduct.setWeight(updatedProduct.getWeight());
                existingProduct.setUnity(updatedProduct.isUnity());

                if (file!= null && !file.isEmpty()){
                    imageService.deleteFile(existingProduct.getPicturePath());
                    String fileName = imageService.saveFile(file);
                    existingProduct.setPicturePath(fileName);
                }

                Product updated = productService.updateProduct(existingProduct);

                return new ResponseEntity<>(updated, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

     */
}
