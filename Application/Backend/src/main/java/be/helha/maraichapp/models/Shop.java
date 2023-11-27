package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shops")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idShop;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String email;
    @Embedded
    @Column(nullable = false)
    private Address address;
    private String picture;
    @Column(nullable = false)
    private String description;
    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "ownerId")
    private Users owner;
    @Column(nullable = false)
    private boolean shopIsOkay;
    @Column(nullable = false)
    private boolean enable = false;

    public Shop(String name, String email ,String road, String number, String postCode, String city, String picture, String description, Users owner, boolean shopIsOkay, boolean enable) {
        this.name = name;
        this.email = email;
        this.address = new Address(road,postCode,city, number);
        this.picture = picture;
        this.description = description;
        this.owner = owner;
        this.shopIsOkay = shopIsOkay;
        this.enable = enable;
    }

    public Shop(String name, String email ,String road, String number, String postCode, String city, String picture, String description, Users owner) {
        this.name = name;
        this.email = email;
        this.address = new Address(road,postCode,city, number);
        this.picture = picture;
        this.description = description;
        this.owner = owner;
        this.shopIsOkay = false;
        this.enable = false;
    }
}
