package com.giants.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "DistrictPrecincts")
public class DistrictPrecinct {

    @Embeddable
    public static class DistrinctPrecinctKey implements Serializable {

        private int districtId;
        private int precinctId;

        public DistrinctPrecinctKey() {

        }

        public DistrinctPrecinctKey(int districtId, int precinctId) {
            this.districtId = districtId;
            this.precinctId = precinctId;
        }

        @Column(name = "districtId")
        public int getDistrictId() {
            return districtId;
        }

        public void setDistrictId(int districtId) {
            this.districtId = districtId;
        }

        @Column(name = "precinctId")
        public int getPrecinctId() {
            return precinctId;
        }

        public void setPrecinctId(int precinctId) {
            this.precinctId = precinctId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DistrinctPrecinctKey that = (DistrinctPrecinctKey) o;
            return districtId == that.districtId &&
                    precinctId == that.precinctId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(districtId, precinctId);
        }
    }

    private DistrinctPrecinctKey primaryKey;

    public DistrictPrecinct() {

    }

    public DistrictPrecinct(int districtId, int precinctId) {
        this.primaryKey = new DistrinctPrecinctKey(districtId, precinctId);
    }

    @EmbeddedId
    public DistrinctPrecinctKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(DistrinctPrecinctKey primaryKey) {
        this.primaryKey = primaryKey;
    }

}
