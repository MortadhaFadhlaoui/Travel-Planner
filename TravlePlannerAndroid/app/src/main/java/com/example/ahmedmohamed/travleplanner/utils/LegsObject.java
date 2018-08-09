package com.example.ahmedmohamed.travleplanner.utils;

import java.util.List;

/**
 * Created by Rym on 19/11/2017.
 */

public class LegsObject {
    private List<StepsObject> steps;
    public LegsObject(List<StepsObject> steps) {
        this.steps = steps;
    }
    public List<StepsObject> getSteps() {
        return steps;
    }
}
