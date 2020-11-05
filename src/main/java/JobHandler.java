import domain.Job;
import enums.Ethnicity;
import enums.JobStatus;
import enums.StateAbbreviation;

import java.util.ArrayList;
import java.util.List;

public class JobHandler {
    // Need to add dependency for Hibernate
    // private EntityManagerFactory entityManagerFactory;

    public Job createJob(StateAbbreviation stateName, int userCompactness, int populationDifferenceLimit, List<Ethnicity> ethnicities, int numberOfMaps, int numberOfDistricts) {
        Job job = new Job(stateName, userCompactness, populationDifferenceLimit, numberOfMaps, numberOfDistricts, ethnicities);
        if (numberOfMaps > 100) {
            job.setOnSeaWulf(true);
            job.setStatus(JobStatus.WAITING);
            job.executeSeaWulfJob();
        } else {
            job.setOnSeaWulf(false);
            job.setStatus(JobStatus.RUNNING);
            job.executeLocalJob();
        }
        return job;
    }

    public boolean cancelJobData(int jobId) {
        // Submit slurm to SeaWulf to cancel
        // For now pretend isCancelled is returned from seawulf
        boolean isCancelled = true;
        // based on isCancelled, use entity manager to update job info in db
        return isCancelled;
    }

    public boolean deleteJobData(int jobId) {
        // Call entity manager to remove instance of job
        return true;
    }

    public List<Job> loadAllJobData() {
        // Get jobs from entityManager
        return new ArrayList<Job>();
    }

    public List<Job> getJobStatusSeaWulf(List<Job> jobs) {
        // For each job in jobs check SeaWulf
        // Pretend status is returned from SeaWulf
        for(Job job : jobs) {
            String status = "COMPLETED";
            if (status.equals(JobStatus.COMPLETED.toString())) {
                job.setStatus(JobStatus.COMPLETED);
                String filePath = job.getSeaWulfData();
                // Methods below here return boolean not sure if check is necessary
                job.countCounties(filePath);
                job.generateJsonFile(filePath);
                job.generateAvgExtDistrictingPlan(filePath);
                job.generateBoxWhiskers(filePath);
                // Merge in Entity Manager
            } else if(!job.getStatus().equals(JobStatus.valueOf(status))) {
                job.setStatus(JobStatus.valueOf(status));
                // Merge in Entity Manager
            } else {
                // Only jobs with changed status will stay in list of jobs to return
                jobs.remove(job);
            }
        }
        return jobs;
    }
}
