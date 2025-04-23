package com.zhh.androidrosbridgeclient.ros.message.move_base_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseStamped;

@MessageType(string = "move_base_msgs/MoveBaseActionGoal")
public class MoveBaseGoal extends Message {
public PoseStamped target_pose;//目标点位置信息
}
