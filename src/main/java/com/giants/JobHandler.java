package com.giants;

import com.giants.domain.*;
import com.giants.enums.RaceEthnicity;
import com.giants.enums.StateAbbreviation;
import com.giants.enums.JobStatus;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class JobHandler {

    private Hashtable<Integer, Job> jobs;
    private List<Integer> jobsToCheckStatus;
    private String pennsylvaniaPrecinctData;
    private String louisianaPrecinctData;
    private String californiaPrecinctData;

    /**
     * This method is called when the server starts up. It is used for instantiating
     * the instance variables and getting jobs and precinct data from the database.
     */
    public void initialSetup() {
        jobs = new Hashtable<Integer, Job>();
        List<Job> jobList = loadAllJobData();
        jobsToCheckStatus = new ArrayList<Integer>();
        for (Job job : jobList) {
            jobs.put(job.getId(), job);
            if (job.getJobStatus() != JobStatus.COMPLETED && job.getJobStatus() != JobStatus.CANCELLED) {
                jobsToCheckStatus.add(job.getId());
            }
        }

        // Need a to verify format for Precinct GeoJSON
        pennsylvaniaPrecinctData = loadPrecinctData(StateAbbreviation.PA);
        louisianaPrecinctData = loadPrecinctData(StateAbbreviation.LA);
        californiaPrecinctData = loadPrecinctData(StateAbbreviation.CA);
    }

    /**
     * This method is for getting the specified state precinct data.
     *
     * @param stateAbbreviation - Abbreviation for state
     * @return Precinct data as a string
     */
    public String getStateData(StateAbbreviation stateAbbreviation) {
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
        Job job = new Job(stateName, userCompactness, populationDifferenceLimit, numberOfMaps);

        // GET RID OF MAGIC NUMBERRRRRRRR
        if (numberOfMaps > 100) {

            /*
             Temporary command for testing seawulf (slurm runs the multiproc file which
             is also stores in SeaWulf at ~/testing
             */
            String command = "cat src/main/resources/seawulf.slurm | ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                    "'source /etc/profile.d/modules.sh; module load slurm; module load anaconda/2; module load " +
                    "mvapich2/gcc/64/2.2rc1; cd ~/testing; sbatch'";
            String processOutput = createScript(command);
            if (!processOutput.contains("Submitted batch job")) return null;
            job.setOnSeaWulf(Integer.parseInt(processOutput.split("\\s+")[3]));
            job.setJobStatus(JobStatus.WAITING);
            System.out.println(job.getOnSeaWulf());
        } else {
            job.setOnSeaWulf(-1);
            job.setJobStatus(JobStatus.RUNNING);
            job.executeLocalJob();
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
            // Add new job to the db
            em.getTransaction().begin();
            em.persist(job);
            System.out.println(job.getId());
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
        if (job.getOnSeaWulf() != -1) jobsToCheckStatus.add(job.getId());
        return job;
    }

    /**
     * This method is for cancelling a job. It updates the job status in the database
     * and in the jobs table and in the jobsToCheckStatus list.
     *
     * @param jobId - Id of specified job
     * @return Boolean for success
     */
    public boolean cancelJobData(int jobId) {
        Job job = jobs.get(jobId);
        System.out.println(job.getJobStatus());
        // The below line is commented out for testing
        // if (job.getOnSeaWulf() == -1 || job.getJobStatus() == JobStatus.COMPLETED || job.getJobStatus() == JobStatus.CANCELLED) return false;
        String command = String.format("ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                "'source /etc/profile.d/modules.sh; module load slurm; scancel %d'", job.getOnSeaWulf());
        String processOutput = createScript(command);
        jobs.replace(jobId, job);
        jobsToCheckStatus.remove(new Integer(jobId));
        return changeJobStatus(jobId, JobStatus.CANCELLED);
    }

    /**
     * This method is for deleting a job. It removes the job in the database and in the
     * jobs table and in the jobsToCheckStatus list. It cancels the job if it needs to
     * before deleting.
     *
     * @param jobId - Id of specified job
     * @return Boolean for success
     */
    public boolean deleteJobData(int jobId) {
        Job job = jobs.get(jobId);
        // If job is running/waiting cancel job
        if (job.getJobStatus() == JobStatus.RUNNING || job.getJobStatus() == JobStatus.WAITING) {
            boolean isCancelled = cancelJobData(jobId);
            if (!isCancelled) {
                System.out.println("Issue cancelling job");
                return false;
            }
        }
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Delete job tuple from database
            em.getTransaction().begin();
            Query q = em.createQuery("DELETE Job WHERE id = :id");
            q.setParameter("id", jobId);
//            Job job = em.find(Job.class, jobId);
//            for (Ethnicity ethnicity : job.getEthnicities()) {
//                em.remove(ethnicity);
//            }
//            em.remove(job);
            q.executeUpdate();
            em.getTransaction().commit();
            // make sure to remove all instances of job object from server
        } catch (Exception e) {
            // Return some kind of error here
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            return false;
        }
        jobs.remove(jobId);
        return true;
    }

    /**
     * This method is called when the server starts up and retrieves all the jobs
     * for the database and stores it in the jobs list
     *
     * @return List of all the jobs
     */
    public List<Job> loadAllJobData() {
        List<Job> jobs = new ArrayList<Job>();

        // Get jobs from entityManager
        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Get the list of all tuples in job table
//            Query q = em.createQuery("SELECT j FROM Jobs j", Job.class);
//            jobs = q.getResultList();
            jobs = em.createNamedQuery("Jobs.getJobs", Job.class).getResultList();
        } catch (Exception e) {
            // Return some kind of error here
            System.out.println(e.getMessage());
            em.getTransaction().rollback();
            return null;
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
            em.getTransaction().rollback();
            return null;
        }
        // Return
        String geoJson = "";
        for (District district : districts) {
            geoJson += district.getGeoJson();
        }
        return geoJson;
    }

    public String loadPrecinctData(StateAbbreviation stateAbbreviation) {
        List<Precinct> precincts = new ArrayList<Precinct>();

        // Get State from entityManager
//        EntityManager em = JPAUtility.getEntityManager();
        try {
            // Start of transaction
//            em.getTransaction().begin();
//            // Get all Precinct objects where StateAbbreviation == stateAbbreviation
//            Query q = em.createQuery("SELECT p FROM Precincts p WHERE stateAbbreviation = :stateAbbreviation", Job.class)
//                    .setParameter("stateAbbreviation", stateAbbreviation);
//            em.getTransaction().commit();
//            precincts = q.getResultList();
        } catch (Exception e) {
            // Return some kind of error here

//            em.getTransaction().rollback();
//            return null;
        }
        String geoJson = "";
        for (Precinct precinct : precincts) {
            geoJson += precinct.getGeoJson();
        }
        return geoJson;
    }

    /**
     * This method gets all the jobs from the jobs table.
     *
     * @return The list of jobs
     */
    public List<Job> getJobHistory() {
        List<Job> jobList = new ArrayList<Job>();
        for (Integer id : jobs.keySet()) {
            jobList.add(jobs.get(id));
        }
        return jobList;
    }

    /**
     * This method checks the status of jobs currently pending or running on SeaWulf.
     * If the job is no longer pending, it is switched from WAITING to RUNNING. If it
     * is not longer running it is switched from RUNNING to COMPLETED. It calls the
     * method calculateEndData() if the job has switched to completed and removes the
     * completed jobs from jobsToCheckStatus.
     */
    public void getJobStatusSeaWulf() {
        // For each job in jobs that is waiting or running check SeaWulf
        List<Job> jobsToCheck = new ArrayList<Job>();
        for (int id : jobsToCheckStatus) {
            jobsToCheck.add(jobs.get(id));
        }
        // Pretend status is returned from SeaWulf
        // Iterator to handle sync issues
        Iterator<Job> jobIterator = jobsToCheck.iterator();
        while (jobIterator.hasNext()) {
            Job job = jobIterator.next();
            System.out.println(job.getId());
            if (job.getJobStatus() == JobStatus.WAITING) {
                // Check if status changed from waiting
                String command = String.format("ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                        "'source /etc/profile.d/modules.sh; module load slurm; squeue -j %d'", job.getOnSeaWulf());
                String processOutput = createScript(command);
                if (!processOutput.contains("PD")) {
                    changeJobStatus(job.getId(), JobStatus.RUNNING);
                }
            }
            if (job.getJobStatus() == JobStatus.RUNNING) {
                // Check if status change from running
                String command = String.format("ssh gurpreetsing@login.seawulf.stonybrook.edu " +
                        "'source /etc/profile.d/modules.sh; module load slurm; sacct -Xj %d'", job.getOnSeaWulf());
                String processOutput = createScript(command);
                if (processOutput != null) {
                    changeJobStatus(job.getId(), JobStatus.COMPLETED);


                    // DO THIS IN A SEPARATE METHOD !!! (make sure to update db)
                    // Send slurm script to calculate data (avg, extreme, boxwhiskers
                    String filePath = job.retrieveSeaWulfData();
                    // Methods below here return boolean not sure if check is necessary
                    job.countCounties(filePath);
                    job.generateJsonFile(filePath);
                    job.generateAvgExtDistrictingPlan(filePath);
                    job.generateBoxWhiskers(filePath);

                    // Update the database
//                    EntityManager em = JPAUtility.getEntityManager();
//                    try {
//                        // Change database job status to completed
//                        em.getTransaction().begin();
//                        Query q = em.createQuery("UPDATE Jobs SET status = :status, averageStateId = :averageStateId," +
//                                "extremeStateId = :extremeStateId WHERE id = :id");
//                        q.setParameter("id", job.getId());
//                        q.setParameter("averageStateId", job.getAverageStateId());
//                        q.setParameter("extremeStateId", job.getExtremeStateId());
//                        q.setParameter("jobStatus", job.getJobStatus());
//                        q.executeUpdate();
//
//                        // UPDATE BOX WHISKER TABLE HEREEE
//
//                        em.getTransaction().commit();
//                    } catch (Exception e) {
//                        // Return some kind of error here
//                        em.getTransaction().rollback();
//                        return null;
//                    }
                    // Remove job from jobs to check since it is completed
                    jobIterator.remove();
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
            // Change job status
            em.getTransaction().begin();
            job.setJobStatus(jobStatus);
            em.getTransaction().commit();
            // make sure to remove all instances of job object from server
        } catch (Exception e) {
            // Return some kind of error here
            em.getTransaction().rollback();
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * This method creates a script in a temporary file and runs the script in a process
     *
     * @param cmd - String of the command
     * @return The process output
     */
    private String createScript(String cmd) {
        String processOutput = null;
        try {
            File tempScript = File.createTempFile("script", null, new File("./src/main/resources/scripts"));
            FileWriter fw = new FileWriter(tempScript);
            fw.write(cmd);
            fw.close();
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();
            processOutput = getProcessOutput(process);
            tempScript.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processOutput;
    }

    /**
     * This method redirects the processes output from SeaWulf and stores in a variables
     *
     * @param process - The process
     * @return String with the process output
     */
    private String getProcessOutput(Process process) {
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator")); // Returns OS dependent line separator
            }
            result = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
