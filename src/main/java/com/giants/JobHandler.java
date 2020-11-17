package com.giants;

import com.giants.domain.*;
import com.giants.enums.RaceEthnicity;
import com.giants.enums.StateAbbreviation;
import com.giants.enums.JobStatus;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class JobHandler {

    public Job createJob(StateAbbreviation stateName, int userCompactness, double populationDifferenceLimit,
                         List<RaceEthnicity> ethnicities, int numberOfMaps) {
        Job job = new Job(stateName, userCompactness, populationDifferenceLimit, numberOfMaps);
        if (numberOfMaps > 100) {
            job.setOnSeaWulf(0);
            job.setJobStatus(JobStatus.WAITING);
            job.executeSeaWulfJob();
            // CHANGE SEAWULF TO JOB ID IN DATABASE
        } else {
            job.setOnSeaWulf(-1);
            job.setJobStatus(JobStatus.RUNNING);
            job.executeLocalJob();
        }

        // CREATE ETHNICITY OBJECTS THEN PERSIST

        // Now add job to database
        EntityManager em = JPAUtility.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(job);
            em.getTransaction().commit();
        } catch (Exception e) {
            // Return some kind of error here

        } finally {

        }
        return job;
    }

    public boolean cancelJobData(int jobId) {
        // Submit slurm to SeaWulf to cancel
        // For now pretend isCancelled is returned from seawulf (if running then return false)
        boolean isCancelled = true;

        // Based on isCancelled, use entity manager to update job info in db
        if (isCancelled) {
            EntityManager em = JPAUtility.getEntityManager();
            try {
                // Start of transaction
                em.getTransaction().begin();
                // Change database job status to cancelled
                Query q = em.createQuery("UPDATE Jobs SET status = :status WHERE id = :id");
                q.setParameter("id", jobId);
                q.setParameter("status", "cancelled");
                q.executeUpdate();
                em.getTransaction().commit();
            } catch (Exception e) {
                // Return some kind of error here
                return false;
            } finally {

            }
        }

        return isCancelled;
    }

    public boolean deleteJobData(int jobId) {
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Start of transaction
            em.getTransaction().begin();
            // Delete job tuple from database
            Query q = em.createQuery("DELETE Jobs WHERE id = :id");
            q.setParameter("id", jobId);
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            // Return some kind of error here
            return false;
        } finally {

        }

        return true;
    }

    public List<Job> loadAllJobData() {
        List<Job> jobs = new ArrayList<Job>();

        // Get jobs from entityManager
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Get the list of all tuples in job table
            Query q = em.createQuery("SELECT j FROM Jobs j", Job.class);
            jobs = q.getResultList();
        } catch (Exception e) {
            // Return some kind of error here

        }

        return jobs;
    }

    public String loadDistrictingData(int stateId) {
        List<District> districts = new ArrayList<District>();

        // Get State from entityManager
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Start of transaction
            em.getTransaction().begin();
            // Get all District objects where stateId == stateId
            Query q = em.createQuery("SELECT d FROM Districts d WHERE stateId = :stateId", Job.class)
                    .setParameter("stateId", stateId);
            em.getTransaction().commit();
            districts = q.getResultList();
        } catch (Exception e) {
            // Return some kind of error here

        } finally {

        }

        // Return
        String geoJson = "";
        for(District district:districts){
            geoJson += district.getGeoJson();
        }
        return geoJson;
    }

    public String loadPrecinctData(StateAbbreviation stateAbbreviation) {
        List<Precinct> precincts = new ArrayList<Precinct>();

        // Get State from entityManager
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Start of transaction
            em.getTransaction().begin();
            // Get all Precinct objects where StateAbbreviation == stateAbbreviation
            Query q = em.createQuery("SELECT p FROM Precincts p WHERE stateAbbreviation = :stateAbbreviation", Job.class)
                    .setParameter("stateAbbreviation", stateAbbreviation);
            em.getTransaction().commit();
            precincts = q.getResultList();
        } catch (Exception e) {
            // Return some kind of error here

        } finally {

        }

        String geoJson = "";
        for(Precinct precinct:precincts){
            geoJson += precinct.getGeoJson();
        }
        return geoJson;
    }

    public List<Job> getJobStatusSeaWulf(List<Job> jobs) {
        // For each job in jobs that is waiting or running check SeaWulf
        // Pretend status is returned from SeaWulf
        for(Job job : jobs) {
            if (job.getJobStatus() == JobStatus.RUNNING || job.getJobStatus() == JobStatus.WAITING) {
                // Send slurm to check current status
                String status = "COMPLETED";
                if (status.equals(JobStatus.COMPLETED.toString())) {
                    // Send slurm script to calculate data (avg, extreme, boxwhiskers
                    job.setJobStatus(JobStatus.COMPLETED);
                    String filePath = job.retrieveSeaWulfData();
                    // Methods below here return boolean not sure if check is necessary
                    job.countCounties(filePath);
                    job.generateJsonFile(filePath);
                    job.generateAvgExtDistrictingPlan(filePath);
                    job.generateBoxWhiskers(filePath);

                    // Update the database
                    EntityManager em = JPAUtility.getEntityManager();
                    try {
                        // Start of transaction
                        em.getTransaction().begin();
                        // Change database job status to cancelled
                        Query q = em.createQuery("UPDATE Jobs SET status = :status, averageStateId = :averageStateId," +
                                "extremeStateId = :extremeStateId WHERE id = :id");
                        q.setParameter("id", job.getId());
                        q.setParameter("averageStateId", job.getAverageStateId());
                        q.setParameter("extremeStateId", job.getExtremeStateId());
                        q.setParameter("status", "cancelled");
                        q.executeUpdate();

                        // UPDATE BOX WHISKER TABLE HEREEE

                        em.getTransaction().commit();
                    } catch (Exception e) {
                        // Return some kind of error here

                    } finally {

                    }
                } else if (!job.getJobStatus().equals(JobStatus.valueOf(status))) {
                    job.setJobStatus(JobStatus.valueOf(status));
                    // Merge in Entity Manager
                } else {
                    // Only jobs with changed status will stay in list of jobs to return
                    jobs.remove(job);
                }
            }
        }
        return jobs;
    }
}
