package com.zhh.slam.robot;


public class LaserPoint {
    private float distance;
    private float angle;
    private boolean valid;

    public LaserPoint() {
        this.distance = this.angle = 0.0F;
        this.valid = false;
    }

    public LaserPoint(float distance, float angle) {
        this.distance = distance;
        this.angle = angle;
        this.valid = true;
    }

    public LaserPoint(float distance, float angle, boolean valid) {
        this.distance = distance;
        this.angle = angle;
        this.valid = valid;
    }

    public LaserPoint(LaserPoint rhs) {
        this.distance = rhs.distance;
        this.angle = rhs.angle;
        this.valid = rhs.valid;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
