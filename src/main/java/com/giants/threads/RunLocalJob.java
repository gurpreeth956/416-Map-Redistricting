package com.giants.threads;

import com.giants.domain.Job;

public class RunLocalJob implements Runnable {

    private Job job;

    public RunLocalJob(Job job) {
        this.job = job;
    }

    public void run() {
//        boolean jobExecuted = job.executeLocalJob();
        boolean jobExecuted = true;
        if (jobExecuted) {
            job.generateJobData();
        }
    }
}
