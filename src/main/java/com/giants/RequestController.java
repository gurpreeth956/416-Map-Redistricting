package com.giants;

import com.giants.domain.State;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.giants.domain.Job;
import com.giants.enums.RaceEthnicity;
import com.giants.enums.StateAbbreviation;
import com.giants.enums.JobStatus;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@EnableScheduling
//@Component
public class RequestController {

    private JobHandler jobHandler;
    private Map<Integer, Job> jobs;
    private List<Integer> jobsToCheckStatus;
    private String pennsylvaniaPrecinctData;
    private String louisianaPrecinctData;
    private String californiaPrecinctData;


    // Everything we need to do when server starts up goes here
    // https://www.baeldung.com/spring-postconstruct-predestroy
    @PostConstruct
    public void initialSetup() {
        System.out.println("please");
        jobHandler = new JobHandler();
        jobs = new HashMap<Integer, Job>();
        List<Job> jobList = jobHandler.loadAllJobData();
        jobsToCheckStatus = new ArrayList<Integer>();
        for(Job job : jobList){
            jobs.put(job.getId(), job);
            if(job.getJobStatus() != JobStatus.COMPLETED) {
                jobsToCheckStatus.add(job.getId());
            }
        }

        // Need a to verify format for Precinct GeoJSON
        pennsylvaniaPrecinctData = jobHandler.loadPrecinctData(StateAbbreviation.PA);
        louisianaPrecinctData = jobHandler.loadPrecinctData(StateAbbreviation.LA);
        californiaPrecinctData = jobHandler.loadPrecinctData(StateAbbreviation.CA);
    }

    // This will return the specified state's precinct data
    @RequestMapping(value = "/getState", method = RequestMethod.POST)
    public String getState(@RequestParam("id") StateAbbreviation stateAbbreviation) {
        if(stateAbbreviation == StateAbbreviation.CA) {
            return californiaPrecinctData;
        } else if(stateAbbreviation == StateAbbreviation.PA) {
            return pennsylvaniaPrecinctData;
        } else {
            return louisianaPrecinctData;
        }
    }

    // This will return the specified state's district data
    @RequestMapping(value = "/getDistricting", method = RequestMethod.POST)
    public String getDistricting(@RequestParam int stateId) {
        String geoJson = jobHandler.loadDistrictingData(stateId);
        return geoJson;
    }

    // This will be called when a the user creates a job
    @RequestMapping(value = "/initializeJob", method = RequestMethod.POST)
    public int submitJob(@RequestParam StateAbbreviation stateName, @RequestParam int userCompactness, @RequestParam int populationDifferenceLimit, @RequestParam List<RaceEthnicity> ethnicities, @RequestParam int numberOfMaps) {
        Job job = jobHandler.createJob(stateName, userCompactness, populationDifferenceLimit, ethnicities, numberOfMaps);
        jobs.put(job.getId(), job);
        return job.getId();
    }

    // This will cancel the specified job
    @RequestMapping(value = "/cancelJob", method = RequestMethod.POST)
    public boolean cancelJob(@RequestParam int jobId) {
        boolean isCancelled = jobHandler.cancelJobData(jobId);
        if(isCancelled) {
            Job job = jobs.get(jobId);
            job.setJobStatus(JobStatus.CANCELLED);
        }
        // Based on cancellation, a modal should popup with details
        return isCancelled;
    }

    // This will ret
    @RequestMapping(value = "/getJobHistory", method = RequestMethod.POST)
    public List<Job> getHistory() {
        List<Job> jobList = new ArrayList<Job>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    // This will delete the specified job
    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
    public boolean deleteJob(@RequestParam int jobId) {
        boolean isDeleted = jobHandler.deleteJobData(jobId);
        if(isDeleted) {
            jobs.remove(jobId);
        }
        return isDeleted;
    }

    // Current scheduled for every 5 seconds
    // fixedDelay is in milliseconds
    // https://www.baeldung.com/spring-scheduled-tasks
    @Scheduled(fixedDelay = 5000)
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

    // Testing/working post method
    @PostMapping("/hello")
    public String hello(@RequestParam("id") String string) {
        System.out.println(string);
        return "MMMM";
    }

    // Testing/working post method
    @GetMapping("/bye")
    public void bye() {
        System.out.println("bye");
    }
}
