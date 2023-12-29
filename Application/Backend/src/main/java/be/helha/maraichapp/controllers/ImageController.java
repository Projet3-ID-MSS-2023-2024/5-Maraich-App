package be.helha.maraichapp.controllers;

import be.helha.maraichapp.services.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @GetMapping("/getImage/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName){
        Resource resource = imageService.load(fileName);

        if (resource != null) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file")MultipartFile file){
        String fileName=imageService.saveFile(file);
        return ResponseEntity.ok().body("{\"fileName\": \"" + fileName + "\"}");
    }
}
