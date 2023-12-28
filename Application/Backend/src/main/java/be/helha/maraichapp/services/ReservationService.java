package be.helha.maraichapp.services;

import be.helha.maraichapp.dto.ReservationDTO;
import be.helha.maraichapp.models.Reservation;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ReservationRepository;
import be.helha.maraichapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ReservationRepository reservationRepository;

    /**
     * Add a reservation to temporarily reserve a product.
     *
     * @param reservationDTO The ReservationDTO containing necessary information for the reservation.
     * @return The created reservation.
     * @throws RuntimeException If the user or product is not found, or if there is not enough quantity available.
     */
    public Reservation addReservation(ReservationDTO reservationDTO) {
        if(reservationDTO.reserveQuantity() < 1){
            throw new RuntimeException("Reserve something !");
        }
        // Retrieve user from the database
        Users users = userRepository.findById(reservationDTO.idUser())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        // Retrieve product from the database
        Product product = productRepository.findById(reservationDTO.idProduct())
                .orElseThrow(() -> new RuntimeException("Product not found!"));
        // Create a new reservation with the provided data
        Reservation finalReservation = new Reservation(users, Instant.now().plusSeconds(5 * 60), product, reservationDTO.reserveQuantity());
        // Retrieve the total reserved quantity for the specific product
        double totalReservedProduct = reservationRepository.getTotalReservedQuantityByProductId(product.getId()).orElse(0.0);
        // Determine available quantity based on product type (unity or weight)
        double availableQuantity = product.isUnity() ? product.getQuantity() : product.getWeight();
        // Check if there is enough quantity available for reservation
        if (availableQuantity - totalReservedProduct < 0) {
            throw new RuntimeException("Not enough quantity available!");
        } else {
            // Check if a reservation already exists for the same user and product
            Optional<Reservation> reservationOptional = reservationRepository.findByProductIdAndUserId(product.getId(), users.getIdUser());
            if (reservationOptional.isPresent()) {
                // If a reservation exists, update the reserved quantity
                Reservation reservationExists = reservationOptional.get();
                reservationExists.setReserveQuantity(reservationExists.getReserveQuantity() + reservationDTO.reserveQuantity());
                finalReservation = reservationRepository.save(reservationExists);
            } else {
                // If no existing reservation, save the new reservation
                finalReservation = reservationRepository.save(finalReservation);
            }
        }
        users.getReservations().add(finalReservation);
        userRepository.save(users);
        product.getReservations().add(finalReservation);
        productRepository.save(product);
        return finalReservation;
    }

//    @Transactional
    public List<Reservation> getAllReservations() {
//        removeExpirateReservations();
        return this.reservationRepository.findAll();
    }

//    @Transactional
//    @Scheduled(cron = "0 0/5 * * * *")
//    public void scheduledRemoveExpirateReservations() {
//        removeExpirateReservations();
//    }
//
//    @Transactional
//    public void removeExpirateReservations() {
//        //Get actual times minus 5 minutes
//        Instant expiredTime = Instant.now().minusSeconds(5 * 60);
//        log.info("Delete expirate reservation at {}", Instant.now());
//        // Delete expired reservations directly from the database
//        this.reservationRepository.deleteExpiredReservations(expiredTime);
//    }

    public List<Reservation> getShoppingCart(int idUser) {
        return reservationRepository.findByUserId(idUser);
    }
}

