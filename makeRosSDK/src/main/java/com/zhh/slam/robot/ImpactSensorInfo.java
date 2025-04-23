package com.zhh.slam.robot;

import com.zhh.slam.bean.LocalPose;


public class ImpactSensorInfo {
    private int sensorId;
    private LocalPose pose;
    private ImpactSensorType type;
    private SensorType coreSensorType;
    private float refreshFreq;

    public ImpactSensorInfo(int sensorId, LocalPose pose, ImpactSensorType type, SensorType coreSensorType, float refreshFreq) {
        this.sensorId = sensorId;
        this.pose = pose;
        this.type = type;
        this.coreSensorType = coreSensorType;
        this.refreshFreq = refreshFreq;
    }

    public int getSensorId() {
        return this.sensorId;
    }

    public LocalPose getPose() {
        return this.pose;
    }

    public ImpactSensorType getType() {
        return this.type;
    }

    public SensorType getKind() {
        return this.coreSensorType;
    }

    public float getRefreshFreq() {
        return this.refreshFreq;
    }
}