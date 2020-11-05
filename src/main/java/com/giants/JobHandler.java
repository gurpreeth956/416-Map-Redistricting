package com.giants;

import com.giants.domain.Job;
import com.giants.enums.Ethnicity;
import com.giants.enums.StateAbbreviation;

import java.util.List;

public class JobHandler {
    // Need to add dependency for Hibernate
    // private EntityManagerFactory entityManagerFactory;

    public int createJob(StateAbbreviation stateName, int userCompactness, int populationDifferenceLimit, List<Ethnicity> ethnicities, int numberOfMaps, int numberOfDistricts) {
        return 0;
    }

    private void executeSeaWulfJob(Job job) {

    }

    private void executeLocalJob(Job job) {

    }

    public boolean cancelJobData(int jobId) {
        return true;
    }

    public boolean deleteJobData(int jobId) {
        return true;
    }

    public Job loadJobData(int jobId) {
        return new Job();
    }

}
