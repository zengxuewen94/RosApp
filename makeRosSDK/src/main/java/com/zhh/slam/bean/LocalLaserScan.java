package com.zhh.slam.bean;

import com.zhh.slam.robot.LaserPoint;

import java.util.Iterator;
import java.util.Vector;


public class LocalLaserScan {
    private Vector<LaserPoint> laserPoints = new Vector();
    private LocalPose pose;

    public LocalLaserScan() {
        this.pose = null;
    }

    public LocalLaserScan(Vector<LaserPoint> laserPoints) {
        this.pose = null;
        this.copyPoint(laserPoints);
    }

    public LocalLaserScan(Vector<LaserPoint> laserPoints, LocalPose pose) {
        this.pose = pose;
        this.copyPoint(laserPoints);
    }

    public LocalLaserScan(LocalLaserScan rhs) {
        this.pose = new LocalPose(rhs.pose);
        this.copyPoint(rhs.laserPoints);
    }

    private void copyPoint(Vector<LaserPoint> laserPoints) {
        Iterator var2 = laserPoints.iterator();

        while(var2.hasNext()) {
            LaserPoint pt = (LaserPoint)var2.next();
            this.laserPoints.add(new LaserPoint(pt));
        }

    }

    public Vector<LaserPoint> getLaserPoints() {
        return this.laserPoints;
    }

    public void setLaserPoints(Vector<LaserPoint> laserPoints) {
        this.laserPoints = new Vector();
        this.copyPoint(laserPoints);
    }

    public LocalPose getPose() {
        return this.pose;
    }

    public void setPose(LocalPose pose) {
        this.pose = new LocalPose(pose);
    }
}
