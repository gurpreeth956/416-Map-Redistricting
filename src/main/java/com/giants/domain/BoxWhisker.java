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

    public BoxWhisker() {

    }

    public BoxWhisker(int jobId, int position) {
        this.primaryKey = new BoxWhiskerKey(jobId, position);
    }

    @EmbeddedId
    public BoxWhiskerKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(BoxWhiskerKey primaryKey) {
        this.primaryKey = primaryKey;
    }

}
