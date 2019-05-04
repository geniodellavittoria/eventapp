package ch.mobpro.eventapp.model;

import java.io.Serializable;

public class EventRegistrationCategory implements Serializable {

    private String category;

    public EventRegistrationCategory() {
    }

    public EventRegistrationCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
