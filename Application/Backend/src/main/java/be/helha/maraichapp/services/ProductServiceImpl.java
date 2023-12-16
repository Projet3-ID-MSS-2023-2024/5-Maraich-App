package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.repositories.CategoryRepository;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ShopRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public Product addProduct(Product product, MultipartFile file) {
        if (isInvalidProduct(product)){
            throw new IllegalArgumentException("Invalid product");
        }
        try {
            product.setCategory(categoryRepository.findById(product.getCategory().getIdCategory()).orElseThrow(()-> new RuntimeException("Category not found!")));
            product.setShop(shopRepository.findById(product.getShop().getIdShop()).orElseThrow(() -> new RuntimeException("Shop not found")));
            String fileName = imageService.saveFile(file);
            product.setPicturePath(fileName);
            return productRepository.save(product);
        }catch (Exception e){
            throw new RuntimeException("Error adding product", e);
        }
    }

    @Override
    public Product updateProduct(int id, Product updatedProduct, MultipartFile file) {
        if (isInvalidProduct(updatedProduct) || !productRepository.existsById(id)){
            throw new IllegalArgumentException("Invalid product");
        }

        try {
            Product existingProduct = getProductById(id);
            if (existingProduct != null){
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setDescription(updatedProduct.getDescription());
                existingProduct.setUnity(updatedProduct.isUnity());
                existingProduct.setWeight(updatedProduct.getWeight());
                existingProduct.setQuantity(updatedProduct.getQuantity());
                existingProduct.setCategory(updatedProduct.getCategory());
                existingProduct.setShop(updatedProduct.getShop());

                if (file != null && !file.isEmpty()){
                    imageService.deleteFile(existingProduct.getPicturePath());
                    String fileName = imageService.saveFile(file);
                    existingProduct.setPicturePath(fileName);
                }

                return productRepository.save(existingProduct);
            }else {
                throw new EntityNotFoundException("Product not found with id: " + id);
            }
        }catch (Exception e){
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public void deleteProductById(int id) {
        if (id <= 0){
            throw new IllegalArgumentException("Invalid product ID");
        }
        Product product = getProductById(id);
        imageService.deleteFile(product.getPicturePath());
        productRepository.deleteById(id);
    }

    private boolean isInvalidProduct(Product product){
        return product == null ||
                product.getCategory() == null ||
                product.getName() == null || product.getName().isEmpty() ||
                product.getPrice() <= 0 ||
                product.getDescription() == null || product.getDescription().isEmpty() ||
                product.getQuantity() <= 0 ||
                product.getWeight() <= 0;
    }
}
