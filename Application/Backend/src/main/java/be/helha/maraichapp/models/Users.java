package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Embedded
    @Column(nullable = false)
    private Address address;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private boolean isActif = false;
    /*@OneToMany(mappedBy = "customer")
    private List<Order> orders;*/
    @ManyToOne(cascade={CascadeType.MERGE})
    @JoinColumn(name = "rankId")
    private Rank rank;


    public Users(String firstName, String surname, String phoneNumber, String password, String number, String road, String postCode, String city, String email, Rank rank) {
        this.firstName = firstName;
        this.surname = surname;
        this.isActif = false;
        this.password = password;
        this.rank = rank;
        this.phoneNumber = phoneNumber;
        this.address = new Address(road,postCode,city, number);
        this.email = email;
    }

    public Users(String firstName, String surname, String phoneNumber, String password, String number, String road, String postCode, String city, String email) {
        this.firstName = firstName;
        this.surname = surname;
        this.isActif = false;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = new Address(road,postCode,city, number);
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ this.rank.getName()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isActif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isActif;
    }

    @Override
    public boolean isEnabled() {
        return this.isActif;
    }
}