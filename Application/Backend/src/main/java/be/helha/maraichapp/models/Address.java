package be.helha.maraichapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(nullable = false)
    private String road;
    @Column(nullable = false)
    private String postCode;
    @Column(nullable = false)
    private String city;
    // The choice of String has been made for house numbers like 60A
    @Column(nullable = false)
    private String number;

}
