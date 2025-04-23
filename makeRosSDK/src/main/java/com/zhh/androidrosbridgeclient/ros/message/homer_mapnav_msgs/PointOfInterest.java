package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Pose;

@MessageType(string = "homer_mapnav_msgs/PointOfInterest")
public class PointOfInterest extends Message {
    public int DEFAULT=100;
    public int OBJECT=300;
    public int GRIPPABLE_OBJECT=400;
    public int PERSON=600;
    public int START_POSITION=900;
    public int START_ORIENTATION=1000;
    public int type;
    public String name;//任务名称
    public String remarks;
    public long wait_time;//在每个点位的等待时间
    public Pose pose;//相对坐标
}


