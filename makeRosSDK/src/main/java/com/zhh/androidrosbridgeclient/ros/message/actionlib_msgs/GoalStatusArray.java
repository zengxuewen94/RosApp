package com.zhh.androidrosbridgeclient.ros.message.actionlib_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "actionlib_msgs/GoalStatusArray")
public class GoalStatusArray extends Message {
  public Header header;//可不指定坐标系
  public GoalStatus[] status_list;//导航状态列表
}
