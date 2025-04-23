package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.MapInfo;

@MessageType(string = "homer_mapnav_msgs/GetMapList")
public class GetMapListResponse extends Message {
    public boolean success; //默认为true
    public String message; //默认为空
    public MapInfo[] map_ids; //地图名
    public String current_map_name;//当前地图名
    public String default_map_name;//默认加载地图名
    //public boolean result;
}