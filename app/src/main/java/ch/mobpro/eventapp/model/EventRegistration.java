package ch.mobpro.eventapp.model;

import java.io.Serializable;

public class EventRegistration implements Serializable {

    private String id;

    private EventRegistrationCategory eventRegistrationCategory;

    //private Instant timestamp;

    private User user;

    private Double paidPrice;

    public EventRegistration(String id, EventRegistrationCategory eventRegistrationCategory, User user, Double paidPrice) {
        this.id = id;
        this.eventRegistrationCategory = eventRegistrationCategory;
        this.user = user;
        this.paidPrice = paidPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventRegistrationCategory getEventRegistrationCategory() {
        return eventRegistrationCategory;
    }

    public void setEventRegistrationCategory(EventRegistrationCategory eventRegistrationCategory) {
        this.eventRegistrationCategory = eventRegistrationCategory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(Double paidPrice) {
        this.paidPrice = paidPrice;
    }
}
