package com.giants.domain;

public class District {

    private int id;
    private int stateId;
    private float compactness;
    private int numberOfCounties;
    private GeoJSON geoJson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public float getCompactness() {
        return compactness;
    }

    public void setCompactness(float compactness) {
        this.compactness = compactness;
    }

    public int getNumberOfCounties() {
        return numberOfCounties;
    }

    public void setNumberOfCounties(int numberOfCounties) {
        this.numberOfCounties = numberOfCounties;
    }

    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }

}
