package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    List<Product> getAllProductByShop(Shop shop);
    List<Product> getAllProductByCategories(Category category);
    Product getProductById(int id);
    Product addProduct(Product product, MultipartFile file);
    Product updateProduct(int id, Product product, MultipartFile file);
    void deleteProductById(int id);
}
