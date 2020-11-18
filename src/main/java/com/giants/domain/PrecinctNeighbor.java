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

    public PrecinctNeighbor() {

    }

    public PrecinctNeighbor(int precinct1Id, int precinct2Id) {
        this.primaryKey = new PrecinctNeighborKey(precinct1Id, precinct2Id);
    }

    @EmbeddedId
    public PrecinctNeighborKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrecinctNeighborKey primaryKey) {
        this.primaryKey = primaryKey;
    }

}
