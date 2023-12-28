package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReservation;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users users;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    private Instant expirateDate;
    private double reserveQuantity;

    public Reservation(Users users, Instant expirateDate, Product product, double reserveQuantity) {
        this.users = users;
        this.expirateDate = expirateDate;
        this.product = product;
        this.reserveQuantity = reserveQuantity;
    }
}
