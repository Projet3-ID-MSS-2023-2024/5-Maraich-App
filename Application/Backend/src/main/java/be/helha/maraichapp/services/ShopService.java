package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Address;
import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.RankRepository;
import be.helha.maraichapp.repositories.ShopRepository;
import be.helha.maraichapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ShopService {

    @Autowired
    ShopRepository shopRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    RankRepository rankRepository;

    public List<Shop> getShopAdmin() {
        return shopRepository.findAll();
    }

    public Shop getShopById(int id) {
        return shopRepository.findById(id).orElseThrow(() -> new RuntimeException("Shop not found with ID : " + id));
    }

    public List<Shop> getShopByName(String name) {
        List<Shop> foundShop = shopRepository.findByName(name);
        if (foundShop.isEmpty()) throw new RuntimeException("Shop not found with name : " + name);
        return foundShop;
    }

    public Shop getShopByIdOwner(int id) {
        Users user = userRepository.findById(id).orElseThrow();
        return user.getShop();
    }

    public Shop addShop(Shop shop) {
        Users usersDB = userRepository.findById(shop.getOwner().getIdUser()).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        shop.setOwner(usersDB);
        boolean dataIsOk = dataShopVerification(shop);

        if (!dataIsOk) throw new RuntimeException("Invalid data");
        if (shopRepository.existsByName(shop.getName())) throw new RuntimeException("Shop's name already used");
        if (usersDB.getRank().getName() != RankEnum.ADMINISTRATOR)
            usersDB.setRank(rankRepository.findByName(RankEnum.MARAICHER).orElseThrow(() -> new RuntimeException("Unknown rank")));
        shop.setOwner(usersDB);
        shop.setShopIsOkay(true);
        shop.setEnable(false);
        shop = shopRepository.save(shop);
        usersDB.setShop(shop);
        userService.updateUserAdmin(usersDB);
        return shop;
    }

    public void addShopMinimal(Users users) {
        Shop shop = new Shop();
        int idUser = users.getIdUser();
        if (users.getRank().getName() != RankEnum.ADMINISTRATOR)
            users.setRank(rankRepository.findByName(RankEnum.MARAICHER).orElseThrow(() -> new RuntimeException("Unknown rank")));
        shop.setOwner(users);
        shop.setName("name" + idUser);
        shop.setEmail("email" + idUser);
        shop.setAddress(new Address("road" + idUser, "postCode" + idUser, "city" + idUser, "number" + idUser));
        shop.setDescription("description" + idUser);
        shop.setShopIsOkay(false);
        shop.setEnable(false);
        shop = shopRepository.save(shop);
        users.setShop(shop);
        userRepository.save(users);
    }

    public Shop updateShop(Shop shop) {
        Users users = userRepository.findById(shop.getOwner().getIdUser()).orElseThrow(() -> new RuntimeException("Owner not found by id !"));
        shop.setOwner(users);
        boolean dataIsOk = dataShopVerification(shop);
        if (!dataIsOk) throw new RuntimeException("Invalid data");
        if (shopRepository.existsById(shop.getIdShop())) {
            shop.setShopIsOkay(true);
            return shopRepository.save(shop);
        }
        throw new RuntimeException("Shop not found with ID : " + shop.getIdShop());
    }

    public void deleteShop(int id) {
        Optional<Shop> shopDB = shopRepository.findById(id);
        Users usersDB;
        if (shopDB.isPresent()) {
            usersDB = shopDB.get().getOwner();
            usersDB.setRank(rankRepository.findByName(RankEnum.CUSTOMER).orElseThrow(() -> new RuntimeException("Unknown rank")));
            usersDB.setShop(null);
            userService.updateUserAdmin(usersDB);
            shopRepository.deleteById(id);
        } else {
            throw new RuntimeException("Shop not found with ID : " + id);
        }
    }

    public boolean dataShopVerification(Shop shop) {
        final String lettersOnlyRegex = "^[A-Za-zÀ-ÿ'\\- ]+$";
        final String lettersAndDigitOnlyRegex = "^[a-zA-Z0-9 ]+$";
        final String emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (!userRepository.existsInDatabase(shop.getOwner())) {
            throw new RuntimeException("User doesn't exist");
        }
        if (!Pattern.matches(lettersOnlyRegex, shop.getAddress().getCity()) && !Pattern.matches(lettersAndDigitOnlyRegex, shop.getAddress().getRoad())) {
            throw new RuntimeException("Invalid address");
        }
        if (shop.getName().isEmpty() || shop.getEmail().isEmpty() || shop.getAddress().getRoad().isEmpty()
                || shop.getAddress().getNumber().isEmpty() || shop.getAddress().getPostCode().isEmpty()
                || shop.getAddress().getCity().isEmpty()
                || shop.getOwner().getFirstName().isEmpty() || shop.getOwner().getSurname().isEmpty()
                || shop.getOwner().getPassword().isEmpty() || shop.getOwner().getUsername().isEmpty()
                || shop.getOwner().getPhoneNumber().isEmpty() || shop.getOwner().getEmail().isEmpty()) {
            throw new RuntimeException("Empty field");
        }
        if (!Pattern.matches(emailRegex, shop.getEmail())) {
            throw new RuntimeException("Invalid email");
        } else return true;
    }

    public List<Shop> getShop() {
        return shopRepository.findAllEnabledShops();
    }

    public boolean turnOnOff(int idShop) {
        Shop shop = shopRepository.findById(idShop).orElseThrow(() -> new RuntimeException("Shop not found !"));
        boolean turnTo = !shop.isEnable();
        shop.setEnable(turnTo);
        shopRepository.save(shop);
        return turnTo;
    }
}
