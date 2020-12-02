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
    private PopAndVap popAndVap;

    public Precinct() {

    }

    public Precinct(String id, StateAbbreviation abbreviation, int countyId) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.countyId = countyId;
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "popAndVapId", referencedColumnName = "id")
    public PopAndVap getPopAndVap() {
        return popAndVap;
    }

    public void setPopAndVap(PopAndVap popAndVap) {
        this.popAndVap = popAndVap;
    }

    public int getSpecificPop(List<Ethnicity> ethnicities) {
        int total = 0;
        boolean containsWhite = false;
        boolean containsAsian = false;
        boolean containsBlack = false;
        boolean containsNative = false;
        for (Ethnicity ethnicity : ethnicities) {
            switch (ethnicity.getPrimaryKey().getEthnicity()) {
                case HISPANIC_OR_LATINO:
                    total += this.popAndVap.getHispanicPop();
                    break;
                case AMERICAN_INDIAN:
                    total += this.popAndVap.getNativePop();
                    containsNative = true;
                    break;
                case ASIAN:
                    total += this.popAndVap.getAsianPop();
                    containsAsian = true;
                    break;
                case BLACK_OR_AFRICAN_AMERICAN:
                    total += this.popAndVap.getBlackPop();
                    containsBlack = true;
                    break;
                case NATIVE_HAWAIIAN_AND_OTHER_PACIFIC:
                    total += this.popAndVap.getHawaiianPop();
                    break;
                case WHITE:
                    total += this.popAndVap.getWhitePop();
                    containsWhite = true;
                    break;
            }
        }
        // Check for mixed ethnicities
        if (containsWhite) {
            if (containsAsian) total += this.popAndVap.getAsianWhitePop();
            if (containsBlack) total += this.popAndVap.getBlackWhitePop();
            if (containsNative) total += this.popAndVap.getNativeWhitePop();
        }
        if (containsBlack) {
            if (containsNative) total += this.popAndVap.getNativeBlackPop();
        }
        return total;
    }

    public int getSpecificVap(List<Ethnicity> ethnicities) {
        int total = 0;
        boolean containsWhite = false;
        boolean containsAsian = false;
        boolean containsBlack = false;
        boolean containsNative = false;
        for (Ethnicity ethnicity : ethnicities) {
            switch (ethnicity.getPrimaryKey().getEthnicity()) {
                case HISPANIC_OR_LATINO:
                    total += this.popAndVap.getHispanicVap();
                    break;
                case AMERICAN_INDIAN:
                    total += this.popAndVap.getNativeVap();
                    containsNative = true;
                    break;
                case ASIAN:
                    total += this.popAndVap.getAsianVap();
                    containsAsian = true;
                    break;
                case BLACK_OR_AFRICAN_AMERICAN:
                    total += this.popAndVap.getBlackVap();
                    containsBlack = true;
                    break;
                case NATIVE_HAWAIIAN_AND_OTHER_PACIFIC:
                    total += this.popAndVap.getHawaiianVap();
                    break;
                case WHITE:
                    total += this.popAndVap.getWhiteVap();
                    containsWhite = true;
                    break;
            }
        }
        // Check for mixed ethnicities
        if (containsWhite) {
            if (containsAsian) total += this.popAndVap.getAsianWhiteVap();
            if (containsBlack) total += this.popAndVap.getBlackWhiteVap();
            if (containsNative) total += this.popAndVap.getNativeWhiteVap();
        }
        if (containsBlack) {
            if (containsNative) total += this.popAndVap.getNativeBlackVap();
        }
        return total;
    }
}
