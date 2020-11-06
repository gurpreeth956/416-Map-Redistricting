package com.giants.domain;

public class Precinct {
    private int id;
    private int districtId;
    private int countyId;
    private GeoJSON geoJson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }
}
