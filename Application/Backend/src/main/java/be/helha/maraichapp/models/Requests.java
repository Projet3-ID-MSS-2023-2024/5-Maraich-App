package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requests {

    @Id
    @GeneratedValue
    private int Id;

    @ManyToOne
    @JoinColumn(name="shopId")
    private Shop shopSeller;

    @Column (nullable = false)
    private String requestBody;

    public Requests(Shop shopSeller, String requestBody) {
        this.shopSeller = shopSeller;
        this.requestBody = requestBody;
    }
}
