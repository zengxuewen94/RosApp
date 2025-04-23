package com.zhh.rosApp.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.MapMetaData;
import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.OccupancyGrid;
import com.zhh.androidrosbridgeclient.ros.message.sensor_msgs.LaserScan;
import com.zhh.rosApp.R;
import com.zhh.rosApp.databinding.ActivityMBinding;

import com.zhh.rosApp.manger.RosDeviceManager;
import com.zhh.rosApp.viewmodel.MainViewModel;
import com.zhh.slam.bean.LocalLaserScan;
import com.zhh.slam.bean.LocalPose;
import com.zhh.slam.bean.Path;
import com.zhh.slam.bean.PointF;
import com.zhh.slam.bean.Size;
import com.zhh.slam.robot.LaserPoint;
import com.zhh.slam.robot.Map;

import java.util.Vector;


public class MainActivity extends ComponentActivity {


    private MapMetaData mapMetaData = null;
    private ActivityMBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_m);
        MainViewModel mainViewModel= new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(mainViewModel);
        getLifecycle().addObserver(mainViewModel);
        observeViewModel(mainViewModel);
    }


    private void observeViewModel(MainViewModel mainViewModel) {
        mainViewModel.getObOccupancyGrid().observe(this, new Observer<OccupancyGrid>() {
            @Override
            public void onChanged(OccupancyGrid occupancyGrid) {
                if (mapMetaData == null) {//这里地图数据为空时，获取地图数据，只刷新一遍数据
                    mapMetaData = occupancyGrid.info;
                    setMapData(occupancyGrid);
                    //这段代码用于在地图上显示建立的地点
//                    List<SlamLocationBean> list = SlamLocationDBManager.getInstance(Utils.getContext())
//                            .queryLocationListByMap("测试地图");
//
//                    List<Location> mList = new ArrayList<>();
//                    for (SlamLocationBean slamLocationBean : list) {
//                        Location location = new Location();
//                        location.setX(slamLocationBean.getX());
//                        location.setY(slamLocationBean.getY());
//                        location.setZ(slamLocationBean.getYaw());
//                        location.setLocationName(slamLocationBean.getLocationNameChina());
//                        mList.add(location);
//                    }
//                    connectBinding.mapview.setSlamLocationBeans(mList);

                }

                // 获取机器的实时位姿
                LocalPose localPose = RosDeviceManager.INSTANCE.getPose();
                if (null != localPose) {
                    binding.ivMap.setRobotPose(localPose);
                    //雷达数据
                    LaserScan laserScan = RosDeviceManager.INSTANCE.getLaserScan();
                    //这里还需要对数据进行处理，
                    if (null != laserScan) {
                        float[] ranges = laserScan.ranges;
                        LocalLaserScan localLaserScan = new LocalLaserScan();
                        localLaserScan.setPose(localPose);
                        Vector<LaserPoint> laserPoints = new Vector<>();
                        for (int i = 0; i < ranges.length; i++) {
                            LaserPoint laserPoint = new LaserPoint();
                            laserPoint.setAngle(ranges[i]);
                            laserPoint.setDistance(i * laserScan.angle_increment + localPose.getRotation().getYaw() + laserScan.angle_min);
                            laserPoints.add(laserPoint);
                        }
                        localLaserScan.setLaserPoints(laserPoints);
                        binding.ivMap.setLaserScan(localLaserScan);
                    }

                }
                // 充电点位姿
                LocalPose homePose = RosDeviceManager.INSTANCE.getHomePose();
                if (null != homePose) {
                    binding.ivMap.setHomePose(homePose);
                }
                // 机器行走时路径规划显示
                Path path = RosDeviceManager.INSTANCE.getPath();
                if (null != path) {
                    binding.ivMap.setRemainingPath(path);
                }

            }
        });
    }


    /**
     * 设置地图数据
     *
     * @param occupancyGrid
     */
    private void setMapData(OccupancyGrid occupancyGrid) {
        Log.d("Ros:", "地图数据:" + mapMetaData.width);
        //这里进行数据转换，转成控件需要的地图数据
        PointF pointO = new PointF();
        pointO.setX((float) mapMetaData.origin.position.x);
        pointO.setY((float) mapMetaData.origin.position.y);
        Size size = new Size();
        //地图宽高
        size.setWidth(mapMetaData.width);
        size.setHeight(mapMetaData.height);
        //地图分辨率
        PointF pointR = new PointF();
        pointR.setY(mapMetaData.resolution);
        pointR.setX(mapMetaData.resolution);
        Map map = new Map(pointO, size, pointR, occupancyGrid.header.seq, occupancyGrid.data);
        binding.ivMap.setMap(map);
    }


}
