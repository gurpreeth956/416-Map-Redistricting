package domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class District {
    private int id;
    private int stateId;
    private float compactness;
    private int numberOfCounties;
    private GeoJSON geoJson;

}
