package com.giants.domain;

import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import javax.websocket.OnError;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Precincts")
public class Precinct {

    private String id;
    private StateAbbreviation abbreviation;
    private int countyId;
    private GeoJSON geoJson;
    private List<PrecinctNeighbor> precinctNeighbors;

    public Precinct() {

    }

    public Precinct(String id, StateAbbreviation abbreviation, int countyId) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.countyId = countyId;
        this.precinctNeighbors = new ArrayList<>();
    }

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "geoJsonId", referencedColumnName = "id")
    public GeoJSON getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(GeoJSON geoJson) {
        this.geoJson = geoJson;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "precinctId", referencedColumnName = "id")
    public List<PrecinctNeighbor> getPrecinctNeighbors() {
        return precinctNeighbors;
    }

    public void setPrecinctNeighbors(List<PrecinctNeighbor> precinctNeighbors) {
        this.precinctNeighbors = precinctNeighbors;
    }
}
