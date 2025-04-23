package com.zhh.androidrosbridgeclient.ros.message.move_base_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.actionlib_msgs.GoalID;
import com.zhh.androidrosbridgeclient.ros.message.move_base_msgs.MoveBaseGoal;


@MessageType(string = "move_base_msgs/MoveBaseActionGoal")
public class MoveBaseActionGoal extends Message {
public Header header;//frame_id:坐标系
public GoalID goal_id;//对应任务id
public MoveBaseGoal goal;//当前前往的目标点位置信息
}
