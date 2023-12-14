package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;

@Entity
@Table (name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue
    private int Id;

    @Column (nullable = false)
    private Calendar orderDate;

    @Column (nullable = false)
    private float totalPrice;

    @Column (nullable = false)
    private Calendar reedeemDate;
    @Column (nullable = false)
    private boolean orderIsReady;

    private Calendar readyDate;
    @Column (nullable = false)
    private boolean isArchived;

    @ManyToOne
    @JoinColumn(name="customerId")
    private Users customer;
    @ManyToOne
    @JoinColumn(name="shopId")
    private Shop shopSeller;
    @OneToMany(mappedBy = "orders")
    private List<OrderProduct> orderProducts;

    public Orders(Calendar orderDate, float totalPrice, Calendar reedeemDate, Users customer, Shop shopSeller) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.reedeemDate = reedeemDate;
        this.orderIsReady = false;
        this.isArchived = false;
        this.customer = customer;
        this.shopSeller = shopSeller;
    }
}
