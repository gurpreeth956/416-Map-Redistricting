package com.giants;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.giants.domain.Job;
import com.giants.enums.Ethnicity;
import com.giants.enums.StateAbbreviation;
import com.giants.enums.JobStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RequestController {
    private JobHandler jobHandler;
    private Map<Integer, Job> jobs;
    private List<Integer> jobsToCheckStatus;
    // Need to import Json type, not sure yet
//    private Json pennsylvaniaGeoJson;
//    private Json louisianaGeoJson;
//    private Json californiaGeoJson;

    public boolean initalSetup() {
        List<Job> jobList = jobHandler.loadAllJobData();
        for(Job job : jobList){
            jobs.put(job.getId(), job);
            if(job.getStatus() != JobStatus.COMPLETED) {
                jobsToCheckStatus.add(job.getId());
            }
        }

        // Need a way to get current Map GEOJSON still ask baloo
        return true;
    }

    // Need to import Json type, not sure yet

//    public Json getState() {
//        return "";
//    }
//
//    public Json getDistricting() {
//        return "";
//    }

    public int submitJob(StateAbbreviation stateName, int userCompactness, int populationDifferenceLimit, List<Ethnicity> ethnicities, int numberOfMaps, int numberOfDistricts) {
        Job job = jobHandler.createJob(stateName, userCompactness, populationDifferenceLimit, ethnicities, numberOfMaps, numberOfDistricts);
        jobs.put(job.getId(), job);
        return job.getId();
    }

    public boolean cancelJob(int jobId) {
        boolean isCancelled = jobHandler.cancelJobData(jobId);
        if(isCancelled) {
            Job job = jobs.get(jobId);
            job.setStatus(JobStatus.CANCELLED);
        }
        // Based on cancellation, a modal should popup with details
        return isCancelled;
    }

    public List<Job> getHistory() {
        List<Job> jobList = new ArrayList<Job>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    public boolean deleteJob(int jobId) {
        boolean isDeleted = jobHandler.deleteJobData(jobId);
        if(isDeleted) {
            jobs.remove(jobId);
        }
        return isDeleted;
    }

    public void checkJobStatus() {
        List<Job> jobsToCheck = new ArrayList<Job>();
        for(int id : jobsToCheckStatus) {
            jobsToCheck.add(jobs.get(id));
        }
        List<Job> changedJobs = jobHandler.getJobStatusSeaWulf(jobsToCheck);
        for(Job job : changedJobs) {
            jobs.replace(job.getId(), job);
        }
    }

    // Saw this method in Class Diagram not sure what it does though
    public void pingSeaWulf() {

    }

    // Testing/working post method
    @PostMapping("/hello")
    public void hello() {
        System.out.println("hello");
    }

    // Testing/working post method
    @GetMapping("/bye")
    public void bye() {
        System.out.println("bye");
    }
}
