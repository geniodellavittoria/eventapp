package ch.mobpro.eventapp.model;

public class EventInvitation {

    private String email;

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
