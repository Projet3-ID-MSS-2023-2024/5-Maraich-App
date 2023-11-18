package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
    public Optional<Users> findByEmail(String email);
    public List<Users> findByRank(Rank rank);
}
