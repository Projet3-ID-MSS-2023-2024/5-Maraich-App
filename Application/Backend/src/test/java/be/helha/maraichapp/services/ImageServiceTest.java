package be.helha.maraichapp.services;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    private String savedFileName;

    @Test
    @Order(1)
    public void saveFileTest(){
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "Hello, World!".getBytes());
        savedFileName = imageService.saveFile(file);
        assertTrue(imageService.fileExists(savedFileName));
    }

    @Test
    @Order(2)
    public void loadFileTest(){
        assertNotNull(savedFileName);
        Resource resource = imageService.load(savedFileName);
        assertNotNull(resource);
    }

    @Test
    @Order(3)
    public void deleteFileTest(){
        assertNotNull(savedFileName);
        imageService.deleteFile(savedFileName);
        assertFalse(imageService.fileExists(savedFileName));
    }
}
