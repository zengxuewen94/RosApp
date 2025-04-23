package com.zhh.slam.utils;


import android.graphics.Point;
import android.graphics.PointF;

import com.zhh.slam.bean.Size;


final public class PointTranslateUtil {

    public static PointF translate(com.zhh.slam.bean.PointF pt) {
        return new PointF(pt.getX(), pt.getY());
    }

    public static com.zhh.slam.bean.PointF translate(PointF pt) {
        return new com.zhh.slam.bean.PointF(pt.x, pt.y);
    }

    public static Point translate(Size sz) {
        return new Point(sz.getWidth(), sz.getHeight());
    }

}
