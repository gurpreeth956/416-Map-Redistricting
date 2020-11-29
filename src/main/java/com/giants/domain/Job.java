package com.giants.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.giants.Script;
import com.giants.enums.JobStatus;
import com.giants.enums.RaceEthnicity;
import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private int seaWulfId;
    private Integer averageStateId;
    private Integer extremeStateId;
//    private List<RaceEthnicity> raceEthnicities;
    private List<State> states;
    private List<Ethnicity> ethnicities;
    private List<BoxWhisker> boxWhiskers;

    public Job() {

    }

    public Job(StateAbbreviation abbreviation, List<RaceEthnicity> raceEthnicities, int userCompactness,
               double populationDifferenceLimit, int numberOfMaps) {
        this.abbreviation = abbreviation;
        this.userCompactness = userCompactness;
        this.populationDifferenceLimit = populationDifferenceLimit;
        this.jobStatus = JobStatus.WAITING;
        this.seaWulfId = -1;
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

    @Column(name = "seaWulfId")
    public int getSeaWulfId() {
        return seaWulfId;
    }

    public void setSeaWulfId(int seaWulfId) {
        this.seaWulfId = seaWulfId;
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

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

//    public List<RaceEthnicity> getRaceEthnicities() {
//        return raceEthnicities;
//    }
//
//    public void setRaceEthnicities(List<RaceEthnicity> raceEthnicities) {
//        this.raceEthnicities = raceEthnicities;
//    }

    public boolean executeSeaWulfJob() {
        Script script = new Script();

        // DECIDE HOW TO SPLIT UP SEAWULF JOB

        String command = "ssh gurpreetsing@login.seawulf.stonybrook.edu 'source /etc/profile.d/modules.sh; " +
                "module load slurm; module load anaconda/2; module load mvapich2/gcc/64/2.2rc1; cd ~/Jobs; " +
                "sbatch ~/Jobs/districting.slurm " + this.abbreviation + " " + this.userCompactness + " " +
                this.populationDifferenceLimit + " " + this.numberOfMaps + "'";
        String processOutput = script.createScript(command);
        if (!processOutput.contains("Submitted batch job")) return false;
        this.setSeaWulfId(Integer.parseInt(processOutput.split("\\s+")[3]));
        return true;
    }

    public boolean executeLocalJob() {
        Script script = new Script();
        try {
            String filePath = "./src/main/resources/Algorithm/Results/" + this.id + ".json";
            System.out.println("STARTING ALGORITHM...");
            File outputFile = new File(filePath);
            outputFile.createNewFile();
            ProcessBuilder pb = new ProcessBuilder("python3", "./src/main/resources/Algorithm/SeedDistricting.py",
                    this.id + "", this.abbreviation + "", this.userCompactness + "", this.populationDifferenceLimit + "",
                    this.numberOfMaps + "");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            String test = script.getProcessOutput(process);
            System.out.println(test);
            this.setJobStatus(JobStatus.PROCESSING);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public String retrieveSeaWulfData() {
        return "";
    }

    public boolean countCounties(String filePath) {
        return true;
    }

    public List<BoxWhisker> generateBoxWhiskers(List<List<Integer>> boxWhiskersData, double totalSpecifiedStateVap) {
        List<BoxWhisker> boxWhiskers = this.getBoxWhiskers();
        for (int i = 1; i < boxWhiskersData.size(); i++) {
            List<Integer> boxWhiskerData = boxWhiskersData.get(i);
            BoxWhisker boxWhisker = new BoxWhisker(this.getId(), i);
            Collections.sort(boxWhiskerData);
            double min = (boxWhiskerData.get(0)/totalSpecifiedStateVap)*100;
            double quart1 = (boxWhiskerData.get((int)(boxWhiskerData.size()/4))/totalSpecifiedStateVap)*100;
            double median = (boxWhiskerData.get((int)(boxWhiskerData.size()/2))/totalSpecifiedStateVap)*100;
            double quart3 = (boxWhiskerData.get((int)(boxWhiskerData.size()/2+boxWhiskerData.size()/4))/totalSpecifiedStateVap)*100;
            double max = (boxWhiskerData.get(boxWhiskerData.size()-1)/totalSpecifiedStateVap)*100;
            boxWhisker.setMinimum(min);
            boxWhisker.setQuartile1(quart1);
            boxWhisker.setMedian(median);
            boxWhisker.setQuartile3(quart3);
            boxWhisker.setMaximum(max);
            boxWhiskers.add(boxWhisker);
        }
        this.setBoxWhiskers(boxWhiskers);
        return boxWhiskers;
    }

    public void calculateAvgExtDistrictingPlan(List<State> states) {
        Collections.sort(states);
        int averageStateId = (states.get((int)(states.size()/2))).getId();
        int extremeStateId = (states.get((int)(states.size()-1))).getId();
        this.setStates(states);
        this.setAverageStateId(averageStateId);
        this.setExtremeStateId(extremeStateId);
    }
}
