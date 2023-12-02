package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    List<Product> getAllProductByShop(Shop shop); // Pas encore implémenter le shop
    List<Product> getAllProductByCategories(Category category);
    Product getProductById(int id);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    void deleteProductById(int id);
}
