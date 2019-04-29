package ch.mobpro.eventapp.model;


import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String id;

    private User organizer;

    // will be provided by frontend for find full user and store in organizer;
    private String username;

    private String name;

    private Instant creationTime;

    private Instant startTime;

    private Instant endTime;

    private Integer place;

    private Integer usedPlace;

    private Double price;

    private String description;

    private String eventImage;

    private double longitude;

    private double latitude;

    private boolean privateEvent;

    private List<EventInvitation> eventInvitations = new ArrayList<>();

    private List<EventRegistration> eventRegistrations = new ArrayList<>();

    private List<EventRegistration> guestList = new ArrayList<>();

    private List<EventCategory> categories = new ArrayList<>();

    public Event() {
    }

    public Event(String id, User organizer, String name, Instant creationTime, Instant startTime, Instant endTime,
                 Integer place, Double price, String description, String eventImage, double longitude, double latitude,
                 boolean privateEvent, List<EventInvitation> eventInvitations, List<EventRegistration> eventRegistrations,
                 List<EventRegistration> guestList, List<EventCategory> categories) {
        this.id = id;
        this.organizer = organizer;
        this.name = name;
        this.creationTime = creationTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.price = price;
        this.description = description;
        this.eventImage = eventImage;
        this.longitude = longitude;
        this.latitude = latitude;
        this.privateEvent = privateEvent;
        this.eventInvitations = eventInvitations;
        this.eventRegistrations = eventRegistrations;
        this.guestList = guestList;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public List<EventInvitation> getEventInvitations() {
        return eventInvitations;
    }

    public void setEventInvitations(List<EventInvitation> eventInvitations) {
        this.eventInvitations = eventInvitations;
    }

    public List<EventRegistration> getEventRegistrations() {
        return eventRegistrations;
    }

    public void setEventRegistrations(List<EventRegistration> eventRegistrations) {
        this.eventRegistrations = eventRegistrations;
    }

    public List<EventRegistration> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<EventRegistration> guestList) {
        this.guestList = guestList;
    }

    public List<EventCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EventCategory> categories) {
        this.categories = categories;
    }

    public Integer getUsedPlace() {
        return usedPlace;
    }

    public void setUsedPlace(Integer usedPlace) {
        this.usedPlace = usedPlace;
    }
}
