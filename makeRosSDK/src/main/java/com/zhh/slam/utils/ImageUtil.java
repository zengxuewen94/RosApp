package com.zhh.slam.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/**
 * 地图图片工具类
 */
public class ImageUtil {
    private final static String TAG = "ImageUtil";

    private ImageUtil() {
    }

    /**
     *
     * 根据地图数据创建返回bitmap，
     * 简单了解栅格地图，它将环境划分为若干个栅格，在每个栅格中会存储占据或高程信息。目前的栅格地图可以根据其栅格
     * 内存储数据的差异分为二维栅格地图、高程栅格地图与三维栅格地图。概率占据栅格地图中每个栅格都存储着概率化的占据信息。
     * 通过二维栅格单元的颜色表示地图的不同状态。
     * 栅格单元具有三种状态:占据(Occupied)， 空闲(Free) 和未知( Unknown)，占据表示障碍物区域，空闲表示无障碍物的开放区域，
     * 未知表示传感器没有探测的区域。
     * @param buffer
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createImage(byte[] buffer, int width, int height) {
        int[] rawData = new int[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            //未知区域
            if (buffer[i] == -1) {
                rawData[i] = 0xC0C0C0;
            }
            //障碍物-这里由ros数据底层定义100就是障碍物
            else if (buffer[i] == 100) {
                rawData[i] = 0x000000;
            }
            //0可通行-这里定义0
            else if (buffer[i] == 0) {
                rawData[i] = 0xffffff;
            }else {
                rawData[i] = 0x00ffffff;
            }

        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        return Bitmap.createBitmap(rawData, width, height, Bitmap.Config.RGB_565);
    }
}
