package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Users getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @Override
    public List<Users> getUsersByRank(@PathVariable RankEnum rank) {
        return userService.getUsersByRank(rank).orElse(null);
    }

    @Override
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public Users addUser(@RequestBody Users user) {
        return userService.addUser(user);
    }

    @Override
    public Users updateUserAdmin(@RequestBody Users user) {
        return userService.updateUserAdmin(user);
    }

    @Override
    public Users updateUserRestricted(@RequestBody Users updatedUser) {
        return userService.updateUserRestricted(updatedUser);
    }

    @Override
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }
}
