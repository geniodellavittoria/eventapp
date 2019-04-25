package ch.mobpro.eventapp.dto;

import ch.mobpro.eventapp.model.Event;

import java.nio.DoubleBuffer;
import java.time.*;
import java.util.Date;

public class CreateEventForm {

    private String name;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    private int place;

    private double price;

    private String description;

    private String eventImage;

    private double longitude;

    private double latitude;

    private boolean privateEvent;

    public CreateEventForm() {
    }

    public CreateEventForm(String name, LocalTime startTime, LocalDate startDate, LocalTime endTime, LocalDate endDate, int place, double price,
                           String eventImage, String description, double longitude, double latitude, boolean privateEvent) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.place = place;
        this.price = price;
        this.description = description;
        this.eventImage = eventImage;
        this.longitude = longitude;
        this.latitude = latitude;
        this.privateEvent = privateEvent;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public double getPrice() {
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
}

