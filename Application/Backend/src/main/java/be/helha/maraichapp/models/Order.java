package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity
@Table (name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private int Id;

    @Column (nullable = false)
    private Calendar orderDate;

    @Column (nullable = false)
    private float totalPrice;

    @Column (nullable = false)
    private Calendar reedeemDate;

    private boolean orderIsReady;

    private Calendar readyDate;

    private boolean isArchived;

    @Column (nullable = false)
    @ManyToOne
    private Users customer;

    @Column (nullable = false)
    @ManyToOne
    private Shop shop;

    /*@ManyToOne
    private Product product;*/

    public Order(Calendar orderDate, float totalPrice, Calendar reedeemDate) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.reedeemDate = reedeemDate;
    }
}
