package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Requests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestsRepository extends JpaRepository<Requests, Integer> {
}
