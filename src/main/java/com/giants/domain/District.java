package com.giants.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Districts")
public class District {

    private int id;
    private int districtNumber;
    private double compactness;
    private int numberOfCounties;
    private State state;
    private GeoJSON geoJson;
    private List<DistrictPrecinct> districtPrecincts;

    public District() {

    }

    public District(State state, int districtNumber, double compactness, int numberOfCounties) {
        this.state = state;
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

    @ManyToOne(optional = false)
    @JoinColumn(name="stateId", referencedColumnName = "id", insertable = false, updatable = false)
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="geoJsonId", referencedColumnName = "id", insertable = false, updatable = false)
    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<DistrictPrecinct> getDistrictPrecincts() {
        return districtPrecincts;
    }

    public void setDistrictPrecincts(List<DistrictPrecinct> districtPrecincts) {
        this.districtPrecincts = districtPrecincts;
    }
}
