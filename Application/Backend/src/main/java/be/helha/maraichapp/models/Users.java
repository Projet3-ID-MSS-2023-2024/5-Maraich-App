package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String phoneNumber;
    @Embedded
    @Column(nullable = false)
    private Address address;
    @Column(nullable = false)
    private String email;


    public Users(String firstName, String surname, String phoneNumber, String roadNumber, String postCode, String city, String email) {
        this.firstName = firstName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.address =  new Address(roadNumber,postCode,city);
        this.email = email;
    }
}