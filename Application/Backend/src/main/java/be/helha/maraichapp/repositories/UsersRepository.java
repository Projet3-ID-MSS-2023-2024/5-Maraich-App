package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {
}
