package com.giants.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PrecinctNeighbors")
public class PrecinctNeighbor {

    @Embeddable
    public static class PrecinctNeighborKey implements Serializable {

        private int id;
        private int precinctId;

        public PrecinctNeighborKey(int id, int precinctId) {
            this.id = id;
            this.precinctId = precinctId;
        }

        @Column(name = "id")
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
            PrecinctNeighborKey that = (PrecinctNeighborKey) o;
            return id == that.id &&
                    precinctId == that.precinctId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, precinctId);
        }
    }

    private PrecinctNeighborKey primaryKey;
    private Precinct precinct1;
    private Precinct precinct2;

    public PrecinctNeighbor() {

    }

    public PrecinctNeighbor(Precinct precinct1, Precinct precinct2) {
        this.primaryKey = new PrecinctNeighborKey(precinct1.getId(), precinct2.getId());
        this.precinct1 = precinct1;
        this.precinct2 = precinct2;
    }

    @EmbeddedId
    public PrecinctNeighborKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrecinctNeighborKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("id")
    @JoinColumn(name="id", referencedColumnName = "id", insertable = false, updatable = false)
    public Precinct getPrecinct1() {
        return precinct1;
    }

    public void setPrecinct1(Precinct precinct1) {
        this.precinct1 = precinct1;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("precinctId")
    @JoinColumn(name="precinctId", referencedColumnName = "id", insertable = false, updatable = false)
    public Precinct getPrecinct2() {
        return precinct2;
    }

    public void setPrecinct2(Precinct precinct2) {
        this.precinct2 = precinct2;
    }
}
