package com.giants.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoxWhiskers {
    private List<List<Float>> boxWhisker;

    public void generateBox(List<State> states) {
    }
}
