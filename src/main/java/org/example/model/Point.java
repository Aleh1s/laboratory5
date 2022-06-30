package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Point {

    private double latitude; // широта
    private double longitude; // довгота

    @Override
    public String toString() {
        return "MapPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
