import domain.Job;
import enums.Ethnicity;
import enums.StateAbbreviation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
