package be.helha.maraichapp.services;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;

    private Category testCategory;
    private Category testCategory2;

    private Shop testShop;
    private Shop testShop2;

    private Users testUser;
    private Users testUser2;

    private Product testProduct;
    private Product testProduct2;
    private Product testProduct3;
    private MockMultipartFile file;
    private static final String FILE_CONTENT = "file content";

    @AfterAll
    public void cleanUp(){
        categoryService.deleteCategoryById(testCategory.getIdCategory());
        categoryService.deleteCategoryById(testCategory2.getIdCategory());
        shopService.deleteShop(testShop.getIdShop());
        shopService.deleteShop(testShop2.getIdShop());
        userService.deleteUserById(testUser.getIdUser());
        userService.deleteUserById(testUser2.getIdUser());
    }

    @BeforeAll
    public void setUp() {
        file = new MockMultipartFile("file", "test.png", "image/png", FILE_CONTENT.getBytes());
        testCategory = categoryService.addCategory(new Category("Légume"));
        testCategory2 = categoryService.addCategory(new Category("Fruits"));
        testUser = userService.addUser(new Users("Celik","Esad","0483000000","Password1","40","Rue du du","6030","Charleroi","la00000@student.helha.be",null,null));
        testShop = shopService.addShop(new Shop("Marché","la111@student.helha.be","Rue du du","40","6030","Charleroi","png.png","C'est un marché",testUser));
        testUser2 = userService.addUser(new Users("CelikBis","EsadBis","0483000000","Password2","40","Rue du du","6030","Charleroi","la222222@student.helha.be",null,null));
        testShop2 = shopService.addShop(new Shop("Marchée","la33@student.helha.be","Rue du du","40","6030","Charleroi","png.png","C'est un marché",testUser2));
    }

    @Test
    @Order(1)
    public void addProductTest(){
        testProduct = productService.addProduct(new Product("Carotte",15,"C'est des carottes","",20,40,false,testCategory,testShop), file);
        testProduct2 = productService.addProduct(new Product("Patate",12,"C'est des patates","",10,80,false,testCategory,testShop),file);
        testProduct3 = productService.addProduct(new Product("Pomme",10,"C'est des pommes","",50,20,false,testCategory2,testShop2), file);

        // Récupérez le produit de la base de données
        Product retrievedProduct = productRepository.findById(testProduct.getId()).orElse(null);
        Product retrievedProduct2 = productRepository.findById(testProduct2.getId()).orElse(null);
        Product retrievedProduct3 = productRepository.findById(testProduct3.getId()).orElse(null);


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
    public void updateProductTest(){
        // On change les données du produits
        testProduct.setName("Patate");
        testProduct.setPrice(12);
        testProduct.setCategory(testCategory2);

        //Mets à jour les données du produit
        Product updatedProduct = productService.updateProduct(testProduct.getId(), testProduct, null);

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
    public void testGetProductByIdTest(){
        Product retrievedProduct = productService.getProductById(testProduct.getId());

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
        List<Product> allProduct = productService.getAllProduct();

        assertFalse(allProduct.isEmpty());

        assertTrue(allProduct.contains(testProduct));
        assertTrue(allProduct.contains(testProduct2));
    }

    @Test
    @Order(5)
    public void testGetAllProductByCategory(){
        List<Product> productCategory1 = productService.getAllProductByCategories(testCategory);

        assertFalse(productCategory1.isEmpty());

        assertFalse(productCategory1.contains(testProduct));
        assertTrue(productCategory1.contains(testProduct2));
        assertFalse(productCategory1.contains(testProduct3));
    }

    @Test
    @Order(6)
    public void testGetAllProductByShop(){
        List<Product> productsShop1 = productService.getAllProductByShop(testShop);

        assertFalse(productsShop1.isEmpty());

        assertTrue(productsShop1.contains(testProduct));
        assertTrue(productsShop1.contains(testProduct2));
        assertFalse(productsShop1.contains(testProduct3));
    }

    @Test
    @Order(7)
    public void testDeleteProduct(){
        productService.deleteProductById(testProduct.getId());
        productService.deleteProductById(testProduct2.getId());
        productService.deleteProductById(testProduct3.getId());

        Product deletedProduct = productService.getProductById(testProduct.getId());
        Product deletedProduct2 = productService.getProductById(testProduct2.getId());
        Product deletedProduct3 = productService.getProductById(testProduct3.getId());

        assertNull(deletedProduct);
        assertNull(deletedProduct2);
        assertNull(deletedProduct3);
    }

}
