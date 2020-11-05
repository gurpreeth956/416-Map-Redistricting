package domain;

import enums.Ethnicity;
import enums.JobStatus;
import enums.StateAbbreviation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Job {
    private int id;
    private JobStatus status;
    private StateAbbreviation abbreviation;
    private int userCompactness;
    private int populationDifferenceLimit;
    private int numberOfMaps;
    private int numberOfDistricts;
    private List<Ethnicity> ethnicities;
    private boolean onSeaWulf;
}
