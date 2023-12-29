package be.helha.maraichapp.services;

import be.helha.maraichapp.dto.ReservationDTO;
import be.helha.maraichapp.models.Reservation;
import be.helha.maraichapp.models.Product;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.ProductRepository;
import be.helha.maraichapp.repositories.ReservationRepository;
import be.helha.maraichapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
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

    public List<Reservation> getShoppingCart(int idUser) {
        return reservationRepository.findByUserId(idUser);
    }

    public Reservation updateReservationQuantity(int idReservation, double newQuantity){
        Reservation reservation = reservationRepository.findById(idReservation).orElseThrow(()-> new RuntimeException("Reservation not found !"));
        // Verify if the product isUnity and accept decimal or not
        if (!reservation.getProduct().isUnity()) {
            reservation.setReserveQuantity(newQuantity);
        } else {
            // If the product isUnity, verify if the new Quantity have decimal
            if (newQuantity % 1 == 0) {
                reservation.setReserveQuantity(newQuantity);
            } else {
                throw new RuntimeException("Decimal values are not allowed for unity products.");
            }
        }
        return reservationRepository.save(reservation);
    }

    public void deleteReservationById(int id) {
            if (reservationRepository.existsById(id)) {
                reservationRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException("Reservation not found with ID: " + id);
            }
    }

    @Transactional
    public List<Reservation> getAllReservations() {
        removeExpirateReservations();
        return this.reservationRepository.findAll();
    }
    @Transactional
    public void deleteShoppingCartByUserId(int idUser) {
        reservationRepository.deleteAllByUserId(idUser);
    }

    /**
     * If the user have any shopping cart or the shopping cart feat with the idShop return 1
     * If the user have a shopping cart but it don't feat with idShop return 0
     * @param idUser
     * @param idShop
     * @return
     */
    public Map<String, String> existShoppingCart(int idUser, int idShop) {
        Map<String, String> returnMap = new HashMap<>();
        List<Reservation> reservationList = reservationRepository.findByUserId(idUser);
        if(reservationList.isEmpty() || reservationList.get(0).getProduct().getShop().getIdShop() == idShop)
        {
            returnMap.put("message", "1");
        } else {
            returnMap.put("message", "0");
        }
        return returnMap;
    }

    @Transactional
    @Scheduled(cron = "0 0/5 * * * *")
    public void scheduledRemoveExpirateReservations() {
        removeExpirateReservations();
    }

    @Transactional
    public void removeExpirateReservations() {
        //Get actual times minus 5 minutes
        Instant expiredTime = Instant.now().minusSeconds(5 * 60);
        log.info("Delete expirate reservation at {}", Instant.now());
        // Delete expired reservations directly from the database
        this.reservationRepository.deleteExpiredReservations(expiredTime);
    }


}

