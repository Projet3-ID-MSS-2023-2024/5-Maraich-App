package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopRepository shopRepository;

    @GetMapping
    public List<Shop> getShop(){
        return shopRepository.findAll();
    }

    @PostMapping
    public Shop addShop(@RequestBody Shop shop){
        return shopRepository.save(shop);
    }

    @PutMapping
    public Shop updateShop(@RequestBody Shop shop){
        if (shopRepository.existsById(shop.getIdShop()))
            return shopRepository.save(shop);
        return null;
    }

    @DeleteMapping
    @RequestMapping("/{id}")
    public void deleteShop(@PathVariable("id")int id) {
        shopRepository.deleteById(id);
    }

}
