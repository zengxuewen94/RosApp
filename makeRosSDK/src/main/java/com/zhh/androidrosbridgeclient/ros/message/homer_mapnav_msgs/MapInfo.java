package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.MapMetaData;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "homer_mapnav_msgs/MapInfo")
public class MapInfo extends Message {
    public MapMetaData info;
    public String name;//地图名
}