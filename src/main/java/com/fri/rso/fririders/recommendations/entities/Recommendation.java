package com.fri.rso.fririders.recommendations.entities;

import java.util.List;

public class Recommendation {

    private int id;
    private String userId;
    private List<Accommodation> accommodations;

    public Recommendation(int id, String userId, List<Accommodation> accommodations) {
        this.id = id;
        this.userId = userId;
        this.accommodations = accommodations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }
}
