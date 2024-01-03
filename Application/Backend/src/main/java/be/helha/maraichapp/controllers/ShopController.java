package be.helha.maraichapp.controllers;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.repositories.ShopRepository;
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
    @RequestMapping("/getAll")
    public List<Shop> getShop(){
        return shopService.getShop();
    }

    @GetMapping
    @RequestMapping("/getAllAdmin")
    public List<Shop> getShopAdmin(){
        List<Shop> shops = shopService.getShopAdmin();
        return shops;
    }

    @GetMapping
    @RequestMapping("/getById/{id}")
    public Shop getShopById(@PathVariable("id")int id){
        return shopService.getShopById(id);
    }

    @GetMapping
    @RequestMapping("/owner/{id}")
    public Shop getShopByIdOwner(@PathVariable("id") int id){ return shopService.getShopByIdOwner(id); }

    @GetMapping
    @RequestMapping("/getByName/{name}")
    public List<Shop> getShopByName(@PathVariable("name")String name){
        return shopService.getShopByName(name);
    }
    @PostMapping
    @RequestMapping("/add")
    public Shop addShop(@RequestBody Shop shop){
        return shopService.addShop(shop);
    }
    @PutMapping
    @RequestMapping("/update")
    public Shop updateShop(@RequestBody Shop shop){
        return shopService.updateShop(shop);
    }

    @GetMapping
    @RequestMapping("/turnOnOrOff/{idShop}")
    public boolean turnOnOff(@PathVariable("idShop") int idShop) {
        return shopService.turnOnOff(idShop);
    }

    @DeleteMapping
    @RequestMapping("/delete/{id}")
    public void deleteShop(@PathVariable("id")int id) {
        shopService.deleteShop(id);
    }
}