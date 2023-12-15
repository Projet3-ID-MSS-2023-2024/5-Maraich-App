package be.helha.maraichapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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
    private Instant orderDate;

    @Column (nullable = false)
    private float totalPrice;

    @Column (nullable = false)
    private Instant reedeemDate;
    @Column (nullable = false)
    private boolean orderIsReady;

    private Instant readyDate;
    @Column (nullable = false)
    private boolean isArchived;
    @ManyToOne
    @JoinColumn(name="customerId")
    private Users customer;
    @ManyToOne
    @JoinColumn(name="shopId")
    private Shop shopSeller;
    @OneToMany(mappedBy = "orders", cascade = {CascadeType.ALL})
    private List<OrderProduct> orderProducts;

    public Orders(Instant orderDate, float totalPrice, Instant reedeemDate, Users customer, Shop shopSeller) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.reedeemDate = reedeemDate;
        this.orderIsReady = false;
        this.isArchived = false;
        this.customer = customer;
        this.shopSeller = shopSeller;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "Id=" + Id +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", reedeemDate=" + reedeemDate +
                ", orderIsReady=" + orderIsReady +
                ", readyDate=" + readyDate +
                ", isArchived=" + isArchived +
                ", customer=" + customer +
                ", shopSeller=" + shopSeller +
                '}';
    }
}
