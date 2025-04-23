package com.zhh.androidrosbridgeclient.ros.message.sensor_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "sensor_msgs/Range")
public class Range extends Message {
  static final byte ULTRASOUND=0;
  static final byte INFRARED=1;
  public Header header;//header中的时间戳是range，返回距离读数的时间
  byte radiation_type;//传感器使用的辐射类型
  float field_of_view;//距离读数的弧的大小
  float min_range; //最小范围值 [m]
  float max_range;//最大范围值 [m]
  float range;//range data [m]当前范围值
  }