package com.zhh.androidrosbridgeclient.ros.message.os_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "os_msgs/ResMsg")
public class ResMsg extends Message {

    public boolean success;
    public String status_message;

}
