package ch.mobpro.eventapp.model;

public class EventInvitation {

    private String id;

    private String eventId;

    private String email;

    public EventInvitation(String id, String eventId, String email) {
        this.id = id;
        this.eventId = eventId;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
