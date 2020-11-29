package com.giants;

import com.giants.domain.*;
import com.giants.enums.RaceEthnicity;
import com.giants.enums.StateAbbreviation;
import com.giants.enums.JobStatus;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.giants.threads.RunLocalJob;
import org.json.simple.*;
import org.json.simple.parser.*;

public class JobHandler {

    // Switched to hashtable for synchronization issues
    private Hashtable<Integer, Job> jobs;
    private List<Integer> jobsToCheckStatus;
    private String pennsylvaniaPrecinctData;
    private String louisianaPrecinctData;
    private String californiaPrecinctData;
    private Properties properties;

    /**
     * This method is called when the server starts up. It is used for instantiating
     * the instance variables and getting jobs and precinct data from the database.
     */
    public void initialSetup() {
        try {
            InputStream input = new FileInputStream("./src/main/resources/config.properties");
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        jobs = new Hashtable<Integer, Job>();
        List<Job> jobList = loadAllJobData();
        jobsToCheckStatus = new ArrayList<>();
        for (Job job : jobList) {
            jobs.put(job.getId(), job);
            if (job.getJobStatus() != JobStatus.COMPLETED && job.getJobStatus() != JobStatus.CANCELLED && job.getSeaWulfId() != -1) {
                jobsToCheckStatus.add(job.getId());
            }
        }

        // Need a to verify format for Precinct GeoJSON
//        System.out.println("1");
//        pennsylvaniaPrecinctData = loadPrecinctData(StateAbbreviation.PA).toString();
//        System.out.println("2");
//        louisianaPrecinctData = loadPrecinctData(StateAbbreviation.LA).toString();
//        System.out.println("3");
//        californiaPrecinctData = loadPrecinctData(StateAbbreviation.CA).toString();
//        pennsylvaniaPrecinctData = loadPrecinctData(StateAbbreviation.PA);
//        System.out.println("2");
//        louisianaPrecinctData = loadPrecinctData(StateAbbreviation.LA);
//        System.out.println("3");
//        californiaPrecinctData = loadPrecinctData(StateAbbreviation.CA);
        System.out.println("Completed");
    }

    /**
     * This method is for getting the specified state precinct data.
     *
     * @param stateAbbreviation - Abbreviation for state
     * @return Precinct data as a string
     */
    public String getStateData(StateAbbreviation stateAbbreviation) {
        // NEED TO WAIT UNTIL PRECINCT DATA DONE LOADING FORM INITIALSETUP() (wait till Completed is printed)
        if (stateAbbreviation == StateAbbreviation.CA) {
            return californiaPrecinctData;
        } else if (stateAbbreviation == StateAbbreviation.PA) {
            return pennsylvaniaPrecinctData;
        } else {
            return louisianaPrecinctData;
        }
    }

    /**
     * This method is for creating a job. It decides where to run the job and then adds
     * the job to the jobs table and list of jobs to check if it will run on SeaWulf. It
     * also adds the job to the database.
     *
     * @param stateName - user specified state for the jb=ob
     * @param userCompactness - user specified compactness
     * @param populationDifferenceLimit - user specified population percent difference
     * @param userEthnicities - user specified ethnicities
     * @param numberOfMaps - user specified number of maps
     * @return The created job object
     */
    public Job createJob(StateAbbreviation stateName, int userCompactness, double populationDifferenceLimit,
                         List<RaceEthnicity> userEthnicities, int numberOfMaps) {
        Job job = new Job(stateName, userEthnicities, userCompactness, populationDifferenceLimit, numberOfMaps);
        int seaWulfThreshold = Integer.parseInt(properties.getProperty("seaWulfThreshold"));
        boolean runLocally = false;
        if (numberOfMaps > seaWulfThreshold) {
            if (!job.executeSeaWulfJob()) return null;
            job.setJobStatus(JobStatus.WAITING);
        } else {
            runLocally = true;
        }

        EntityManager em = JPAUtility.getEntityManager();
//        EntityTransaction txn = em.getTransaction();
//        if (txn.isActive()) {
//            try {
//                txn.rollback();
//            } catch (PersistenceException | IllegalStateException e) {
//                System.out.println(e.getMessage());
//            }
//        }
        try {
            em.getTransaction().begin();
            em.persist(job);
            if (runLocally) {
                job.setSeaWulfId(-1);
                job.setJobStatus(JobStatus.RUNNING);
                Runnable r = new RunLocalJob(job);
                new Thread(r).start();
            }
            // Create and add all the ethnicities
            List<Ethnicity> jobEthnicities = new ArrayList<>();
            for (RaceEthnicity userEthnicity : userEthnicities) {
                jobEthnicities.add(new Ethnicity(job.getId(), userEthnicity));
            }
            job.setEthnicities(jobEthnicities);
            em.getTransaction().commit();
        } catch (Exception e) {
            // Return some kind of error here
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            return null;
        }
        jobs.put(job.getId(), job);
        jobsToCheckStatus.add(job.getId());
        return job;
    }

    /**
     * This method is for cancelling a job. It updates the job status in the database
     * and in the jobs table and in the jobsToCheckStatus list.
     *
     * @param jobId - Id of specified job
     * @return List of jobs
     */
    public List<Job> cancelJobData(int jobId) {
        Job job = jobs.get(jobId);
        // Make sure valid jobId (to be safe)
        if (job != null && job.getSeaWulfId() != -1 && job.getJobStatus() != JobStatus.COMPLETED &&
                job.getJobStatus() != JobStatus.CANCELLED) {
            Script script = new Script();
            String command = String.format("ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                    "'source /etc/profile.d/modules.sh; module load slurm; scancel %d'", job.getSeaWulfId());
            String processOutput = script.createScript(command);
            jobs.replace(jobId, job);
            jobsToCheckStatus.remove(new Integer(jobId));
        }
        List<Job> jobList = new ArrayList<>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    /**
     * This method is for deleting a job. It removes the job in the database and in the
     * jobs table and in the jobsToCheckStatus list. It cancels the job if it needs to
     * before deleting.
     *
     * @param jobId - Id of specified job
     * @return List of jobs
     */
    public List<Job> deleteJobData(int jobId) {
        Job job = jobs.get(jobId);
        // If local job then make sure it was cancelled or completed (to be safe)
        if (job != null && (job.getSeaWulfId() != -1 || (job.getSeaWulfId() == -1 && job.getJobStatus() != JobStatus.RUNNING))) {
            // If job is running or waiting cancel job first
            if (job.getJobStatus() == JobStatus.RUNNING || job.getJobStatus() == JobStatus.WAITING) {
                cancelJobData(jobId);
            }
            EntityManager em = JPAUtility.getEntityManager();
            try {
                em.getTransaction().begin();
                Query q = em.createQuery("DELETE Job WHERE id = :id");
                q.setParameter("id", jobId);
                q.executeUpdate();
                em.getTransaction().commit();
                jobs.remove(jobId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                em.getTransaction().rollback();
            }
        }

        List<Job> jobList = new ArrayList<>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    /**
     * This method is called when the server starts up and retrieves all the jobs
     * for the database and stores it in the jobs list
     *
     * @return List of all the jobs
     */
    public List<Job> loadAllJobData() {
        List<Job> jobs;
        EntityManager em = JPAUtility.getEntityManager();
        try {
            jobs = em.createNamedQuery("Jobs.getJobs", Job.class).getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return jobs;
    }

    public String loadDistrictingData(int stateId) {
        System.out.println("AAAAA");
        // Return format
//        data = {
//            districtPos: ,
//            pop (for each):
//            vap (for each):
//            geoJson: [
//                    ]
//        }

        List<District> districts;
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Get all District objects where stateId == stateId
//            Query q = em.createQuery("SELECT d FROM Districts d WHERE stateId = :stateId", Job.class)
//                    .setParameter("stateId", stateId);
//            em.getTransaction().commit();
//            districts = q.getResultList();
//            Query q = em.createQuery("SELECT Districts WHERE stateId = :stateId");
//            q.setParameter("stateId", stateId);
//            q.executeUpdate();
//            districts = q.getResultList();
            State state = em.find(State.class, stateId);
            System.out.println(stateId + " " + state.getId());
            districts = state.getDistricts();
            for (District d : districts) {
                System.out.println("HEHE" + d.getId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        // Return
        String geoJson = "";
        for (District district : districts) {
//            geoJson += district.getPopAndVap();
//            System.out.println(geoJson);
        }
        return geoJson;
    }

    /**
     * This method loads the json precinct data of a specific state.
     *
     * @param stateAbbreviation
     * @return
     */
    public JSONObject loadPrecinctData(StateAbbreviation stateAbbreviation) {
        String filePath = null;
        if (stateAbbreviation == StateAbbreviation.CA) {
            filePath = "./src/main/resources/jsons/precincts/CaliforniaPrecincts.json";
        } else if (stateAbbreviation == StateAbbreviation.LA) {
            filePath = "./src/main/resources/jsons/precincts/LouisianaPrecincts.json";
        } else if (stateAbbreviation == StateAbbreviation.PA) {
            filePath = "./src/main/resources/jsons/precincts/PennsylvaniaPrecincts.json";
        }
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(filePath));
            return (JSONObject) obj;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * This method gets all the jobs from the jobs table.
     *
     * @return The list of jobs
     */
    public List<Job> getJobHistory() {
        List<Job> jobList = new ArrayList<>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    /**
     * This method checks the status of jobs currently pending or running on SeaWulf and locally.
     * If the job is no longer pending, it is switched from WAITING to RUNNING. If it is not longer
     * running it is switched from RUNNING to COMPLETED. It calls the method calculateEndData() if
     * the job has switched to completed and removes the completed jobs from jobsToCheckStatus.
     */
    public void checkJobStatus() {
        // For each job in jobs that is waiting or running check SeaWulf
        List<Job> jobsToCheck = new ArrayList<>();
        for (int id : jobsToCheckStatus) {
            jobsToCheck.add(jobs.get(id));
        }
        // Iterator to handle sync issues
        Iterator<Job> jobIterator = jobsToCheck.iterator();
        while (jobIterator.hasNext()) {
            Script script = new Script();
            Job job = jobIterator.next();

            if (job.getSeaWulfId() == -1) {
                // PROCESSING for local job means it has finished running
                if (job.getJobStatus() == JobStatus.PROCESSING) {
                    System.out.println("Starting Processing...");
                    parseLocalDistrictingJson(job);
                    System.out.println("Finishing Processing...");
                    changeJobStatus(job.getId(), JobStatus.COMPLETED);
                }
                continue;
            }
            if (job.getJobStatus() == JobStatus.WAITING) {
                // Check if status changed from waiting
                String command = String.format("ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                        "'source /etc/profile.d/modules.sh; module load slurm; squeue -j %d'", job.getSeaWulfId());
                String processOutput = script.createScript(command);
                if (!processOutput.contains("PD")) {
                    changeJobStatus(job.getId(), JobStatus.RUNNING);
                }
            }
            if (job.getJobStatus() == JobStatus.RUNNING) {
                // Check if status change from running
                String command = String.format("ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                        "'source /etc/profile.d/modules.sh; module load slurm; sacct -Xj %d'", job.getSeaWulfId());
                String processOutput = script.createScript(command);
                if (processOutput != null) {

                    // DO THIS IN A SEPARATE METHOD !!! (make sure to update db)
                    // Send slurm script to calculate data (avg, extreme, boxwhiskers

                    changeJobStatus(job.getId(), JobStatus.COMPLETED);
//                    String filePath = job.retrieveSeaWulfData();
                    // Methods below here return boolean not sure if check is necessary
//                    job.countCounties(filePath);
//                    job.generateJsonFile(filePath);
//                    job.generateAvgExtDistrictingPlan(filePath);
//                    job.generateBoxWhiskers(filePath);

                }
            }
        }
        for (Job job : jobsToCheck) {
            jobs.replace(job.getId(), job);
            if (job.getJobStatus() == JobStatus.COMPLETED) {
                jobsToCheckStatus.remove(new Integer(job.getId()));
            }
        }
    }

    /**
     * This method changes the job status of a job
     *
     * @param jobId - Id of the job
     * @param jobStatus - Status to change job to
     * @return Boolean for success
     */
    private boolean changeJobStatus(int jobId, JobStatus jobStatus) {
        Job job = jobs.get(jobId);
        EntityManager em = JPAUtility.getEntityManager();
        try {
            em.getTransaction().begin();
            job.setJobStatus(jobStatus);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
        return true;
    }

    /**
     * This method is called after a local job is completed. It calculates the number of
     * counties, the average plan, the extreme plan, orders the districts by user requested
     * VAPs and the box and whiskers data.
     *
     * @param job - The job to parse
     * @return Boolean for success
     */
    private boolean parseLocalDistrictingJson(Job job) {
        EntityManager em = JPAUtility.getEntityManager();
        try {
            em.getTransaction().begin();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("./src/main/resources/Algorithm/Results/" + job.getId() + ".json"));
            JSONArray districtingsJson = (JSONArray) jsonObject.get("Districtings");
            List<List<Integer>> boxWhiskersData = createBoxWhiskerArray(job);
            List<State> states = job.getStates();
            int totalSpecifiedStateVap = 0;
            // Iterate through districtings array
            for (int i = 0; i < districtingsJson.size(); i++) {
                JSONObject districtingJson = (JSONObject) districtingsJson.get(i);
                State state = new State(job.getId(), (int) (long) districtingJson.get("Max Pop Difference"),
                        ((double) districtingJson.get("Overall Compactness")));
                em.persist(state);
                states.add(state);
                totalSpecifiedStateVap = 0;
                List<District> districts = state.getDistricts();
                JSONArray districtsJson = (JSONArray) districtingJson.get("Districts");
                // Iterate through districts array
                for (int j = 0; j < districtsJson.size(); j++) {
                    JSONObject districtJson = (JSONObject) districtsJson.get(j);
                    PopAndVap popAndVap = new PopAndVap();
                    popAndVap.setAbbreviation(job.getAbbreviation());
                    District district = new District(state.getId(), popAndVap);

                    // NEED TO ADD ALL PRECINCTS VAPS AND POPS TO DISTRICT GEOJSON

                    em.persist(popAndVap);
                    em.persist(district);
                    districts.add(district);
                    List<DistrictPrecinct> districtPrecincts = district.getDistrictPrecincts();
                    // For counting counties as you iterate through the precincts in each district
                    Set<Integer> counties = new HashSet<>();
                    JSONArray precinctsJson = (JSONArray) districtJson.get("precincts");
                    // Iterate through precincts array
                    for (int k = 0; k < precinctsJson.size(); k++) {
                        Precinct precinct = em.find(Precinct.class, precinctsJson.get(k));
                        System.out.println(precinct.getId() + " " + precinct.getAbbreviation());

                        DistrictPrecinct districtPrecinct = new DistrictPrecinct(district.getId(), precinct.getId());
                        em.persist(districtPrecinct);
                        districtPrecincts.add(districtPrecinct);
                        counties.add(precinct.getCountyId());
                        // Get pop and vap of the user entered ethnicities
                        int precinctSpecifiedVap = precinct.getSpecificVap(job.getEthnicities());
                        district.addPopAndVap(precinct.getSpecificPop(job.getEthnicities()), precinctSpecifiedVap);
                        totalSpecifiedStateVap += precinctSpecifiedVap;
                    }
                    district.setDistrictPrecincts(districtPrecincts);
                    district.setNumberOfCounties(counties.size());
                }
                // Sort districts and set box whiskers position number for each state
                state.sortDistricts(boxWhiskersData, districts);
            }
            // Calculate average and extreme states and box and whiskers
            job.calculateAvgExtDistrictingPlan(states);
            List<BoxWhisker> boxWhiskers = job.generateBoxWhiskers(boxWhiskersData, totalSpecifiedStateVap);
            for (BoxWhisker boxWhisker : boxWhiskers) {
                em.persist(boxWhisker);
            }
            em.getTransaction().commit();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            return false;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
        return true;
    }

    private List<List<Integer>> createBoxWhiskerArray(Job job) {
        int districts = 0;
        switch (job.getAbbreviation()) {
            case CA:
                districts = Integer.parseInt(properties.getProperty("caNumOfDistricts"));
                break;
            case LA:
                districts = Integer.parseInt(properties.getProperty("laNumOfDistricts"));
                break;
            case PA:
                districts = Integer.parseInt(properties.getProperty("paNumOfDistricts"));
                break;
        }

        List<List<Integer>> boxWhiskersData = new ArrayList<>();
        for (int i = 0; i < districts + 1; i++) {
            boxWhiskersData.add(i, new ArrayList<>());
        }
        return  boxWhiskersData;
    }
}

