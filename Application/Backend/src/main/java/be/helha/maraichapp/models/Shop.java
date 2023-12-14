package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @Column(nullable = false)
    private boolean shopIsOkay;
    @Column(nullable = false)
    private boolean enable = false;
    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "ownerId")
    private Users owner;
    @OneToMany (mappedBy = "shopSeller")
    private List<Order> orders;
    @OneToMany(mappedBy = "shop", cascade = {CascadeType.ALL})
    private List<Product> products;


    public Shop(String name, String email, Address address, String picture, String description, boolean shopIsOkay, boolean enable, Users owner, List<Orders> orders, List<Product> products) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.picture = picture;
        this.description = description;
        this.shopIsOkay = shopIsOkay;
        this.enable = enable;
        this.owner = owner;
        this.orders = orders;
        this.products = products;
    }

    public Shop(String name, String email , String road, String number, String postCode, String city, String picture, String description, Users owner) {
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