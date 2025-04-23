package com.zhh.slam.bean;


public class Rotation {
    private float yaw;
    private float roll;
    private float pitch;

    public Rotation() {
        this.yaw = this.roll = this.pitch = 0.0F;
    }

    public Rotation(float yaw) {
        this.yaw = yaw;
        this.roll = this.pitch = 0.0F;
    }

    public Rotation(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.roll = roll;
        this.pitch = pitch;
    }

    public Rotation(Rotation rhs) {
        this.yaw = rhs.yaw;
        this.roll = rhs.roll;
        this.pitch = rhs.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return this.roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}

