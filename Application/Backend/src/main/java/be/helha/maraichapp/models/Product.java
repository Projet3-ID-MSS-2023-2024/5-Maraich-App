package be.helha.maraichapp.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProduct;
    @Column(nullable = false)
    private String nameProduct;
    @Column(nullable = false)
    private double priceProduct;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String nameProduct, double priceProduct, Category category) {
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.category = category;
    }

    public Product() {

    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(priceProduct, product.priceProduct) == 0 && Objects.equals(nameProduct, product.nameProduct) && Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameProduct, priceProduct, category);
    }
}
