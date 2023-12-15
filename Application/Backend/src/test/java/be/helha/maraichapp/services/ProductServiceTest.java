package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
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

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;

    private static Category testCategory;
    private static Category testCategory2;

    private static Shop testShop;
    private static Shop testShop2;

    private static Users testUser;
    private static Users testUser2;

    @AfterEach
    public void cleanUp(){
        List<Product> allProduct = productService.getAllProduct();
        for (Product product : allProduct){
            productService.deleteProductById(product.getId());
        }
        categoryService.deleteCategoryById(testCategory.getIdCategory());
        categoryService.deleteCategoryById(testCategory2.getIdCategory());
        shopService.deleteShop(testShop.getIdShop());
        shopService.deleteShop(testShop2.getIdShop());
        userService.deleteUserById(testUser.getIdUser());
        userService.deleteUserById(testUser2.getIdUser());
    }

    @BeforeEach
    public void setUpCategories() {
        testCategory = categoryService.addCategory(new Category("Légume"));
        testCategory2 = categoryService.addCategory(new Category("Fruits"));
        testUser = userService.addUser(new Users("Celik","Esad","0483000000","Password1","40","Rue du du","6030","Charleroi","la00000@student.helha.be",null,null));
        testShop = shopService.addShop(new Shop("Marché","la111@student.helha.be","Rue du du","40","6030","Charleroi","png.png","C'est un marché",testUser));
        testUser2 = userService.addUser(new Users("CelikBis","EsadBis","0483000000","Password2","40","Rue du du","6030","Charleroi","la222222@student.helha.be",null,null));
        testShop2 = shopService.addShop(new Shop("Marchée","la33@student.helha.be","Rue du du","40","6030","Charleroi","png.png","C'est un marché",testUser2));
    }

    @Test
    @Order(1)
    @Transactional
    public void addProductTest(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);

        Product savedProduct = productService.addProduct(testProduct);

        // Récupérez le produit de la base de données
        Product retrievedProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        // Vérifiez que le produit n'est pas nul
        assertNotNull(retrievedProduct);

        // Assurez-vous que les propriétés du produit sont correctes
        assertEquals(testProduct.getName(), retrievedProduct.getName());
        assertEquals(testProduct.getPrice(), retrievedProduct.getPrice());
        assertEquals(testProduct.getDescription(), retrievedProduct.getDescription());
        assertEquals(testProduct.getPicturePath(), retrievedProduct.getPicturePath());
        assertEquals(testProduct.getQuantity(), retrievedProduct.getQuantity());
        assertEquals(testProduct.getWeight(), retrievedProduct.getWeight());
        assertEquals(testProduct.getCategory(), retrievedProduct.getCategory());
    }

    @Test
    @Order(2)
    @Transactional
    public void updateProductTest(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);

        // On ajoute les données
        Product savedProduct = productService.addProduct(testProduct);

        // On change les données du produits
        savedProduct.setName("Patate");
        savedProduct.setPrice(12);
        savedProduct.setCategory(testCategory2);

        //Mets à jour les données du produit
        Product updatedProduct = productService.updateProduct(savedProduct);

        // Récupérez le produit modifié de la base de données
        Product retrievedProduct = productRepository.findById(updatedProduct.getId()).orElse(null);

        // Vérifier que la catégorie n'est pas null et que les infos ont bien été modifié
        assertNotNull(retrievedProduct);
        assertEquals("Patate", retrievedProduct.getName());
        assertEquals(12,retrievedProduct.getPrice());
        assertEquals(testCategory2, retrievedProduct.getCategory());
    }

    @Test
    @Order(3)
    @Transactional
    public void testGetProductByIdTest(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);

        Product savedProduct = productService.addProduct(testProduct);

        Product retrievedProduct = productService.getProductById(savedProduct.getId());

        assertNotNull(retrievedProduct);
        assertEquals(testProduct.getName(), retrievedProduct.getName());
        assertEquals(testProduct.getPrice(), retrievedProduct.getPrice());
        assertEquals(testProduct.getDescription(), retrievedProduct.getDescription());
        assertEquals(testProduct.getPicturePath(), retrievedProduct.getPicturePath());
        assertEquals(testProduct.getQuantity(), retrievedProduct.getQuantity());
        assertEquals(testProduct.getWeight(), retrievedProduct.getWeight());
        assertEquals(testProduct.getCategory(), retrievedProduct.getCategory());
    }

    @Test
    @Order(4)
    public void testGetAllProductTest(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);
        Product testProduct2 = new Product("Patate",12,"C'est des patates","patate.png",10,80,false,testCategory,testShop);

        productService.addProduct(testProduct);
        productService.addProduct(testProduct2);

        List<Product> allProduct = productService.getAllProduct();

        assertFalse(allProduct.isEmpty());

        assertTrue(allProduct.contains(testProduct));
        assertTrue(allProduct.contains(testProduct2));
    }

    @Test
    @Order(5)
    public void testGetAllProductByCategory(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);
        Product testProduct2 = new Product("Patate",12,"C'est des patates","patate.png",10,80,false,testCategory,testShop);
        Product testProduct3 = new Product("Pomme",10,"C'est des pommes","pomme.png",50,20,false,testCategory2,testShop);

        productService.addProduct(testProduct);
        productService.addProduct(testProduct2);
        productService.addProduct(testProduct3);

        List<Product> productCategory1 = productService.getAllProductByCategories(testCategory);

        assertFalse(productCategory1.isEmpty());

        assertTrue(productCategory1.contains(testProduct));
        assertTrue(productCategory1.contains(testProduct2));
        assertFalse(productCategory1.contains(testProduct3));
    }

    @Test
    @Order(6)
    public void testGetAllProductByShop(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);
        Product testProduct2 = new Product("Patate",12,"C'est des patates","patate.png",10,80,false,testCategory,testShop);
        Product testProduct3 = new Product("Pomme",10,"C'est des pommes","pomme.png",50,20,false,testCategory2,testShop2);

        productService.addProduct(testProduct);
        productService.addProduct(testProduct2);
        productService.addProduct(testProduct3);

        List<Product> productsShop1 = productService.getAllProductByShop(testShop);

        assertFalse(productsShop1.isEmpty());

        assertTrue(productsShop1.contains(testProduct));
        assertTrue(productsShop1.contains(testProduct2));
        assertFalse(productsShop1.contains(testProduct3));
    }

    @Test
    @Order(7)
    public void testDeleteProduct(){
        Product testProduct = new Product("Carotte",15,"C'est des carottes","carotte.png",20,40,false,testCategory,testShop);

        Product savedProduct = productService.addProduct(testProduct);

        productService.deleteProductById(savedProduct.getId());

        Product deletedProduct = productService.getProductById(savedProduct.getId());
        assertNull(deletedProduct);

        List<Product> allProductAfterDeletion = productService.getAllProduct();
        assertEquals(0, allProductAfterDeletion.size());
    }

}
