package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.repositories.CategoryRepository;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ShopRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;

    private final ShopRepository shopRepository;
  
    private final ProductRepository productRepository;

    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, ImageService imageService, CategoryRepository categoryRepository, ShopRepository shopRepository){
        this.productRepository = productRepository;
        this.imageService = imageService;
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductByShop(Shop shop) {
        if (shop == null){
            throw new IllegalArgumentException("Shop cannot be null");
        }
        return productRepository.findProductByShop(shop);
    }

    @Override
    public List<Product> getAllProductByCategories(Category category) {
        if (category == null){
            throw new IllegalArgumentException("Category cannot be null");
        }
        return productRepository.findProductByCategory(category);
    }

    @Override
    public Product getProductById(int id) {
        if (id <= 0){
            throw new IllegalArgumentException("Invalid product ID");
        }
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product addProduct(Product product) {
        try {
            product.setCategory(categoryRepository.findById(product.getCategory().getIdCategory()).orElseThrow(()-> new RuntimeException("Category not found!")));
            product.setShop(shopRepository.findById(product.getShop().getIdShop()).orElseThrow(() -> new RuntimeException("Shop not found")));
            return productRepository.save(product);
        }catch (Exception e){
            throw new RuntimeException("Error adding product", e);
        }
    }

    @Override
    public Product updateProduct(int id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        if (productRepository.existsById(updatedProduct.getId())){
            if (!(updatedProduct.getPicturePath().equals(existingProduct.getPicturePath()))){
                imageService.deleteFile(existingProduct.getPicturePath());
                existingProduct.setPicturePath(updatedProduct.getPicturePath());
            }
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    @Override
    public void deleteProductById(int id) {
        if (id <= 0){
            throw new IllegalArgumentException("Invalid product ID");
        }
        productRepository.deleteById(id);
    }
}
