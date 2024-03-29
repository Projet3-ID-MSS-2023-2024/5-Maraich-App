package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
    public Optional<Users> findByEmail(String email);
    public List<Users> findByRank(Rank rank);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE u = :users")
    public boolean existsInDatabase(Users users);
    @Transactional
    void deleteByEmail(String email);
}