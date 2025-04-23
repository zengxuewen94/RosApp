package com.zhh.androidrosbridgeclient.ros.message.geometry_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "geometry_msgs/PoseStamped")
public class PoseStamped extends Message {
  public Header header;// frame_id 指定坐标系如map
  public Pose pose;//发布指定位置（回桩充电无须发布位置）
}
