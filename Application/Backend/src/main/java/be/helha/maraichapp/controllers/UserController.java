package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/get/{id}")
    public Users getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/getByRank/{rank}")
    public List<Users> getUsersByRank(@PathVariable RankEnum rank) {
        return userService.getUsersByRank(rank).orElse(null);
    }

    @GetMapping("/getAll")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/newUser")
    public Users addUser(@RequestBody Users user) {
        return userService.addUser(user);
    }

    @PutMapping("/update/admin")
    public Users updateUserAdmin(@RequestBody Users user) {
        return userService.updateUserAdmin(user);
    }

    @PutMapping("/update/restricted")
    public Users updateUserRestricted(@RequestBody Users updatedUser) {
        return userService.updateUserRestricted(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }
}
