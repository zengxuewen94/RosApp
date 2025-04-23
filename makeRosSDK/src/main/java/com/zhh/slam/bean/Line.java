package com.zhh.slam.bean;


public class Line {
    private int segmentId;
    private PointF startPoint;
    private PointF endPoint;

    public Line(int segmentId, PointF startPoint, PointF endPoint) {
        this.segmentId = segmentId;
        this.startPoint = new PointF(startPoint);
        this.endPoint = new PointF(endPoint);
    }

    public Line(int segmentId, float startX, float startY, float endX, float endY) {
        this.segmentId = segmentId;
        this.startPoint = new PointF(startX, startY);
        this.endPoint = new PointF(endX, endY);
    }

    public Line(Line line) {
        this.segmentId = line.segmentId;
        this.startPoint = new PointF(line.startPoint);
        this.endPoint = new PointF(line.endPoint);
    }

    public Line(PointF startP, PointF endP) {
        this.startPoint = startP;
        this.endPoint = endP;
    }

    public PointF getStartPoint() {
        return this.startPoint;
    }

    public void setStartPoint(PointF startPointF) {
        this.startPoint = new PointF(startPointF);
    }

    public PointF getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(PointF endPoint) {
        this.endPoint = new PointF(endPoint);
    }

    public float getStartX() {
        return this.getStartPoint().getX();
    }

    public float getStartY() {
        return this.getStartPoint().getY();
    }

    public float getEndX() {
        return this.getEndPoint().getX();
    }

    public float getEndY() {
        return this.getEndPoint().getY();
    }

    public int getSegmentId() {
        return this.segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }
}
