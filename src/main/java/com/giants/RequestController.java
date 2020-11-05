package com.giants;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.giants.domain.Job;
import com.giants.enums.Ethnicity;
import com.giants.enums.StateAbbreviation;

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
        return 0;
    }

    public boolean cancelJob(int jobId) {
        return true;
    }

    public List<Job> getHistory() {
        return new ArrayList<Job>();
    }

    public boolean deleteJob(int jobId) {
        return true;
    }

    public void checkJobStatus() {

    }

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
