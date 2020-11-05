package com.giants.domain;

import com.giants.enums.Ethnicity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RacialDemographic {
    private Ethnicity ethnicity;
    private int population;
    private int votingAgePopulation;
}
