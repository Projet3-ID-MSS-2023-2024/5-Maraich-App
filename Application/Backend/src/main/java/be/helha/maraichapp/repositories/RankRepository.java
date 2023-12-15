package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.RankEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank, Integer> {
    Optional<Rank> findByName(RankEnum name);
}
