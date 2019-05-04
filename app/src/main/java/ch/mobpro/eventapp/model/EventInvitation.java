package ch.mobpro.eventapp.model;

import java.io.Serializable;

public class EventInvitation implements Serializable {

    private String email;

    public EventInvitation() {
    }

    public EventInvitation(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
