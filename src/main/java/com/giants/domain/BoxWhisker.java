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

        public BoxWhiskerKey() {

        }

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
    private double minimum;
    private double quartile1;
    private double median;
    private double quartile3;
    private double maximum;

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

    @Column(name = "minimum")
    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    @Column(name = "quartile1")
    public double getQuartile1() {
        return quartile1;
    }

    public void setQuartile1(double quartile1) {
        this.quartile1 = quartile1;
    }

    @Column(name = "median")
    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    @Column(name = "quartile3")
    public double getQuartile3() {
        return quartile3;
    }

    public void setQuartile3(double quartile3) {
        this.quartile3 = quartile3;
    }

    @Column(name = "maximum")
    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }
}
