package be.helha.maraichapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderProductId implements Serializable {
        private int orderId;
        private int productId;
    }

    @EmbeddedId
    private OrderProductId id;
    @JsonIgnore
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne(cascade = { CascadeType.MERGE})
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
}
