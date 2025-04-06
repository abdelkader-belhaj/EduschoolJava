package tn.eduskool.entities;

public class Pack {
    private int id;
    private String name;
    private float price;
    private int duration; // durée en jours
    private String description;

    // Constructeurs

    public Pack() {
    }

    public Pack(String name, float price, int duration, String description) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.description = description;
    }

    public Pack(int id, String name, float price, int duration, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.description = description;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Affichage

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                '}';
    }
}
