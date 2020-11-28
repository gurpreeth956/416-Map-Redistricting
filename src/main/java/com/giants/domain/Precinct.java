package com.giants.domain;

import com.giants.enums.RaceEthnicity;
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

    public int getSpecificPop(List<Ethnicity> ethnicities) {
        int total = 0;
        for (Ethnicity ethnicity : ethnicities) {
            switch (ethnicity.getPrimaryKey().getEthnicity()) {
                case HISPANIC_OR_LATINO:
                    total += this.geoJson.getHispanicPop();
                    break;
                case AMERICAN_INDIAN:
                    total += this.geoJson.getNativePop();
                    break;
                case ASIAN:
                    total += this.geoJson.getAsianPop();
                    break;
                case BLACK_OR_AFRICAN_AMERICAN:
                    total += this.geoJson.getBlackPop();
                    break;
                case NATIVE_HAWAIIAN_AND_OTHER_PACIFIC:
                    total += this.geoJson.getHawaiianPop();
                    break;
                case WHITE:
                    total += this.geoJson.getWhitePop();
                    break;
            }
        }
        return total;
    }

    public int getSpecificVap(List<Ethnicity> ethnicities) {
        int total = 0;
        for (Ethnicity ethnicity : ethnicities) {
            switch (ethnicity.getPrimaryKey().getEthnicity()) {
                case HISPANIC_OR_LATINO:
                    total += this.geoJson.getHispanicVap();
                    break;
                case AMERICAN_INDIAN:
                    total += this.geoJson.getNativeVap();
                    break;
                case ASIAN:
                    total += this.geoJson.getAsianVap();
                    break;
                case BLACK_OR_AFRICAN_AMERICAN:
                    total += this.geoJson.getBlackVap();
                    break;
                case NATIVE_HAWAIIAN_AND_OTHER_PACIFIC:
                    total += this.geoJson.getHawaiianVap();
                    break;
                case WHITE:
                    total += this.geoJson.getWhiteVap();
                    break;
            }
        }
        return total;
    }
}
