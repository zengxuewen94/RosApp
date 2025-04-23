package com.zhh.slam.robot;


public enum SensorType {
    Unknown,
    Bumper,
    Cliff,
    Sonar,
    DepthCamera,
    WallSensor,
    MagTapeDetector;

    private SensorType() {
    }
}
