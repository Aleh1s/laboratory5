package org.example.model;

public class MapPoint {

    private float latitude; // широта
    private float longitude; // довгота

    public MapPoint(
            float latitude,
            float longitude
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
