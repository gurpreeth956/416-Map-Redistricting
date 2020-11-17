package com.giants.domain;

import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import javax.websocket.OnError;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Precincts")
public class Precinct {

    private int id;
    private StateAbbreviation abbreviation;
    private int countyId;
    private GeoJSON geoJson;
    private List<PrecinctNeighbor> precinctNeighbors;

    public Precinct() {

    }

    public Precinct(int id, StateAbbreviation abbreviation, int countyId) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.countyId = countyId;
        this.precinctNeighbors = new ArrayList<>();
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "abbreviation")
    public StateAbbreviation getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(StateAbbreviation abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Column(name = "countyId")
    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    @OneToOne(optional = false)
    @JoinColumn(name="geoJsonId", referencedColumnName = "id", insertable = false, updatable = false)
    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }

    @OneToMany(mappedBy = "precinct1", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PrecinctNeighbor> getPrecinctNeighbors() {
        return precinctNeighbors;
    }

    public void setPrecinctNeighbors(List<PrecinctNeighbor> precinctNeighbors) {
        this.precinctNeighbors = precinctNeighbors;
    }
}
