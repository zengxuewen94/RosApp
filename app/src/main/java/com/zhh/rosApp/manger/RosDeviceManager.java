package com.zhh.rosApp.manger;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zhh.androidrosbridgeclient.RosBridgeClientManager;
import com.zhh.androidrosbridgeclient.ros.ROSClient;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Pose;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseStamped;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Quaternion;
import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.OccupancyGrid;
import com.zhh.androidrosbridgeclient.ros.message.sensor_msgs.LaserScan;
import com.zhh.slam.bean.LocalPose;
import com.zhh.slam.bean.Location;
import com.zhh.slam.bean.Path;
import com.zhh.slam.bean.Rotation;

import java.util.Vector;


public enum RosDeviceManager {

    /**
     * 单列
     */
    INSTANCE;
    public String TAG = RosDeviceManager.class.getSimpleName();
    public String ip;
    private final RosBridgeClientManager manager = RosBridgeClientManager.INSTANCE;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 连接底盘
     *
     * @param uri 连接底盘的uri  "ws://192.168.31.119:9090"
     * @return 底盘连接结果
     */
    public synchronized boolean connect(String uri) {
        this.ip = uri;
        try {
            return manager.connect(uri, connectionStatusListener);
        } catch (Exception e) {
            onErrorInfo(e);
        }
        return false;
    }


    /**
     * @return 机器所在地图位姿信息
     */
    public LocalPose getPose() {
        LocalPose pose = null;
        try {
            Pose temp = manager.getPose();
            if (temp != null) {
                Location location = new Location();
                location.setX((float) temp.position.x);
                location.setY((float) temp.position.y);
                Rotation rotation = new Rotation();
                rotation.setYaw((float) quaternion2Yaw(temp.orientation));
                pose = new LocalPose(location, rotation);
            }
        } catch (Exception e) {
            onErrorInfo(e);

        }
        return pose;
    }


    /**
     * @return 充点电位姿信息
     */
    public LocalPose getHomePose() {
        LocalPose localPose = null;
        PoseStamped poseStamped = manager.getHomePose();
        if (null != poseStamped) {
            Pose pose = poseStamped.pose;
            if (pose != null) {
                Location location = new Location();
                location.setX((float) pose.position.x);
                location.setY((float) pose.position.y);
                Rotation rotation = new Rotation();
                rotation.setYaw((float) quaternion2Yaw(pose.orientation));
                localPose = new LocalPose(location, rotation);
            }
        }
        return localPose;
    }


    /**
     * @return 激光雷达数据
     */
    public LaserScan getLaserScan() {
        return manager.getLaserScanData();
    }

    /**
     * 路径规划
     *
     * @return
     */
    public Path getPath() {
        Path localPath = null;
        com.zhh.androidrosbridgeclient.ros.message.nav_msgs.Path path = manager.getLocalPlan();
        if (path != null) {
            localPath = new Path();
            Vector<Location> locationVector = new Vector<>();
            PoseStamped[] poseStampedArray = path.poses;
            for (PoseStamped poseStamped : poseStampedArray) {
                Pose pose = poseStamped.pose;
                if (pose != null) {
                    Location location = new Location();
                    location.setX((float) pose.position.x);
                    location.setY((float) pose.position.y);
                    locationVector.add(location);
                }
            }
            localPath.setPoints(locationVector);
        }
        return localPath;
    }


    /**
     * @return 地图原始数据
     */
    public synchronized OccupancyGrid getMapData() {
        return manager.getMapData();
    }


    /**
     * 地图数据获取的订阅服务开启
     */
    public void subscribeGetMapDataTopic() {
        manager.subscribeGetMapDataTopic();
    }

    /**
     * 地图数据获取的订阅服务关闭
     */
    public void unsubscribeGetMapDataTopic() {
        manager.unsubscribeGetMapDataTopic();
    }


    //订阅激光雷达数据
    public void subscribeLaserScanData() {
        manager.subscribeLaserScanData();
    }


    public void subscribeGlobalLine() {
        manager.pathLineTopic();
    }

    public void subscribeLocalLine() {
        manager.localPlanTopic();
    }


    /**
     * 四元数转为欧拉角
     *
     * @param quaternion
     * @return
     */
    public double quaternion2Yaw(Quaternion quaternion) {
        double siny_cosp = 2 * (quaternion.w * quaternion.z + quaternion.x * quaternion.y);
        double cosy_cosp = 1 - 2 * (quaternion.y * quaternion.y + quaternion.z * quaternion.z);
        double yaw = Math.atan2(siny_cosp, cosy_cosp);
        return yaw;
    }

    /**
     * 错误信息处理
     *
     * @param e 错误信息
     */
    public void onErrorInfo(Exception e) {
    }


    /**
     * 关闭连接
     */
    public void closeConnect() {
        try {
            manager.disConnect();
        } catch (Exception e) {
            onErrorInfo(e);
        }

    }

    ROSClient.ConnectionStatusListener connectionStatusListener = new ROSClient.ConnectionStatusListener() {
        @Override
        public void onConnect() {
            Log.d("Ros", "onConnect:连接成功");
        }

        @Override
        public void onDisconnect(boolean normal, String reason, int code) {
            Log.d("Ros", "onDisconnect:连接失败" + reason + code);
        }

        @Override
        public void onError(Exception ex) {
            Log.d("Ros", "onError:连接失败：" + ex.getMessage());
        }
    };

}
