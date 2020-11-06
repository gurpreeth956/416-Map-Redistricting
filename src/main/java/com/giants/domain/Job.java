package com.giants.domain;

import com.giants.enums.Ethnicity;
import com.giants.enums.JobStatus;
import com.giants.enums.StateAbbreviation;

import java.util.List;

public class Job {
    private int id;
    private JobStatus status;
    private StateAbbreviation abbreviation;
    private int userCompactness;
    private int populationDifferenceLimit;
    private int numberOfMaps;
    private int numberOfDistricts;
    private List<Ethnicity> ethnicities;
    private boolean onSeaWulf;
    private BoxWhiskers boxWhiskers;
    private int averageStateId;
    private int extremeStateId;

    public Job(StateAbbreviation abbreviation, int userCompactness, int populationDifferenceLimit, int numberOfMaps, int numberOfDistricts, List<Ethnicity> ethnicities) {
        this.abbreviation = abbreviation;
        this.userCompactness = userCompactness;
        this.populationDifferenceLimit = populationDifferenceLimit;
        this.numberOfMaps = numberOfMaps;
        this.numberOfDistricts = numberOfDistricts;
        this.ethnicities = ethnicities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public StateAbbreviation getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(StateAbbreviation abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getUserCompactness() {
        return userCompactness;
    }

    public void setUserCompactness(int userCompactness) {
        this.userCompactness = userCompactness;
    }

    public int getPopulationDifferenceLimit() {
        return populationDifferenceLimit;
    }

    public void setPopulationDifferenceLimit(int populationDifferenceLimit) {
        this.populationDifferenceLimit = populationDifferenceLimit;
    }

    public int getNumberOfMaps() {
        return numberOfMaps;
    }

    public void setNumberOfMaps(int numberOfMaps) {
        this.numberOfMaps = numberOfMaps;
    }

    public int getNumberOfDistricts() {
        return numberOfDistricts;
    }

    public void setNumberOfDistricts(int numberOfDistricts) {
        this.numberOfDistricts = numberOfDistricts;
    }

    public List<Ethnicity> getEthnicities() {
        return ethnicities;
    }

    public void setEthnicities(List<Ethnicity> ethnicities) {
        this.ethnicities = ethnicities;
    }

    public boolean isOnSeaWulf() {
        return onSeaWulf;
    }

    public void setOnSeaWulf(boolean onSeaWulf) {
        this.onSeaWulf = onSeaWulf;
    }

    public BoxWhiskers getBoxWhiskers() {
        return boxWhiskers;
    }

    public void setBoxWhiskers(BoxWhiskers boxWhiskers) {
        this.boxWhiskers = boxWhiskers;
    }

    public int getAverageState() {
        return averageStateId;
    }

    public void setAverageState(int averageStateId) {
        this.averageStateId = averageStateId;
    }

    public int getExtremeState() {
        return extremeStateId;
    }

    public void setExtremeState(int extremeStateId) {
        this.extremeStateId = extremeStateId;
    }

    public void executeSeaWulfJob() {

    }

    public void executeLocalJob() {

    }

    public String getSeaWulfData() {
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
