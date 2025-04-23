package com.zhh.androidrosbridgeclient.ros.message.std_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
/**
 * @author Administrator
 */
@MessageType(string = "std_msgs/UInt16")
public class UInt16 extends Message {
    public short data;
}