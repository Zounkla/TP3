package fr.fullstack.shopapp.dto;

import java.time.LocalDate;
import java.util.List;

import fr.fullstack.shopapp.model.OpeningHoursShop;
import fr.fullstack.shopapp.model.Product;

public class ShopDTO {

    private long id;
    private LocalDate createdAt;
    private boolean inVacations;
    private String name;
    private long nbProducts;
    private List<OpeningHoursShop> openingHours;
    private List<Product> products;
    private int numberOfCategories;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isInVacations() {
        return inVacations;
    }

    public void setInVacations(boolean inVacations) {
        this.inVacations = inVacations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNbProducts() {
        return nbProducts;
    }

    public void setNbProducts(long nbProducts) {
        this.nbProducts = nbProducts;
    }

    public List<OpeningHoursShop> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<OpeningHoursShop> openingHours) {
        this.openingHours = openingHours;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getNumberOfCategories() {
        return numberOfCategories;
    }

    public void setNumberOfCategories(int numberOfCategories) {
        this.numberOfCategories = numberOfCategories;
    }
}
