package be.helha.maraichapp.models;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProduct;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double price;

    @ManyToOne
    private Category category;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Product() {

    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
