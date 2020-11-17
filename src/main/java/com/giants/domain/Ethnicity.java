package com.giants.domain;

import com.giants.enums.RaceEthnicity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Ethnicities")
public class Ethnicity {

    @Embeddable
    public static class EthnicityKey implements Serializable {

        private int jobId;
        private RaceEthnicity ethnicity;

        public EthnicityKey(int jobId, RaceEthnicity ethnicity) {
            this.jobId = jobId;
            this.ethnicity = ethnicity;
        }

        @Column(name = "jobId")
        public int getId() {
            return jobId;
        }

        public void setId(int jobId) {
            this.jobId = jobId;
        }

        @Column(name = "ethnicity")
        public RaceEthnicity getStateId() {
            return ethnicity;
        }

        public void setStateId(RaceEthnicity ethnicity) {
            this.ethnicity = ethnicity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EthnicityKey that = (EthnicityKey) o;
            return jobId == that.jobId &&
                    ethnicity == that.ethnicity;
        }

        @Override
        public int hashCode() {
            return Objects.hash(jobId, ethnicity);
        }
    }

    private EthnicityKey primaryKey;
    private Job job;

    public Ethnicity() {

    }

    public Ethnicity(Job job, RaceEthnicity ethnicity) {
        this.primaryKey = new EthnicityKey(job.getId(), ethnicity);
        this.job = job;
    }

    @EmbeddedId
    public EthnicityKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(EthnicityKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("jobId")
    @JoinColumn(name="jobId", referencedColumnName = "id", insertable = false, updatable = false)
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
