package com.giants.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Districts")
public class District {

    private int id;
    private int stateId;
    private int districtNumber;
    private double compactness;
    private int numberOfCounties;
    private GeoJSON geoJson;
    private List<DistrictPrecinct> districtPrecincts;

    public District() {

    }

    public District(int stateId, int districtNumber, double compactness, int numberOfCounties) {
        this.stateId = stateId;
        this.districtNumber = districtNumber;
        this.compactness = compactness;
        this.numberOfCounties = numberOfCounties;
        this.geoJson = new GeoJSON();
        this.districtPrecincts = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "stateId")
    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    @Column(name = "districtNumber")
    public int getDistrictNumber() {
        return districtNumber;
    }

    public void setDistrictNumber(int districtNumber) {
        this.districtNumber = districtNumber;
    }

    @Column(name = "compactness")
    public double getCompactness() {
        return compactness;
    }

    public void setCompactness(double compactness) {
        this.compactness = compactness;
    }

    @Column(name = "numberOfCounties")
    public int getNumberOfCounties() {
        return numberOfCounties;
    }

    public void setNumberOfCounties(int numberOfCounties) {
        this.numberOfCounties = numberOfCounties;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "geoJsonId", referencedColumnName = "id")
    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "districtId", referencedColumnName = "id")
    public List<DistrictPrecinct> getDistrictPrecincts() {
        return districtPrecincts;
    }

    public void setDistrictPrecincts(List<DistrictPrecinct> districtPrecincts) {
        this.districtPrecincts = districtPrecincts;
    }

}
