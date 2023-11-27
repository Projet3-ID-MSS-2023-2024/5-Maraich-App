package be.helha.maraichapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String picturePath;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double weight;
    @Column(nullable = false)
    private boolean isUnity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, double price, String description, String picturePath, int quantity, double weight, boolean isUnity, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.picturePath = picturePath;
        this.quantity = quantity;
        this.weight = weight;
        this.isUnity = isUnity;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && quantity == product.quantity && Double.compare(weight, product.weight) == 0 && isUnity == product.isUnity && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(picturePath, product.picturePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, description, picturePath, quantity, weight, isUnity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", quantity=" + quantity +
                ", weight=" + weight +
                ", isUnity=" + isUnity +
                ", category=" + (category != null ? category.getIdCategory() : "null") +
                '}';
    }
}
