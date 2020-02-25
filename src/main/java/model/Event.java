package model;

import java.util.Objects;

/**
 * Model Event class contains all of the information an event has
 */
public class Event {
    private String eventID = null;
    private String associatedUsername = null; //User (Username) to which this person belongs
    private String personID = null;
    private float latitude = 0;
    private float longitude = 0;
    private String country = null;
    private String city = null;
    private String eventType = null; //Type of event (birth, baptism, christening, marriage, death, etc.)
    private int year = 0;

    public Event() {
    }

    public Event(String eventID, String associatedUsername, String personID, float latitude,
                 float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Float.compare(event.latitude, latitude) == 0 &&
                Float.compare(event.longitude, longitude) == 0 &&
                year == event.year &&
                eventID.equals(event.eventID) &&
                associatedUsername.equals(event.associatedUsername) &&
                personID.equals(event.personID) &&
                country.equals(event.country) &&
                city.equals(event.city) &&
                eventType.equals(event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }
}
