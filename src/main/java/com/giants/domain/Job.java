package com.giants.domain;

import com.giants.enums.JobStatus;
import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Jobs")
@NamedQuery(name = "Jobs.getJobs", query = "SELECT j FROM Job j")
public class Job {

    private int id;
    private JobStatus jobStatus;
    private StateAbbreviation abbreviation;
    private int userCompactness;
    private double populationDifferenceLimit;
    private int numberOfMaps;
    private int onSeaWulf;
    private Integer averageStateId;
    private Integer extremeStateId;
    private List<State> states;
    private List<Ethnicity> ethnicities;
    private List<BoxWhisker> boxWhiskers;

    public Job() {

    }

    public Job(StateAbbreviation abbreviation, int userCompactness, double populationDifferenceLimit, int numberOfMaps) {
        this.abbreviation = abbreviation;
        this.userCompactness = userCompactness;
        this.populationDifferenceLimit = populationDifferenceLimit;
        this.jobStatus = JobStatus.WAITING;
        this.onSeaWulf = -1;
        this.numberOfMaps = numberOfMaps;
        this.states = new ArrayList<>();
        this.ethnicities = new ArrayList<>();
        this.boxWhiskers = new ArrayList<>();
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
    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
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

    @Column(name = "onSeaWulf")
    public int getOnSeaWulf() {
        return onSeaWulf;
    }

    public void setOnSeaWulf(int onSeaWulf) {
        this.onSeaWulf = onSeaWulf;
    }

    @Column(name = "averageStateId")
    public Integer getAverageStateId() {
        return averageStateId;
    }

    public void setAverageStateId(Integer averageStateId) {
        this.averageStateId = averageStateId;
    }

    @Column(name = "extremeStateId")
    public Integer getExtremeStateId() {
        return extremeStateId;
    }

    public void setExtremeStateId(Integer extremeStateId) {
        this.extremeStateId = extremeStateId;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    public List<Ethnicity> getEthnicities() {
        return ethnicities;
    }

    public void setEthnicities(List<Ethnicity> ethnicities) {
        this.ethnicities = ethnicities;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    public List<BoxWhisker> getBoxWhiskers() {
        return boxWhiskers;
    }

    public void setBoxWhiskers(List<BoxWhisker> boxWhiskers) {
        this.boxWhiskers = boxWhiskers;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
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
