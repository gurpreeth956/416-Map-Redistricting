package com.giants.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
