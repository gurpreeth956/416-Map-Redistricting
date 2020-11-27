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
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@EnableScheduling
public class RequestController {

    private JobHandler jobHandler;

    // Everything we need to do when server starts up goes here
    // https://www.baeldung.com/spring-postconstruct-predestroy
    @PostConstruct
    public void initialSetup() {
        jobHandler = new JobHandler();
        jobHandler.initialSetup();
    }

    // This will return the specified state's precinct data
    @RequestMapping(value = "/getState", method = RequestMethod.GET)
    public String getState(@RequestParam StateAbbreviation stateAbbreviation) {
        return jobHandler.getStateData(stateAbbreviation);
    }

    // This will return the specified state's district data
    @RequestMapping(value = "/getDistricting", method = RequestMethod.POST)
    public String getDistricting(@RequestParam int stateId) {
        return jobHandler.loadDistrictingData(stateId);
    }

    // This will be called when a the user creates a job
    @RequestMapping(value = "/initializeJob", method = RequestMethod.POST)
    public Job submitJob(@RequestParam StateAbbreviation stateName, @RequestParam int userCompactness, @RequestParam
            double populationDifferenceLimit, @RequestParam List<RaceEthnicity> ethnicities, @RequestParam int numberOfMaps) {
        Job job = jobHandler.createJob(stateName, userCompactness, populationDifferenceLimit, ethnicities, numberOfMaps);
        return job;
    }

    // This will cancel the specified job
    @RequestMapping(value = "/cancelJob", method = RequestMethod.POST)
    public List<Job> cancelJob(@RequestParam int jobId) {
        // Based on cancellation, a modal should popup with details
        return jobHandler.cancelJobData(jobId);
    }

    // This will return the list of jobs
    @RequestMapping(value = "/getJobHistory", method = RequestMethod.GET)
    public List<Job> getHistory() {
        return jobHandler.getJobHistory();
    }

    // This will delete the specified job
    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
    public List<Job> deleteJob(@RequestParam int jobId)  {
        return jobHandler.deleteJobData(jobId);
    }

    // Current scheduled for every 5 seconds (fixedDelay is in milliseconds)
    // https://www.baeldung.com/spring-scheduled-tasks
    @Scheduled(fixedDelay = 5000)
    public void checkJobStatus()  {
        System.out.println("TIMER");
        jobHandler.getJobStatusSeaWulf();
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
