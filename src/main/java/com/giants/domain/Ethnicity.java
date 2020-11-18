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

        public EthnicityKey() {

        }

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
        public RaceEthnicity getEthnicity() {
            return ethnicity;
        }

        public void setEthnicity(RaceEthnicity ethnicity) {
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

    public Ethnicity() {

    }

    public Ethnicity(int jobId, RaceEthnicity ethnicity) {
        this.primaryKey = new EthnicityKey(jobId, ethnicity);
    }

    @EmbeddedId
    public EthnicityKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(EthnicityKey primaryKey) {
        this.primaryKey = primaryKey;
    }

}
