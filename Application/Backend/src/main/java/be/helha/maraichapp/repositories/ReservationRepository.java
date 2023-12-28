package be.helha.maraichapp.repositories;

import be.helha.maraichapp.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    // Retrieve the total reserved quantity for a specific product and user
    @Query("SELECT SUM(r.reserveQuantity) FROM Reservation r WHERE r.product.id = :idProduct")
    Optional<Double> getTotalReservedQuantityByProductId(int idProduct);

    // Find a reservation by product ID and user ID
    @Query("SELECT r FROM Reservation r WHERE r.product.id = :idProduct AND r.users.idUser = :idUser")
    Optional<Reservation> findByProductIdAndUserId(int idProduct, int idUser);

    @Query("SELECT r FROM Reservation r WHERE r.users.idUser = :idUser")
    List<Reservation> findByUserId(@Param("idUser") int idUser);

    @Modifying
    @Query("DELETE FROM Reservation r WHERE r.expirateDate IS NOT NULL AND r.expirateDate < :expiredTime")
    void deleteExpiredReservations(@Param("expiredTime") Instant expiredTime);
}
