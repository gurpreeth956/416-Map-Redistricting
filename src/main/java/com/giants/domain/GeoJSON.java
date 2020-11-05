package com.giants.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeoJSON {
    private int population;
    // IDK what WKT is ask Baloo for info
    // private WKT coordinates;
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
