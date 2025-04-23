package com.zhh.androidrosbridgeclient.ros.message.map_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.Header;

@MessageType(string = "map_msgs/OccupancyGridUpdate")
public class OccupancyGridUpdate extends Message {
    public Header header;
    public int x;
    public int y;
    public int width;//代价地图宽度
    public int height;//代价地图高度
    public byte[] data;//地图原始数据
}