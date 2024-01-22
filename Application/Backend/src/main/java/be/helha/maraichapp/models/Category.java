package be.helha.maraichapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(String nomCategory) {
        this.nomCategory = nomCategory;
        this.products = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return idCategory == category.idCategory &&
                Objects.equals(nomCategory, category.nomCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategory, nomCategory);
    }

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", nomCategory='" + nomCategory + '\'' +
                ", products=" + (products != null ? products.size() : "null") +
                '}';
    }

}
