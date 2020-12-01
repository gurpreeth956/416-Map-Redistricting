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
        this.districts = districts;
    }

    public void sortDistricts(List<List<Double>> boxWhiskersData, List<District> districts) {
        Collections.sort(districts);
        for (int j = 0; j < districts.size(); j++) {
            District district = districts.get(j);
            district.setDistrictNumber(j+1);
            double districtVap = district.getPopAndVap().getTotalVap();
            // Fix dividing by zero
            double boxWhiskerPercent = (district.getTotalUserRequestedVap()/districtVap)*100;
            if (boxWhiskerPercent > 99) boxWhiskerPercent = 99;
            if (boxWhiskerPercent < 0) boxWhiskerPercent = 0;
            boxWhiskersData.get(j+1).add(boxWhiskerPercent);
        }
        this.setDistricts(districts);
    }

    public int compareTo(Districting districting) {

        // Change this to use vap and for each district

        return (int)((this.getMaxPopulationDifference() / this.getOverallCompactness()) -
                (districting.getMaxPopulationDifference() / districting.getOverallCompactness()));
    }

}
