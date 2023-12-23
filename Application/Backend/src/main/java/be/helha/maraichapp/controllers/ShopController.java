package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping
    @RequestMapping("/shops")
    public List<Shop> getShop(){
        return shopService.getShop();
    }

    @GetMapping
    @RequestMapping("/shop/{id}")
    public Shop getShopById(@PathVariable("id")int id){
        return shopService.getShopById(id);
    }

    @GetMapping
    @RequestMapping("/shop/owner/{id}")
    public Shop getShopByIdOwner(@PathVariable("id") int id){ return shopService.getShopByIdOwner(id); }

    @GetMapping
    @RequestMapping("/name/{name}")
    public List<Shop> getShopByName(@PathVariable("name")String name){
        return shopService.getShopByName(name);
    }

    @PostMapping
    @RequestMapping("/add")
    public Shop addShop(@RequestBody Shop shop){
        return shopService.addShop(shop);
    }

    @PostMapping
    @RequestMapping("/addMinimal/{idUser}")
    public Shop addShopMinimal(@PathVariable("idUser")int idUser){
        return shopService.addShopMinimal(idUser);
    }


    @PutMapping
    @RequestMapping("/update")
    public Shop updateShop(@RequestBody Shop shop){
        return shopService.updateShop(shop);
    }

    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public void deleteShop(@PathVariable("id")int id) {
        shopService.deleteShop(id);
    }

}
