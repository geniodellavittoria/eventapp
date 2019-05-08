package ch.mobpro.eventapp.dto;

import ch.mobpro.eventapp.model.EventRegistrationCategory;

public class EventRegistrationForm {

    private EventRegistrationCategory eventRegistrationCategory;

    private String username;

    private Double paidPrice;

    public EventRegistrationForm() {
    }

    public EventRegistrationForm(EventRegistrationCategory eventRegistrationCategory, String username, Double paidPrice) {
        this.eventRegistrationCategory = eventRegistrationCategory;
        this.username = username;
        this.paidPrice = paidPrice;
    }

    public EventRegistrationCategory getEventRegistrationCategory() {
        return eventRegistrationCategory;
    }

    public void setEventRegistrationCategory(EventRegistrationCategory eventRegistrationCategory) {
        this.eventRegistrationCategory = eventRegistrationCategory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(Double paidPrice) {
        this.paidPrice = paidPrice;
    }
}
