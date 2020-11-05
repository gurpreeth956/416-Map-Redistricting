package domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Precinct {
    private int id;
    private int districtId;
    private int countyId;
    private GeoJSON geoJson;
}
