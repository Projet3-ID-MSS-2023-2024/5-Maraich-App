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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRequest;


    @OneToOne(cascade = {CascadeType.MERGE})
    private Users user;

    @Column (nullable = false)
    private String requestBody;

    public Requests(Users user, String requestBody) {
        this.user = user;
        this.requestBody = requestBody;
    }
}
