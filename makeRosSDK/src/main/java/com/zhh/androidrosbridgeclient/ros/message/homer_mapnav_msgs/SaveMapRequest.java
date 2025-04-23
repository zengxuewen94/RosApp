package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.std_msgs.DataString;
@MessageType(string = "homer_mapnav_msgs/SaveMap")
public class SaveMapRequest extends Message {
    public DataString folder;//地图名称
}