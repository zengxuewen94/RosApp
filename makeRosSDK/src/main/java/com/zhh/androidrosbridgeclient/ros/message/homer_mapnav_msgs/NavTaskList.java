package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "homer_mapnav_msgs/NavTaskList")
public class NavTaskList extends Message {
    public NavTask[] tasks;
}