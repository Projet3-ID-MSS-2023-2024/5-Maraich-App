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

    public Shop(String name, String email ,String roadNumber, String postCode, String city, String picture, String description, Users owner) {
        this.name = name;
        this.email = email;
        this.address = new Address(roadNumber,postCode,city);
        this.picture = picture;
        this.description = description;
        this.owner = owner;
    }
}
