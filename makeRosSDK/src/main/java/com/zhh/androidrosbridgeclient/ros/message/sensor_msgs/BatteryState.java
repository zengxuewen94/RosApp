package com.zhh.androidrosbridgeclient.ros.message.sensor_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "sensor_msgs/BatteryState")
public class BatteryState extends Message {
  //充电状态
  static final byte POWER_SUPPLY_STATUS_UNKNOWN = 0;//未知
  static final byte POWER_SUPPLY_STATUS_CHARGING = 1;//充电中
  static final byte POWER_SUPPLY_STATUS_DISCHARGING = 2;//放电中
  static final byte POWER_SUPPLY_STATUS_NOT_CHARGING = 3;//没有充电
  static final byte POWER_SUPPLY_STATUS_FULL = 4;//电池电量已满
  //电源健康状态
  static final byte POWER_SUPPLY_HEALTH_UNKNOWN = 0;//未知
  static final byte POWER_SUPPLY_HEALTH_GOOD = 1;//电源健康
  static final byte POWER_SUPPLY_HEALTH_OVERHEAT = 2;//电源过热
  static final byte POWER_SUPPLY_HEALTH_DEAD = 3;//电源已损坏
  static final byte POWER_SUPPLY_HEALTH_OVERVOLTAGE = 4;//电压过大
  static final byte POWER_SUPPLY_HEALTH_UNSPEC_FAILURE = 5;//未知错误
  static final byte POWER_SUPPLY_HEALTH_COLD = 6;//电源过冷
  static final byte POWER_SUPPLY_HEALTH_WATCHDOG_TIMER_EXPIRE = 7;//看门狗定时器到期
  static final byte POWER_SUPPLY_HEALTH_SAFETY_TIMER_EXPIRE = 8;//安全运行时间到期
  //电源技术（化学）
  static final byte POWER_SUPPLY_TECHNOLOGY_UNKNOWN = 0;//未知
  static final byte POWER_SUPPLY_TECHNOLOGY_NIMH = 1;//镍氢电池
  static final byte POWER_SUPPLY_TECHNOLOGY_LION = 2;//锂电池
  static final byte POWER_SUPPLY_TECHNOLOGY_LIPO = 3;//锂聚合物电池
  static final byte POWER_SUPPLY_TECHNOLOGY_LIFE = 4;//锂铁电池
  static final byte POWER_SUPPLY_TECHNOLOGY_NICD = 5;//镍镉电池
  static final byte POWER_SUPPLY_TECHNOLOGY_LIMN = 6;//锂离子电池
  public Header header;//电池时间戳信息
  public float voltage;//电压（伏特）
  public float temperature;//温度
  public float current;//电流 放电时为负值（A）（如果未测量的NaN）
  public float charge;//当前的电源容量（以Ah计）
  public float capacity;//容量，Ah（上一次满容量）（如果未测量，NaN）
  public float design_capacity;//容量（设计容量）（如果未测量的NaN）
  public float percentage;//电量百分比在0到1的范围内（如果未测量的NaN）
  public byte power_supply_status;//充电状态，上面定义的值
  public byte power_supply_health;//电源健康状态， 上面定义的值
  public byte power_supply_technology;//电源技术（化学），上面定义的值
  public boolean present;//如果有电池，则为真
  public float[] cell_voltage;//电池组中每个电池的单个电池电压的阵列
  public float[] cell_temperature;//电池组中每个电池的单个电池温度的阵列
  public String location;//电池插入的位置。 （插槽号或插头）
  public String serial_number;//电池序列号的最佳近似值
}
