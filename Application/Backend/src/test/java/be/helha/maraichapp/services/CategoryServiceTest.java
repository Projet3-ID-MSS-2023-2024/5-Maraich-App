package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Category;
import be.helha.maraichapp.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void cleanUp(){
        List<Category> allCategories = categoryService.getAllCategories();
        for (Category category : allCategories) {
            categoryService.deleteCategoryById(category.getIdCategory());
        }
    }

    @Test
    @Order(1)
    @Transactional
    public void addCategoryTest(){
        Category testCategory = new Category("Boissons");

        Category savedCategory = categoryService.addCategory(testCategory);

        // Récupérez la catégorie de la base de données
        Category retrievedCategory = categoryRepository.findById(savedCategory.getIdCategory()).orElse(null);

        //Vérifiez que la catégorie n'est pas nul (présent dans la base de données)
        assertNotNull(retrievedCategory);

        //Assurez-vous que les propriétés de la catégorie sont correctes
        assertEquals(testCategory.getNomCategory(), retrievedCategory.getNomCategory());
    }

    @Test
    @Order(2)
    @Transactional
    public void updateCategoryTest(){
        Category testCategory = new Category("Nourriture");

        Category savedCategory = categoryService.addCategory(testCategory);

        // Mettez à jour les informations de la catégorie
        savedCategory.setNomCategory("Vetement");

        // Appelez la méthode updateCategory
        Category updatedCategory = categoryService.updateCategory(savedCategory, savedCategory.getIdCategory());

        // Récupérez la catégorie mis à jour de la base de données
        Category retrievedCategory = categoryRepository.findById(updatedCategory.getIdCategory()).orElse(null);

        // Vérifier que la catégorie mis à jour a les bonnes propriétés
        assertNotNull(retrievedCategory);
        assertEquals("Vetement",retrievedCategory.getNomCategory());
    }

    @Test
    @Order(3)
    @Transactional
    public void getCategoryByIdTest(){
        // Création d'un category pour la recherche
        Category testCategory = new Category("Nourriture");

        // Ajoutez la catégorie à la base de données
        Category savedCategory = categoryService.addCategory(testCategory);

        // Appelez la méthode getCategoryById
        Category retrievedCategory = categoryService.getCategoryById(savedCategory.getIdCategory());

        // Vérifiez que la catégorie récupéré a les bonnes propritétés
        assertNotNull(retrievedCategory);
        assertEquals(testCategory.getNomCategory(), retrievedCategory.getNomCategory());
    }

    @Test
    @Order(4)
    public void testGetAllCategory(){
        // Ajout de plusieurs catégories pour le test
        Category testCategory1 = new Category("Boissons");
        Category testCategory2 = new Category("Nourritures");

        // Ajoutez les catégories à la base de données
        categoryService.addCategory(testCategory1);
        categoryService.addCategory(testCategory2);

        // Appelez la méthode getAllCategory
        List<Category> allCategories = categoryService.getAllCategories();

        // Vérifiez que la liste n'est pas vide
        assertFalse(allCategories.isEmpty());

        // Vérifiez que la liste contient au moins les utilisateurs ajoutés
        assertTrue(allCategories.contains(testCategory1));
        assertTrue(allCategories.contains(testCategory2));
    }

    @Test
    @Order(5)
    public void testDeleteCategoryById(){
        // Créez une catégorie pour la suppression
        Category testCategory = new Category("Nourriture");

        // Ajoutez la catégorie à la base de données
        Category savedCategory = categoryService.addCategory(testCategory);

        // Appelez la méthode deleteCategoryById pour supprimé la catégorie ajouté
        categoryService.deleteCategoryById(savedCategory.getIdCategory());

        Category deletedCategory = categoryService.getCategoryById(savedCategory.getIdCategory());
        assertNull(deletedCategory);

        // Appelez la méthode getAllCategories pour vérifier la taille de la liste après la suppression
        List<Category> allCategoriesAfterDeletion = categoryService.getAllCategories();
        assertEquals(0, allCategoriesAfterDeletion.size());
    }
}
