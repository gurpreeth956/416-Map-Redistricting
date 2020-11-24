package com.giants.domain;

import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GeoJsons")
public class GeoJSON {

    private int id;
    private StateAbbreviation abbreviation;
    private double totalPop;
    private double totalVap;
    private double nativePop;
    private double nativeVap;
    private double asianPop;
    private double asianVap;
    private double blackPop;
    private double blackVap;
    private double hawaiianPop;
    private double hawaiianVap;
    private double whitePop;
    private double whiteVap;
    private double nativeWhitePop;
    private double nativeWhiteVap;
    private double asianWhitePop;
    private double asianWhiteVap;
    private double blackWhitePop;
    private double blackWhiteVap;
    private double nativeBlackPop;
    private double nativeBlackVap;
    private double hispanicPop;
    private double hispanicVap;
    private double otherPop;
    private double otherVap;
//    private List<GeoCoord> geoCoords;

    public GeoJSON() {

    }

    public GeoJSON(StateAbbreviation abbreviation, double totalPop, double totalVap, double nativePop, double nativeVap,
                   double asianPop, double asianVap, double blackPop, double blackVap, double hawaiianPop, double hawaiianVap,
                   double whitePop, double whiteVap, double nativeWhitePop, double nativeWhiteVap, double asianWhitePop,
                   double asianWhiteVap, double blackWhitePop, double blackWhiteVap, double nativeBlackPop, double nativeBlackVap,
                   double hispanicPop, double hispanicVap, double otherPop, double otherVap) {
        this.abbreviation = abbreviation;
        this.totalPop = totalPop;
        this.totalVap = totalVap;
        this.nativePop = nativePop;
        this.nativeVap = nativeVap;
        this.asianPop = asianPop;
        this.asianVap = asianVap;
        this.blackPop = blackPop;
        this.blackVap = blackVap;
        this.hawaiianPop = hawaiianPop;
        this.hawaiianVap = hawaiianVap;
        this.whitePop = whitePop;
        this.whiteVap = whiteVap;
        this.nativeWhitePop = nativeWhitePop;
        this.nativeWhiteVap = nativeWhiteVap;
        this.asianWhitePop = asianWhitePop;
        this.asianWhiteVap = asianWhiteVap;
        this.blackWhitePop = blackWhitePop;
        this.blackWhiteVap = blackWhiteVap;
        this.nativeBlackPop = nativeBlackPop;
        this.nativeBlackVap = nativeBlackVap;
        this.hispanicPop = hispanicPop;
        this.hispanicVap = hispanicVap;
        this.otherPop = otherPop;
        this.otherVap = otherVap;
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

    @Column(name = "totalPop")
    public double getTotalPop() {
        return totalPop;
    }

    public void setTotalPop(double totalPop) {
        this.totalPop = totalPop;
    }

    @Column(name = "totalVap")
    public double getTotalVap() {
        return totalVap;
    }

    public void setTotalVap(double totalVap) {
        this.totalVap = totalVap;
    }

    @Column(name = "nativePop")
    public double getNativePop() {
        return nativePop;
    }

    public void setNativePop(double nativePop) {
        this.nativePop = nativePop;
    }

    @Column(name = "nativeVap")
    public double getNativeVap() {
        return nativeVap;
    }

    public void setNativeVap(double nativeVap) {
        this.nativeVap = nativeVap;
    }

    @Column(name = "asianPop")
    public double getAsianPop() {
        return asianPop;
    }

    public void setAsianPop(double asianPop) {
        this.asianPop = asianPop;
    }

    @Column(name = "asianVap")
    public double getAsianVap() {
        return asianVap;
    }

    public void setAsianVap(double asianVap) {
        this.asianVap = asianVap;
    }

    @Column(name = "blackPop")
    public double getBlackPop() {
        return blackPop;
    }

    public void setBlackPop(double blackPop) {
        this.blackPop = blackPop;
    }

    @Column(name = "blackVap")
    public double getBlackVap() {
        return blackVap;
    }

    public void setBlackVap(double blackVap) {
        this.blackVap = blackVap;
    }

    @Column(name = "hawaiianPop")
    public double getHawaiianPop() {
        return hawaiianPop;
    }

    public void setHawaiianPop(double hawaiianPop) {
        this.hawaiianPop = hawaiianPop;
    }

    @Column(name = "hawaiianVap")
    public double getHawaiianVap() {
        return hawaiianVap;
    }

    public void setHawaiianVap(double hawaiianVap) {
        this.hawaiianVap = hawaiianVap;
    }

    @Column(name = "whitePop")
    public double getWhitePop() {
        return whitePop;
    }

    public void setWhitePop(double whitePop) {
        this.whitePop = whitePop;
    }

    @Column(name = "whiteVap")
    public double getWhiteVap() {
        return whiteVap;
    }

    public void setWhiteVap(double whiteVap) {
        this.whiteVap = whiteVap;
    }

    @Column(name = "nativeWhitePop")
    public double getNativeWhitePop() {
        return nativeWhitePop;
    }

    public void setNativeWhitePop(double nativeWhitePop) {
        this.nativeWhitePop = nativeWhitePop;
    }

    @Column(name = "nativeWhiteVap")
    public double getNativeWhiteVap() {
        return nativeWhiteVap;
    }

    public void setNativeWhiteVap(double nativeWhiteVap) {
        this.nativeWhiteVap = nativeWhiteVap;
    }

    @Column(name = "asianWhitePop")
    public double getAsianWhitePop() {
        return asianWhitePop;
    }

    public void setAsianWhitePop(double asianWhitePop) {
        this.asianWhitePop = asianWhitePop;
    }

    @Column(name = "asianWhiteVap")
    public double getAsianWhiteVap() {
        return asianWhiteVap;
    }

    public void setAsianWhiteVap(double asianWhiteVap) {
        this.asianWhiteVap = asianWhiteVap;
    }

    @Column(name = "blackWhitePop")
    public double getBlackWhitePop() {
        return blackWhitePop;
    }

    public void setBlackWhitePop(double blackWhitePop) {
        this.blackWhitePop = blackWhitePop;
    }

    @Column(name = "blackWhiteVap")
    public double getBlackWhiteVap() {
        return blackWhiteVap;
    }

    public void setBlackWhiteVap(double blackWhiteVap) {
        this.blackWhiteVap = blackWhiteVap;
    }

    @Column(name = "nativeBlackPop")
    public double getNativeBlackPop() {
        return nativeBlackPop;
    }

    public void setNativeBlackPop(double nativeBlackPop) {
        this.nativeBlackPop = nativeBlackPop;
    }

    @Column(name = "nativeBlackVap")
    public double getNativeBlackVap() {
        return nativeBlackVap;
    }

    public void setNativeBlackVap(double nativeBlackVap) {
        this.nativeBlackVap = nativeBlackVap;
    }

    @Column(name = "hispanicPop")
    public double getHispanicPop() {
        return hispanicPop;
    }

    public void setHispanicPop(double hispanicPop) {
        this.hispanicPop = hispanicPop;
    }

    @Column(name = "hispanicVap")
    public double getHispanicVap() {
        return hispanicVap;
    }

    public void setHispanicVap(double hispanicVap) {
        this.hispanicVap = hispanicVap;
    }

    @Column(name = "otherPop")
    public double getOtherPop() {
        return otherPop;
    }

    public void setOtherPop(double otherPop) {
        this.otherPop = otherPop;
    }

    @Column(name = "otherVap")
    public double getOtherVap() {
        return otherVap;
    }

    public void setOtherVap(double otherVap) {
        this.otherVap = otherVap;
    }

    //    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "geoJsonId", referencedColumnName = "id")
//    public List<GeoCoord> getGeoCoords() {
//        return geoCoords;
//    }
//
//    public void setGeoCoords(List<GeoCoord> geoCoords) {
//        this.geoCoords = geoCoords;
//    }
}
