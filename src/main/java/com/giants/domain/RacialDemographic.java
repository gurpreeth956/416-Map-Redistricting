package com.giants.domain;

import com.giants.enums.Ethnicity;

public class RacialDemographic {
    private Ethnicity ethnicity;
    private int population;
    private int votingAgePopulation;

    public Ethnicity getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getVotingAgePopulation() {
        return votingAgePopulation;
    }

    public void setVotingAgePopulation(int votingAgePopulation) {
        this.votingAgePopulation = votingAgePopulation;
    }
}
