package com.giants.domain;

public class GeoJSON {
    private int population;
    // IDK what WKT is ask Baloo for info
    // WKT stands for Well Known Text representing as String for now
    // Might need parser to convert? https://locationtech.github.io/jts/javadoc/org/locationtech/jts/io/WKTReader.html
    private String coordinates;
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

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getBlackPop() {
        return blackPop;
    }

    public void setBlackPop(int blackPop) {
        this.blackPop = blackPop;
    }

    public int getBlackVap() {
        return blackVap;
    }

    public void setBlackVap(int blackVap) {
        this.blackVap = blackVap;
    }

    public int getAsianPop() {
        return asianPop;
    }

    public void setAsianPop(int asianPop) {
        this.asianPop = asianPop;
    }

    public int getAsianVap() {
        return asianVap;
    }

    public void setAsianVap(int asianVap) {
        this.asianVap = asianVap;
    }

    public int getNativePop() {
        return nativePop;
    }

    public void setNativePop(int nativePop) {
        this.nativePop = nativePop;
    }

    public int getNativeVap() {
        return nativeVap;
    }

    public void setNativeVap(int nativeVap) {
        this.nativeVap = nativeVap;
    }

    public int getHawaiianPop() {
        return hawaiianPop;
    }

    public void setHawaiianPop(int hawaiianPop) {
        this.hawaiianPop = hawaiianPop;
    }

    public int getHawaiianVap() {
        return hawaiianVap;
    }

    public void setHawaiianVap(int hawaiianVap) {
        this.hawaiianVap = hawaiianVap;
    }

    public int getHispanicPop() {
        return hispanicPop;
    }

    public void setHispanicPop(int hispanicPop) {
        this.hispanicPop = hispanicPop;
    }

    public int getHispanicVap() {
        return hispanicVap;
    }

    public void setHispanicVap(int hispanicVap) {
        this.hispanicVap = hispanicVap;
    }
}
