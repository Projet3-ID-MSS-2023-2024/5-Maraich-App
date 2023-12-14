package be.helha.maraichapp.services;

import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Users;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {

    Users addUser(Users user);

    Users updateUserAdmin(Users user);

    Users updateUserRestricted(Users updatedUser);

    Users getUserById(int id);

    Optional<List<Users>> getUsersByRank(RankEnum rankEnum);

    List<Users> getAllUsers();

    void deleteUserById(int id);
}
