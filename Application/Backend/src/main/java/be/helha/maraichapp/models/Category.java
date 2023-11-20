package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategory;
    @Column(nullable = false)
    private String nomCategory;
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(String nomCategory) {
        this.nomCategory = nomCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNomCategory() {
        return nomCategory;
    }

    public void setNomCategory(String nomCategory) {
        this.nomCategory = nomCategory;
    }
}
