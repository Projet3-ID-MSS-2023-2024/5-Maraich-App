package be.helha.maraichapp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.path}")
    private String uploadDirectory;

    public String saveFile(MultipartFile file){
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path targetLocation = Paths.get(uploadDirectory).resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file:",e);
        }
    }

    public Resource load(String fileName){
        try {
            Path file = Paths.get(uploadDirectory).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()){
                return resource;
            }else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String fileName){
        try {
            Path filePath = Paths.get(uploadDirectory).resolve(fileName);
            Files.deleteIfExists(filePath);
        }catch (IOException e){
            throw new RuntimeException("Failed to delete this: " + fileName);
        }
    }

}
