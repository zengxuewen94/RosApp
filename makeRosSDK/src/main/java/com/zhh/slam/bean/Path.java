package com.zhh.slam.bean;


import java.util.Iterator;
import java.util.Vector;


public class Path {
    private Vector<Location> points = new Vector();

    public Path() {
    }

    public Path(Vector<Location> points) {
        this.copyLocations(points);
    }

    public Path(Path path) {
        this.copyLocations(path.points);
    }

    public Vector<Location> getPoints() {
        return this.points;
    }

    public void setPoints(Vector<Location> points) {
        this.points = new Vector();
        this.copyLocations(points);
    }

    private void copyLocations(Vector<Location> points) {
        Iterator var2 = points.iterator();

        while(var2.hasNext()) {
            Location loc = (Location) var2.next();
            this.points.add(loc);
        }

    }
}