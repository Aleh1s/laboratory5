package org.example.model;

public class MapPlace {

    private String type;
    private String subType;
    private String name;
    private String address;

    public MapPlace(
            String type,
            String subType,
            String name,
            String address
    ) {
        this.type = type;
        this.subType = subType;
        this.name = name;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
