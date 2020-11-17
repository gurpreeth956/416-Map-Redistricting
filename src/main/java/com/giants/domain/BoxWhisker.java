package com.giants.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "BoxWhiskers")
public class BoxWhisker {

    @Embeddable
    public static class BoxWhiskerKey implements Serializable {

        private int jobId;
        private int positionNum;

        public BoxWhiskerKey(int jobId, int positionNum) {
            this.jobId = jobId;
            this.positionNum = positionNum;
        }

        @Column(name = "jobId")
        public int getId() {
            return jobId;
        }

        public void setId(int jobId) {
            this.jobId = jobId;
        }

        @Column(name = "positionNum")
        public int getPositionNum() {
            return positionNum;
        }

        public void setPositionNum(int positionNum) {
            this.positionNum = positionNum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BoxWhiskerKey that = (BoxWhiskerKey) o;
            return jobId == that.jobId &&
                    positionNum == that.positionNum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(jobId, positionNum);
        }
    }

    private BoxWhiskerKey primaryKey;
    private Job job;

    public BoxWhisker() {

    }

    public BoxWhisker(Job job, int position) {
        this.primaryKey = new BoxWhiskerKey(job.getId(), position);
        this.job = job;
    }

    @EmbeddedId
    public BoxWhiskerKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(BoxWhiskerKey primaryKey) {
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
