package com.giants.domain;

import com.giants.enums.StateAbbreviation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PopAndVaps")
public class PopAndVap {

    private int id;
    private StateAbbreviation abbreviation;
    private int totalPop;
    private int totalVap;
    private int nativePop;
    private int nativeVap;
    private int asianPop;
    private int asianVap;
    private int blackPop;
    private int blackVap;
    private int hawaiianPop;
    private int hawaiianVap;
    private int whitePop;
    private int whiteVap;
    private int nativeWhitePop;
    private int nativeWhiteVap;
    private int asianWhitePop;
    private int asianWhiteVap;
    private int blackWhitePop;
    private int blackWhiteVap;
    private int nativeBlackPop;
    private int nativeBlackVap;
    private int hispanicPop;
    private int hispanicVap;
    private int otherPop;
    private int otherVap;
//    private List<GeoCoord> geoCoords;

    public PopAndVap() {

    }

    public PopAndVap(StateAbbreviation abbreviation, int totalPop, int totalVap, int nativePop, int nativeVap,
                   int asianPop, int asianVap, int blackPop, int blackVap, int hawaiianPop, int hawaiianVap,
                   int whitePop, int whiteVap, int nativeWhitePop, int nativeWhiteVap, int asianWhitePop,
                   int asianWhiteVap, int blackWhitePop, int blackWhiteVap, int nativeBlackPop, int nativeBlackVap,
                   int hispanicPop, int hispanicVap, int otherPop, int otherVap) {
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
    public int getTotalPop() {
        return totalPop;
    }

    public void setTotalPop(int totalPop) {
        this.totalPop = totalPop;
    }

    @Column(name = "totalVap")
    public int getTotalVap() {
        return totalVap;
    }

    public void setTotalVap(int totalVap) {
        this.totalVap = totalVap;
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

    @Column(name = "whitePop")
    public int getWhitePop() {
        return whitePop;
    }

    public void setWhitePop(int whitePop) {
        this.whitePop = whitePop;
    }

    @Column(name = "whiteVap")
    public int getWhiteVap() {
        return whiteVap;
    }

    public void setWhiteVap(int whiteVap) {
        this.whiteVap = whiteVap;
    }

    @Column(name = "nativeWhitePop")
    public int getNativeWhitePop() {
        return nativeWhitePop;
    }

    public void setNativeWhitePop(int nativeWhitePop) {
        this.nativeWhitePop = nativeWhitePop;
    }

    @Column(name = "nativeWhiteVap")
    public int getNativeWhiteVap() {
        return nativeWhiteVap;
    }

    public void setNativeWhiteVap(int nativeWhiteVap) {
        this.nativeWhiteVap = nativeWhiteVap;
    }

    @Column(name = "asianWhitePop")
    public int getAsianWhitePop() {
        return asianWhitePop;
    }

    public void setAsianWhitePop(int asianWhitePop) {
        this.asianWhitePop = asianWhitePop;
    }

    @Column(name = "asianWhiteVap")
    public int getAsianWhiteVap() {
        return asianWhiteVap;
    }

    public void setAsianWhiteVap(int asianWhiteVap) {
        this.asianWhiteVap = asianWhiteVap;
    }

    @Column(name = "blackWhitePop")
    public int getBlackWhitePop() {
        return blackWhitePop;
    }

    public void setBlackWhitePop(int blackWhitePop) {
        this.blackWhitePop = blackWhitePop;
    }

    @Column(name = "blackWhiteVap")
    public int getBlackWhiteVap() {
        return blackWhiteVap;
    }

    public void setBlackWhiteVap(int blackWhiteVap) {
        this.blackWhiteVap = blackWhiteVap;
    }

    @Column(name = "nativeBlackPop")
    public int getNativeBlackPop() {
        return nativeBlackPop;
    }

    public void setNativeBlackPop(int nativeBlackPop) {
        this.nativeBlackPop = nativeBlackPop;
    }

    @Column(name = "nativeBlackVap")
    public int getNativeBlackVap() {
        return nativeBlackVap;
    }

    public void setNativeBlackVap(int nativeBlackVap) {
        this.nativeBlackVap = nativeBlackVap;
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

    @Column(name = "otherPop")
    public int getOtherPop() {
        return otherPop;
    }

    public void setOtherPop(int otherPop) {
        this.otherPop = otherPop;
    }

    @Column(name = "otherVap")
    public int getOtherVap() {
        return otherVap;
    }

    public void setOtherVap(int otherVap) {
        this.otherVap = otherVap;
    }

}
