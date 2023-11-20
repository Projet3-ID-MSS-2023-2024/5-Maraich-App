package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    private static Category testCategory;
    private static Category testCategory2;

    @AfterEach
    public void cleanUp(){
        List<Product> allProduct = productService.getAllProduct();
        for (Product product : allProduct){
            productService.deleteProductById(product.getIdProduct());
        }
        categoryService.deleteCategoryById(testCategory.getIdCategory());
        categoryService.deleteCategoryById(testCategory2.getIdCategory());
    }

    @BeforeEach
    public void setUpCategories() {
        testCategory = categoryService.addCategory(new Category("Légume"));
        testCategory2 = categoryService.addCategory(new Category("Fruits"));
    }

    @Test
    @Order(1)
    @Transactional
    public void addProductTest(){
        Product testProduct = new Product("Carotte", 15.00, testCategory);

        Product savedProduct = productService.addProduct(testProduct);

        // Récupérez le produit de la base de données
        Product retrievedProduct = productRepository.findById(savedProduct.getIdProduct()).orElse(null);

        // Vérifiez que le produit n'est pas nul
        assertNotNull(retrievedProduct);

        // Assurez-vous que les propriétés du produit sont correctes
        assertEquals(testProduct.getNameProduct(), retrievedProduct.getNameProduct());
        assertEquals(testProduct.getPriceProduct(), retrievedProduct.getPriceProduct());
        assertEquals(testProduct.getCategory(), retrievedProduct.getCategory());
    }

    @Test
    @Order(2)
    @Transactional
    public void updateProductTest(){
        Product testProduct = new Product("Carotte", 15.00, testCategory);

        // On ajoute les données
        Product savedProduct = productService.addProduct(testProduct);

        // On change les données du produits
        savedProduct.setNameProduct("Patate");
        savedProduct.setPriceProduct(12);
        savedProduct.setCategory(testCategory2);

        //Mets à jour les données du produit
        Product updatedProduct = productService.updateProduct(savedProduct);

        // Récupérez le produit modifié de la base de données
        Product retrievedProduct = productRepository.findById(updatedProduct.getIdProduct()).orElse(null);

        // Vérifier que la catégorie n'est pas null et que les infos ont bien été modifié
        assertNotNull(retrievedProduct);
        assertEquals("Patate", retrievedProduct.getNameProduct());
        assertEquals(12,retrievedProduct.getPriceProduct());
        assertEquals(testCategory2, retrievedProduct.getCategory());
    }

    @Test
    @Order(3)
    @Transactional
    public void getProductByIdTest(){
        Product testProduct = new Product("Carotte", 15.00, testCategory);

        Product savedProduct = productService.addProduct(testProduct);

        Product retrievedProduct = productService.getProductById(savedProduct.getIdProduct());

        assertNotNull(retrievedProduct);
        assertEquals(testProduct.getNameProduct(), retrievedProduct.getNameProduct());
        assertEquals(testProduct.getPriceProduct(), retrievedProduct.getPriceProduct());
        assertEquals(testProduct.getCategory(), retrievedProduct.getCategory());
    }

    @Test
    @Order(4)
    public void getAllProductTest(){
        Product testProduct = new Product("Carotte", 15.00, testCategory);
        Product testProduct2 = new Product("Patate", 12.00, testCategory);

        productService.addProduct(testProduct);
        productService.addProduct(testProduct2);

        List<Product> allProduct = productService.getAllProduct();

        assertFalse(allProduct.isEmpty());

        assertTrue(allProduct.contains(testProduct));
        assertTrue(allProduct.contains(testProduct2));
    }

    @Test
    @Order(5)
    public void getAllProductByCategory(){
        Product testProduct = new Product("Carotte", 15.00, testCategory);
        Product testProduct2 = new Product("Patate", 12.00, testCategory);
        Product testProduct3 = new Product("Pomme", 10, testCategory2);

        productService.addProduct(testProduct);
        productService.addProduct(testProduct2);
        productService.addProduct(testProduct3);

        List<Product> productCategory1 = productService.getAllProductByCategories(testCategory);

        System.out.println("Product List: " + productCategory1);

        assertFalse(productCategory1.isEmpty());

        assertTrue(productCategory1.contains(testProduct));
        assertTrue(productCategory1.contains(testProduct2));
        assertFalse(productCategory1.contains(testProduct3));
    }

    @Test
    @Order(6)
    public void testDeleteProduct(){
        Product testProduct = new Product("Carotte", 15, testCategory);

        Product savedProduct = productService.addProduct(testProduct);

        productService.deleteProductById(savedProduct.getIdProduct());

        Product deletedProduct = productService.getProductById(savedProduct.getIdProduct());
        assertNull(deletedProduct);

        List<Product> allProductAfterDeletion = productService.getAllProduct();
        assertEquals(0, allProductAfterDeletion.size());
    }

}
