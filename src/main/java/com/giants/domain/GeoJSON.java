package com.giants.domain;

import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GeoJsons")
public class GeoJSON {

    private int id;
    private StateAbbreviation abbreviation;
    private int population;
    private int blackPop;
    private int blackVap;
    private int asianPop;
    private int asianVap;
    private int nativePop;
    private int nativeVap;
    private int hawaiianPop;
    private int hawaiianVap;
    private int hispanicPop;
    private int hispanicVap;
    private List<GeoCoord> geoCoords;

    public GeoJSON() {

    }

    public GeoJSON(StateAbbreviation abbreviation, int population, int blackPop, int blackVap, int asianPop,
                   int asianVap, int nativePop, int nativeVap, int hawaiianPop, int hawaiianVap, int hispanicPop,
                   int hispanicVap) {
        this.abbreviation = abbreviation;
        this.population = population;
        this.blackPop = blackPop;
        this.blackVap = blackVap;
        this.asianPop = asianPop;
        this.asianVap = asianVap;
        this.nativePop = nativePop;
        this.nativeVap = nativeVap;
        this.hawaiianPop = hawaiianPop;
        this.hawaiianVap = hawaiianVap;
        this.hispanicPop = hispanicPop;
        this.hispanicVap = hispanicVap;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "population")
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Column(name = "blackPop")
    public int getBlackPop() {
        return blackPop;
    }

    public void setBlackPop(int blackPop) {
        this.blackPop = blackPop;
    }

    @Column(name = "blackVap")
    public int getBlackVap() {
        return blackVap;
    }

    public void setBlackVap(int blackVap) {
        this.blackVap = blackVap;
    }

    @Column(name = "asianPop")
    public int getAsianPop() {
        return asianPop;
    }

    public void setAsianPop(int asianPop) {
        this.asianPop = asianPop;
    }

    @Column(name = "asianVap")
    public int getAsianVap() {
        return asianVap;
    }

    public void setAsianVap(int asianVap) {
        this.asianVap = asianVap;
    }

    @Column(name = "nativePop")
    public int getNativePop() {
        return nativePop;
    }

    public void setNativePop(int nativePop) {
        this.nativePop = nativePop;
    }

    @Column(name = "nativeVap")
    public int getNativeVap() {
        return nativeVap;
    }

    public void setNativeVap(int nativeVap) {
        this.nativeVap = nativeVap;
    }

    @Column(name = "hawaiianPop")
    public int getHawaiianPop() {
        return hawaiianPop;
    }

    public void setHawaiianPop(int hawaiianPop) {
        this.hawaiianPop = hawaiianPop;
    }

    @Column(name = "hawaiianVap")
    public int getHawaiianVap() {
        return hawaiianVap;
    }

    public void setHawaiianVap(int hawaiianVap) {
        this.hawaiianVap = hawaiianVap;
    }

    @Column(name = "hispanicPop")
    public int getHispanicPop() {
        return hispanicPop;
    }

    public void setHispanicPop(int hispanicPop) {
        this.hispanicPop = hispanicPop;
    }

    @Column(name = "hispanicVap")
    public int getHispanicVap() {
        return hispanicVap;
    }

    public void setHispanicVap(int hispanicVap) {
        this.hispanicVap = hispanicVap;
    }

    @OneToMany(mappedBy = "geoJson", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GeoCoord> getGeoCoords() {
        return geoCoords;
    }

    public void setGeoCoords(List<GeoCoord> geoCoords) {
        this.geoCoords = geoCoords;
    }
}
