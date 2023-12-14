package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {
    Optional<Validation> findByCode(String code);
    @Query("FROM Validation v where v.users.email = :email")
    Optional<Validation> findbyUser(String email);
    @Modifying
    @Query("DELETE FROM Validation v WHERE v.users.email = :email")
    void deleteByUserEmail(String email);

    @Modifying
    @Query("DELETE FROM Validation v WHERE v.users.idUser = :idUser")
    void deleteByUserId(int idUser);
}
