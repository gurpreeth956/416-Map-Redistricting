package com.giants.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Districtings")
public class Districting implements Comparable<Districting> {

    private int id;
    private int jobId;
    private int maxPopulationDifference;
    private double overallCompactness;
    private List<District> districts;

    public Districting() {

    }

    public Districting(int jobId, int maxPopulationDifference, double overallCompactness) {
        this.jobId = jobId;
        this.maxPopulationDifference = maxPopulationDifference;
        this.overallCompactness = overallCompactness;
        this.districts = new ArrayList<>();
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

    @Column(name = "jobId")
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Column(name = "maxPopulationDifference")
    public int getMaxPopulationDifference() {
        return maxPopulationDifference;
    }

    public void setMaxPopulationDifference(int maxPopulationDifference) {
        this.maxPopulationDifference = maxPopulationDifference;
    }

    @Column(name = "overallCompactness")
    public double getOverallCompactness() {
        return overallCompactness;
    }

    public void setOverallCompactness(double overallCompactness) {
        this.overallCompactness = overallCompactness;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "districtingId", referencedColumnName = "id")
    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        Collections.sort(districts);
        this.districts = districts;
    }

    public void sortBoxWhiskersData(List<List<Double>> boxWhiskersData) {
        for (int j = 0; j < this.districts.size(); j++) {
            District district = this.districts.get(j);
            district.setDistrictNumber(j+1);
            double districtVap = district.getPopAndVap().getTotalVap();
            double boxWhiskerPercent = (district.getTotalUserRequestedVap()/districtVap)*100;
            if (boxWhiskerPercent > 99) boxWhiskerPercent = 99;
            if (boxWhiskerPercent < 0) boxWhiskerPercent = 0;
            boxWhiskersData.get(j+1).add(boxWhiskerPercent);
        }
    }

    public int compareTo(Districting districting) {
        int thisGreaterVapsCounter = 0;
        int otherGreaterVapsCounter = 0;
        for (int i = 0; i < this.districts.size(); i++) {
            District thisDistrict = this.districts.get(i);
            District otherDistrict = districting.districts.get(i);
            if (thisDistrict.compareTo(otherDistrict) > 0) thisGreaterVapsCounter++;
            else if (otherDistrict.compareTo(thisDistrict) < 0) otherGreaterVapsCounter++;
        }
        return thisGreaterVapsCounter - otherGreaterVapsCounter;
    }

}
