package be.helha.maraichapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"authorities", "credentialsNonExpired", "accountNonLocked", "accountNonExpired","username","enabled"})

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
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.REMOVE})
    private List<Orders> orders;
    @ManyToOne(cascade={CascadeType.MERGE})
    @JoinColumn(name = "rankId")
    private Rank rank;
    @JsonIgnore
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="requestId")
    private Requests requests;
    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = {CascadeType.ALL})
    private List<Jwt> jwts;
    @JsonIgnore
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="validationId")
    private Validation validation;
    @JsonIgnore
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "shopId")
    private Shop shop;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return idUser == users.idUser && isActif == users.isActif && Objects.equals(firstName, users.firstName) && Objects.equals(surname, users.surname) && Objects.equals(phoneNumber, users.phoneNumber) && Objects.equals(password, users.password) && Objects.equals(address, users.address) && Objects.equals(email, users.email) && Objects.equals(rank, users.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, firstName, surname, phoneNumber, password, address, email, isActif, rank);
    }

    public Users(String firstName, String surname, String phoneNumber, String password, String number, String road, String postCode, String city, String email, Rank rank, List<Orders> orders) {
        this.firstName = firstName;
        this.surname = surname;
        this.isActif = false;
        this.password = password;
        this.rank = rank;
        this.phoneNumber = phoneNumber;
        this.address = new Address(road,postCode,city, number);
        this.email = email;
        this.orders = orders;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}