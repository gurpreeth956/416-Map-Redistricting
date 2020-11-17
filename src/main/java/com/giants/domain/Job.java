package com.giants.domain;

import com.giants.enums.Ethnicity;
import com.giants.enums.JobStatus;
import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Jobs")
@NamedQuery(name="Jobs.getJobs", query="SELECT j FROM Job j")
public class Job {
    private int id;
    private JobStatus status;
    private StateAbbreviation abbreviation;
    private int userCompactness;
    private double populationDifferenceLimit;
    private int numberOfMaps;
//    private List<Ethnicity> ethnicities;
    private int onSeaWulf;
//    private BoxWhiskers boxWhiskers;
    private int averageStateId;
    private int extremeStateId;

    public Job() {

    }

    public Job(StateAbbreviation abbreviation, int userCompactness, double populationDifferenceLimit, int numberOfMaps, List<Ethnicity> ethnicities) {
        this.abbreviation = abbreviation;
        this.userCompactness = userCompactness;
        this.populationDifferenceLimit = populationDifferenceLimit;
        this.numberOfMaps = numberOfMaps;
//        this.ethnicities = ethnicities;
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

    @Column(name = "jobStatus")
    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    @Column(name = "abbreviation")
    public StateAbbreviation getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(StateAbbreviation abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Column(name = "userCompactness")
    public int getUserCompactness() {
        return userCompactness;
    }

    public void setUserCompactness(int userCompactness) {
        this.userCompactness = userCompactness;
    }

    @Column(name = "populationDifferenceLimit")
    public double getPopulationDifferenceLimit() {
        return populationDifferenceLimit;
    }

    public void setPopulationDifferenceLimit(double populationDifferenceLimit) {
        this.populationDifferenceLimit = populationDifferenceLimit;
    }

    @Column(name = "numberOfMaps")
    public int getNumberOfMaps() {
        return numberOfMaps;
    }

    public void setNumberOfMaps(int numberOfMaps) {
        this.numberOfMaps = numberOfMaps;
    }

//    public List<Ethnicity> getEthnicities() {
//        return ethnicities;
//    }
//
//    public void setEthnicities(List<Ethnicity> ethnicities) {
//        this.ethnicities = ethnicities;
//    }

    @Column(name = "onSeaWulf")
    public int isOnSeaWulf() {
        return onSeaWulf;
    }

    public void setOnSeaWulf(int onSeaWulf) {
        this.onSeaWulf = onSeaWulf;
    }

//    public BoxWhiskers getBoxWhiskers() {
//        return boxWhiskers;
//    }
//
//    public void setBoxWhiskers(BoxWhiskers boxWhiskers) {
//        this.boxWhiskers = boxWhiskers;
//    }

    @Column(name = "averageStateId")
    public int getAverageState() {
        return averageStateId;
    }

    public void setAverageState(int averageStateId) {
        this.averageStateId = averageStateId;
    }

    @Column(name = "extremeStateId")
    public int getExtremeState() {
        return extremeStateId;
    }

    public void setExtremeState(int extremeStateId) {
        this.extremeStateId = extremeStateId;
    }

    public int executeSeaWulfJob() {
        return 1;
    }

    public void executeLocalJob() {

    }

    public String retrieveSeaWulfData() {
        return "";
    }

    public boolean countCounties(String filePath) {
        return true;
    }

    public boolean generateJsonFile(String filePath) {
        return true;
    }

    public boolean generateBoxWhiskers(String filePath) {
        return true;
    }

    public boolean generateAvgExtDistrictingPlan(String filePath) {
        return true;
    }
}
