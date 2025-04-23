package com.zhh.androidrosbridgeclient.ros.message.os_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.os_msgs.NProcess;
import com.zhh.androidrosbridgeclient.ros.rosbridge.indication.Indicator;


@MessageType(string = "os_msgs/OSRequestCmd")

public class OSRequestCmd extends Message {
  
  static final byte SYSTEM_OS_INVALID=-1;//无效命令

  static final byte SYSTEM_CMD_UNKNOWN=0;//指令未知状态
  static final byte SYSTEM_CMD_SHUTDOWN=1;//关机
  static final byte SYSTEM_CMD_REBOOT=2;//重启
  @Indicator
  public int system_cmd;//系统命令

  static final byte OS_NAV_STATUS_UNKNOWN=0; //状态未知
  static final byte OS_NAV_STATUS_IDLE=1;//导航空闲状态
  static final byte OS_NAV_STATUS_DASH=2;//底盘状态
  static final byte OS_NAV_STATUS_MAPPING=4;//建图状态
  static final byte OS_NAV_STATUS_NAV=8;//导航状态
  static final byte OS_NAV_STATUS_DOCKING=16;//回充状态
  static final byte OS_NAV_STATUS_GMCL = 32;//定位

  @Indicator
  public int os_status;//导航功能状态切换
  @Indicator
  public NProcess process;
}