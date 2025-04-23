package com.zhh.slam.bean;


public class PointF {
    private float x;
    private float y;

    public PointF() {
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF(PointF rhs) {
        this.x = rhs.x;
        this.y = rhs.y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

