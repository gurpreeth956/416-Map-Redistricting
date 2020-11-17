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
    private District district;
    private Precinct precinct;

    public DistrictPrecinct() {

    }

    public DistrictPrecinct(District district, Precinct precinct) {
        this.primaryKey = new DistrinctPrecinctKey(district.getId(), precinct.getId());
        this.district = district;
        this.precinct = precinct;
    }

    @EmbeddedId
    public DistrinctPrecinctKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(DistrinctPrecinctKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("districtId")
    @JoinColumn(name="districtId", referencedColumnName = "id", insertable = false, updatable = false)
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("precinctId")
    @JoinColumn(name="precinctId", referencedColumnName = "id", insertable = false, updatable = false)
    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }
}
