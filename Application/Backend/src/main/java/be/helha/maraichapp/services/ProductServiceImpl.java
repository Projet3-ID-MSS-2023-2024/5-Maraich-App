package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.repositories.CategoryRepository;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductByShop(Shop shop) {
        return productRepository.findProductByShop(shop);
    }

    @Override
    public List<Product> getAllProductByCategories(Category category) {
        return productRepository.findProductByCategory(category);
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product addProduct(Product product) {
        product.setCategory(categoryRepository.findById(product.getCategory().getIdCategory()).orElseThrow(()-> new RuntimeException("Category not found!")));
        product.setShop(shopRepository.findById(product.getShop().getIdShop()).orElseThrow(() -> new RuntimeException("Shop not found")));
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (productRepository.existsById(product.getId())){
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
}
