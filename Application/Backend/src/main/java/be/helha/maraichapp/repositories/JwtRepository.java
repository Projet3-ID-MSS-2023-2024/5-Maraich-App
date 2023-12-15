package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt, Integer> {
    Optional<Jwt> findByValue(String value);

    Optional<Jwt> findByValueAndIsDisableAndIsExpired(String token, boolean isDisable, boolean isExpired);
    @Query("FROM Jwt j WHERE j.isExpired = :isExpired and j.isDisable = :isDisable and j.users.email = :email")
    Optional<Jwt> findByUsersValidToken(String email, boolean isDisable, boolean isExpired);
    @Query("FROM Jwt j WHERE j.users.email = :email")
    Stream<Jwt> findByEmail(String email);

    void deleteAllByIsExpiredAndIsDisable(boolean isExpired, boolean isDisable);

    boolean existsByValue(String tokenBearer);

    @Modifying
    @Query("DELETE FROM Jwt j WHERE j.users.idUser = :idUser")
    void deleteByUserId(int idUser);
}
