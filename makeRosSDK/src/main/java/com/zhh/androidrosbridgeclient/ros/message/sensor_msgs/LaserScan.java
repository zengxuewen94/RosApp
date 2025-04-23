package com.zhh.androidrosbridgeclient.ros.message.sensor_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "sensor_msgs/LaserScan")
public class LaserScan extends Message {
  public Header header;//是一个结构体，包含seq、stamp、frame—id。seq扫描顺序增加的id序列，stamp激光数据的时间戳，frame-id是扫描数据的名字。
  public float angle_min;//开始扫描的角度
  public float angle_max;//结束扫描的角度
  public float angle_increment;//每次扫描增加的角度
  public float time_increment;//测量的时间间隔
  public float scan_time;//扫描的时间间隔
  public float range_min;//测距的最小值
  public float range_max;//测距的最大值
  public float[] ranges;//转一周的测量数据一共360个
  public float[] intensities;//与设备有关，强度数组长度360
  }
