package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.repositories.CategoryRepository;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ReservationRepository;
import be.helha.maraichapp.repositories.ShopRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private final CategoryRepository categoryRepository;

    private final ShopRepository shopRepository;
  
    private final ProductRepository productRepository;

    private final ImageService imageService;
    private final ReservationRepository reservationRepository;

    public ProductServiceImpl(ProductRepository productRepository, ImageService imageService, CategoryRepository categoryRepository, ShopRepository shopRepository, ReservationRepository reservationRepository){
        this.productRepository = productRepository;
        this.imageService = imageService;
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
        this.reservationRepository = reservationRepository;
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
        List<Product> productList = productRepository.findProductByShop(shop);
        // Parcourir la liste des produits
        for (Product product : productList) {
            // Récupérer la somme des quantités réservées pour le produit donné
            Double totalReservedQuantity = reservationRepository.getTotalReservedQuantityByProductId(product.getId()).orElse(0.0);

            // Ajouter l'entrée à la map
            if (product.isUnity()) {
                product.setQuantity(product.getQuantity() - totalReservedQuantity.intValue());
            } else {
                product.setWeight(product.getWeight() - totalReservedQuantity);
            }
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
    public double getQuantityAvailableByIdProduct(int idProduct){
        Product product = productRepository.findById(idProduct).orElseThrow(()-> new RuntimeException("Product not found !"));
        double quantityWithoutReserve;
        if(product.isUnity()){
            quantityWithoutReserve = product.getQuantity();
        } else {
            quantityWithoutReserve = product.getWeight();
        }
        return quantityWithoutReserve - reservationRepository.getTotalReservedQuantityByProductId(idProduct).orElse(0.0);
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
