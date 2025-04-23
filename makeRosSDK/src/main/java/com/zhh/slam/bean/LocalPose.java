package com.zhh.slam.bean;


public class LocalPose {
    private Location location;
    private Rotation rotation;

    public LocalPose() {
        this.location = new Location();
        this.rotation = new Rotation();
    }

    public LocalPose(Location loc, Rotation rot) {
        this.location = loc;
        this.rotation = rot;
    }

    public LocalPose(float x, float y, float z, float yaw, float roll, float pitch) {
        this.location = new Location(x, y, z);
        this.rotation = new Rotation(yaw, pitch, roll);
    }

    public LocalPose(LocalPose rhs) {
        this.location = new Location(rhs.location);
        this.rotation = new Rotation(rhs.rotation);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = new Location(location);
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = new Rotation(rotation);
    }

    public float getX() {
        return this.location.getX();
    }

    public void setX(float v) {
        this.location.setX(v);
    }

    public float getY() {
        return this.location.getY();
    }

    public void setY(float v) {
        this.location.setY(v);
    }

    public float getZ() {
        return this.location.getZ();
    }

    public void setZ(float v) {
        this.location.setZ(v);
    }

    public float getYaw() {
        return this.rotation.getYaw();
    }

    public void setYaw(float v) {
        this.rotation.setYaw(v);
    }

    public float getRoll() {
        return this.rotation.getRoll();
    }

    public void setRoll(float v) {
        this.rotation.setRoll(v);
    }

    public float getPitch() {
        return this.rotation.getPitch();
    }

    public void setPitch(float v) {
        this.rotation.setPitch(v);
    }
}
