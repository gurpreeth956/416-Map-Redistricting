package com.giants.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Districts")
public class District implements Comparable<District> {

    private int id;
    private int stateId;
    private int districtNumber;
    private double compactness;
    private int numberOfCounties;
    private int totalUserRequestedPop;
    private int totalUserRequestedVap;
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
        this.totalUserRequestedPop = 0;
        this.totalUserRequestedVap = 0;
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

    @Column(name = "totalUserRequestedPop")
    public int getTotalUserRequestedPop() {
        return totalUserRequestedPop;
    }

    public void setTotalUserRequestedPop(int totalUserRequestedPop) {
        this.totalUserRequestedPop = totalUserRequestedPop;
    }

    @Column(name = "totalUserRequestedVap")
    public int getTotalUserRequestedVap() {
        return totalUserRequestedVap;
    }

    public void setTotalUserRequestedVap(int totalUserRequestedVap) {
        this.totalUserRequestedVap = totalUserRequestedVap;
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

    public void addPopAndVap(int pop, int vap) {
        this.totalUserRequestedPop += pop;
        this.totalUserRequestedVap += vap;
    }

    public int compareTo(District district) {
        return this.getTotalUserRequestedVap() - district.getTotalUserRequestedVap();
    }

}
