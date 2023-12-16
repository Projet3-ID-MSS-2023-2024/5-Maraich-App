package be.helha.maraichapp.controllers;

import be.helha.maraichapp.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/upload", produces = "application/json")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file")MultipartFile file){
        String fileName=imageService.saveFile(file);
        return ResponseEntity.ok().body("{\"fileName\": \"" + fileName + "\"}");
    }
}
