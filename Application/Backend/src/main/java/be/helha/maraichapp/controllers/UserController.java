package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Users;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserController {

    @GetMapping("/get/{id}")
    Users getUserById(@PathVariable int id);

    @GetMapping("/getByRank/{rank}")
    List<Users> getUsersByRank(@PathVariable RankEnum rank);

    @GetMapping("/getAll")
    List<Users> getAllUsers();

    @PostMapping("/newUser")
    Users addUser(@RequestBody Users user);

    @PutMapping("/update/admin")
    Users updateUserAdmin(@RequestBody Users user);

    @PutMapping("/update/restricted")
    Users updateUserRestricted(@RequestBody Users updatedUser);

    @DeleteMapping("/delete/{id}")
    void deleteUserById(@PathVariable int id);
}
