package com.giants;

import com.giants.domain.State;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.giants.domain.Job;
import com.giants.enums.Ethnicity;
import com.giants.enums.StateAbbreviation;
import com.giants.enums.JobStatus;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RequestController {
    private JobHandler jobHandler;
    private Map<Integer, Job> jobs;
    private List<Integer> jobsToCheckStatus;
    // Need to import Json type, not sure yet
    private String pennsylvaniaGeoJson;
    private String louisianaGeoJson;
    private String californiaGeoJson;


    // I think this is right
    // https://www.baeldung.com/spring-postconstruct-predestroy
    @PostConstruct
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
    // Seems like @ResponseBody will take String and insert directly into resposne without modifications
    // This should work since our GeoJson String will already be in Json format

    @RequestMapping(value = "/getState",
            method = RequestMethod.GET)
    @ResponseBody
    public String getState(@RequestParam("id") StateAbbreviation stateAbbreviation) {
        if(stateAbbreviation == StateAbbreviation.CA) {
            return californiaGeoJson;
        } else if(stateAbbreviation == StateAbbreviation.PA) {
            return pennsylvaniaGeoJson;
        } else {
            return louisianaGeoJson;
        }
    }

    @RequestMapping(value = "/getDistricting",
            method = RequestMethod.GET)
    @ResponseBody
    public String getDistricting(@RequestParam int stateId) {
        State state = jobHandler.loadStateData(stateId);
        return "";
    }

    @RequestMapping(value = "/initializeJob",
            method = RequestMethod.POST)
    public int submitJob(@RequestParam StateAbbreviation stateName, @RequestParam int userCompactness, @RequestParam int populationDifferenceLimit, @RequestParam List<Ethnicity> ethnicities, @RequestParam int numberOfMaps, @RequestParam int numberOfDistricts) {
        Job job = jobHandler.createJob(stateName, userCompactness, populationDifferenceLimit, ethnicities, numberOfMaps, numberOfDistricts);
        jobs.put(job.getId(), job);
        return job.getId();
    }

    @RequestMapping(value = "/cancelJob",
            method = RequestMethod.POST)
    public boolean cancelJob(@RequestParam int jobId) {
        boolean isCancelled = jobHandler.cancelJobData(jobId);
        if(isCancelled) {
            Job job = jobs.get(jobId);
            job.setStatus(JobStatus.CANCELLED);
        }
        // Based on cancellation, a modal should popup with details
        return isCancelled;
    }

    @RequestMapping(value = "/getJobHistory",
            method = RequestMethod.GET)
    public List<Job> getHistory() {
        List<Job> jobList = new ArrayList<Job>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    @RequestMapping(value = "/deleteJob",
            method = RequestMethod.POST)
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
