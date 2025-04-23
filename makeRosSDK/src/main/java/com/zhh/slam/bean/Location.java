package com.zhh.slam.bean;


public class Location {
    private float x;
    private float y;
    private float z;
    private String locationName;
    public Location() {
        this.x = this.y = this.z = 0.0F;
    }

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(float x, float y, float z, String locationName) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.locationName = locationName;
    }

    public Location(Location rhs) {
        this.x = rhs.x;
        this.y = rhs.y;
        this.z = rhs.z;
    }

    public float distanceTo(Location that) {
        float dx = that.x - this.x;
        float dy = that.y - this.y;
        float dz = that.z - this.z;
        return (float)Math.sqrt((double)(dx * dx + dy * dy + dz * dz));
    }

    public float getX() {
        return this.x;
    }

    public void setX(float v) {
        this.x = v;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float v) {
        this.y = v;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float v) {
        this.z = v;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

