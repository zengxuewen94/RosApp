package com.zhh.androidrosbridgeclient.ros.message.nav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.Time;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Pose;

@MessageType(string = "nav_msgs/MapMetaData")
public class MapMetaData extends Message {
public Time map_load_time;
public float resolution;
public int width;//地图宽度
public int height;//地图高度
public Pose origin;
// public String name;//地图名
}