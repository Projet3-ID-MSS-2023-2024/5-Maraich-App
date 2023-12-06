package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrderProduct;

    @ManyToOne
    @MapsId("orderId")
    private Orders orders;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    private int quantity;
}