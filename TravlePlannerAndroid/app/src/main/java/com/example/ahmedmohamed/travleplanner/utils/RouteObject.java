package com.example.ahmedmohamed.travleplanner.utils;

import java.util.List;

/**
 * Created by Rym on 19/11/2017.
 */

public class RouteObject {
    private List<LegsObject> legs;
    public RouteObject(List<LegsObject> legs) {
        this.legs = legs;
    }
    public List<LegsObject> getLegs() {
        return legs;
    }
}
