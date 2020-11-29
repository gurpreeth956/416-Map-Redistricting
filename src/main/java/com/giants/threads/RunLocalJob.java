package com.giants.threads;

import com.giants.domain.Job;
import com.giants.enums.JobStatus;

public class RunLocalJob implements Runnable {

    private Job job;

    public RunLocalJob(Job job) {
        this.job = job;
    }

    public void run() {
        boolean jobExecuted = job.executeLocalJob();
//        boolean jobExecuted = true;
//        if (jobExecuted) {
//            job.generateJobData();
//        }
//        job.setJobStatus(JobStatus.PROCESSING);
    }
}
