package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Rectangle {

    private double maxLatitude;
    private double minLatitude;
    private double maxLongitude;
    private double minLongitude;

    public Rectangle() {
        this.maxLatitude = 0.0;
        this.minLatitude = 0.0;
        this.maxLongitude = 0.0;
        this.minLongitude = 0.0;
    }

    public double countArea() {
        return (maxLatitude - minLatitude) * (maxLongitude - minLongitude);
    }

    public double countIncrease(Point point) {
        Rectangle newMbr = new Rectangle();

        newMbr.maxLatitude = Math.max(maxLatitude, point.getLatitude());
        newMbr.minLatitude = Math.min(minLatitude, point.getLongitude());
        newMbr.maxLongitude = Math.max(maxLongitude, point.getLongitude());
        newMbr.minLongitude = Math.min(minLongitude, point.getLongitude());

        return newMbr.countArea() / this.countArea();
    }

    public void initMBR(Rectangle leafMbr) {
        this.setMinLatitude(leafMbr.getMinLatitude());
        this.setMaxLatitude(leafMbr.getMaxLatitude());
        this.setMinLongitude(leafMbr.getMinLongitude());
        this.setMaxLongitude(leafMbr.getMaxLongitude());
    }

    public void initMBR(Point coordinates) {

        double latitude = coordinates.getLatitude();
        this.setMaxLatitude(latitude);
        this.setMinLatitude(latitude);

        double longitude = coordinates.getLongitude();
        this.setMaxLongitude(longitude);
        this.setMinLongitude(longitude);
    }

    public void increaseMBR(Point coordinates) {
        double latitude = coordinates.getLatitude();
        if (this.getMaxLatitude() < latitude) this.setMaxLatitude(latitude);
        if (this.getMinLatitude() > latitude) this.setMinLatitude(latitude);

        double longitude = coordinates.getLongitude();
        if (this.getMaxLongitude() < longitude) this.setMaxLongitude(longitude);
        if (this.getMinLongitude() > longitude) this.setMinLongitude(longitude);
    }

    public void increaseMBR(Rectangle leafMbr) {
        double maxLatitude = leafMbr.getMaxLatitude();
        if (this.getMaxLatitude() < maxLatitude) this.setMaxLatitude(maxLatitude);

        double minLatitude = leafMbr.getMinLatitude();
        if (this.getMinLatitude() > minLatitude) this.setMinLatitude(minLatitude);

        double maxLongitude = leafMbr.getMaxLongitude();
        if (this.getMaxLongitude() < maxLongitude) this.setMaxLongitude(maxLongitude);

        double minLongitude = leafMbr.getMinLongitude();
        if (this.getMinLongitude() > minLongitude) this.setMinLongitude(minLongitude);
    }

    public boolean isCrosses(Rectangle mbr) {
        Point minLeft = new Point(
                mbr.minLatitude,
                mbr.minLongitude
        );

        Point maxLeft = new Point(
                mbr.maxLatitude,
                mbr.minLongitude
        );

        Point minRight = new Point(
                mbr.minLatitude,
                mbr.maxLongitude
        );

        Point maxRight = new Point(
                mbr.maxLatitude,
                mbr.maxLongitude
        );

        if (this.liesInPlane(minLeft)) return true;
        if (this.liesInPlane(maxLeft)) return true;
        if (this.liesInPlane(minRight)) return true;
        if (this.liesInPlane(maxRight)) return true;

        return false;
    }

    public boolean liesInPlane(Point point) {
        return (minLatitude <= point.getLatitude() && point.getLatitude() <= maxLatitude)
                && (minLongitude <= point.getLongitude() && point.getLongitude() <= maxLongitude);
    }

    @Override
    public String toString() {
        return String.format(
                """
                        {
                            maxLatitude: %s,
                            minLatitude: %s,
                            maxLongitude: %s,
                            minLongitude: %s
                        }
                        """
        ,maxLatitude, minLatitude, maxLongitude, minLongitude);
    }
}
