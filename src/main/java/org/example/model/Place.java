package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
public class Place {

    private String type; // shop
    private String subType; // car
    private String name; // Авто масло
    private String address; // ...
    private Point point;

    public static Place parse(String str) {
        str = str.replace(";", "; ");

        String[] split = str.split(";");
        return new Place(
                split[2].trim(),
                split[3].trim(),
                split[4].trim(),
                split[5].trim(),
                new Point(
                        Double.parseDouble(split[0].trim().replace(",", ".")),
                        Double.parseDouble(split[1].trim().replace(",", "."))
                )
        );
    }

    @Override
    public String toString() {
        return "MapPlace{" +
                "type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", point=" + point +
                '}';
    }
}
