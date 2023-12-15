package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jwt")
@Entity
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String value;
    private boolean isDisable;
    private boolean isExpired;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "userId")
    private Users users;

    public Jwt(boolean isDisable, boolean isExpired, Users users, String value){
        this.isDisable = isDisable;
        this.isExpired = isExpired;
        this.users = users;
        this.value = value;
    }
}
