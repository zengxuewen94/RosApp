package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "homer_mapnav_msgs/LoadMap")
public class LoadMapResponse extends Message {
    public boolean result;
}