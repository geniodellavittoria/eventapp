package ch.mobpro.eventapp.model;


import java.io.Serializable;

public class EventCategory implements Serializable {

    private String category;

    public EventCategory() {
    }

    public EventCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
