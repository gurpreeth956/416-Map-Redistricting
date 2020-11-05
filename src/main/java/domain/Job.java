package domain;

import enums.Ethnicity;
import enums.JobStatus;
import enums.StateAbbreviation;
import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Job {
    private int id;
    private JobStatus status;
    @NonNull
    private StateAbbreviation abbreviation;
    @NonNull
    private int userCompactness;
    @NonNull
    private int populationDifferenceLimit;
    @NonNull
    private int numberOfMaps;
    @NonNull
    private int numberOfDistricts;
    @NonNull
    private List<Ethnicity> ethnicities;
    private boolean onSeaWulf;
    private BoxWhiskers boxWhiskers;
    private State averageState;
    private State extremeState;

    public void executeSeaWulfJob() {

    }

    public void executeLocalJob() {

    }

    public String getSeaWulfData() {
        return "";
    }

    public boolean countCounties(String filePath) {
        return true;
    }

    public boolean generateJsonFile(String filePath) {
        return true;
    }

    public boolean generateBoxWhiskers(String filePath) {
        return true;
    }

    public boolean generateAvgExtDistrictingPlan(String filePath) {
        return true;
    }
}
