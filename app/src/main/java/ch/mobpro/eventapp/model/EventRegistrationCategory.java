package ch.mobpro.eventapp.model;

public class EventRegistrationCategory {

    private String id;

    private String Category;

    public EventRegistrationCategory(String id, String category) {
        this.id = id;
        Category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
