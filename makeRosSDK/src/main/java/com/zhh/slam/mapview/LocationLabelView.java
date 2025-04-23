package com.zhh.slam.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;


import com.zhh.slam.bean.Location;
import com.zhh.slam.utils.ListUtils;
import com.zhh.slam.utils.RadianUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 位置点
 */
public class LocationLabelView extends SlamwareBaseView {
    public int RADIUS_COLOR = 0xC098C0FE;
    private float mDeviceRadius = 0.18f;
    private Paint mPaintOrientation;
    private Paint mPaintWhite;
    private Paint mPaintLine;
    private List<Location> locationBeanList;

    public LocationLabelView(Context context, WeakReference<MapView> parent) {
        super(context, parent);
        locationBeanList = new ArrayList<>();

        mPaintOrientation = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOrientation.setColor(RADIUS_COLOR);

        mPaintWhite = new Paint();
        mPaintWhite.setColor(RADIUS_COLOR);
        mPaintWhite.setTextSize(30);
        mPaintWhite.setStrokeWidth(1f);

        mPaintLine = new Paint();
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(Color.GREEN); // 设置画笔颜色
        mPaintLine.setStrokeWidth(2f);    // 设置画笔宽度
    }


    public void setRadiusColor(int textColor) {
        this.RADIUS_COLOR = textColor;
    }

    public void setLocationList(List<Location> locationBeanList) {
        if (ListUtils.isNotEmpty(locationBeanList)) {
            this.locationBeanList = locationBeanList;
            postInvalidate();
        }
    }

    private float radius=100f;
    private float scaleFactor=0.9f;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < locationBeanList.size(); i++) {
            Location locationBean = locationBeanList.get(i);
            float x = locationBean.getX();
            float y = locationBean.getY();
            float yaw = locationBean.getZ();

            PointF center = mParent.get().mapCoordinateToWidthCoordinateF(x, y);
            float widthRadius = mParent.get().mapDistanceToWidthDistance(mDeviceRadius);


            PointF originalLeft = new PointF(center.x - widthRadius * 0.25f, center.y - widthRadius * 0.6f);
            PointF originalRight = new PointF(center.x - widthRadius * 0.25f, center.y + widthRadius * 0.6f);
            PointF originalTop = new PointF(center.x + widthRadius, center.y);
            PointF originalBottom = new PointF(center.x - widthRadius * 0.5f, center.y);

            Matrix matrix = new Matrix();
            matrix.postRotate(RadianUtil.toAngel(-yaw) + mRotation, center.x, center.y);
            float[] points = new float[]{originalTop.x, originalTop.y, originalLeft.x, originalLeft.y, originalBottom.x, originalBottom.y, originalRight.x, originalRight.y};
            matrix.mapPoints(points);

            Path path = new Path();
            path.moveTo(points[0], points[1]);
            path.lineTo(points[2], points[3]);
            path.lineTo(points[4], points[5]);
            path.lineTo(points[6], points[7]);
            path.lineTo(points[0], points[1]);
            canvas.drawPath(path, mPaintOrientation);
            Path textPath = new Path();
            textPath.moveTo(points[4], points[5]);
            textPath.lineTo(points[4], points[5] - 500);
            canvas.drawPath(path, mPaintOrientation);

            canvas.drawTextOnPath(locationBean.getLocationName(), textPath, 0f, 0f, mPaintWhite);

            if (i > 0) {
                Path linePath = new Path();
                Location previous = locationBeanList.get(i - 1);

                PointF previousCenter = mParent.get().mapCoordinateToWidthCoordinateF(previous.getX(), previous.getY());
                linePath.moveTo(previousCenter.x, previousCenter.y);
                linePath.lineTo(center.x, center.y);
                canvas.drawPath(linePath, mPaintLine);

            }


        }



//        for (int i = 0; i < locationBeanList.size(); i++) {
//            Location locationBean = locationBeanList.get(i);
//            float x = locationBean.getX();
//            float y = locationBean.getY();
//            float yaw = locationBean.getZ();
//
//            PointF center = mParent.get().mapCoordinateToWidthCoordinateF(x, y);
//            float widthRadius = mParent.get().mapDistanceToWidthDistance(mDeviceRadius);
//

//            PointF originalLeft = new PointF(center.x - widthRadius * 0.25f, center.y - widthRadius * 0.6f);
//            PointF originalRight = new PointF(center.x - widthRadius * 0.25f, center.y + widthRadius * 0.6f);
//            PointF originalTop = new PointF(center.x + widthRadius, center.y);
//            PointF originalBottom = new PointF(center.x - widthRadius * 0.5f, center.y);
//
//            Matrix matrix = new Matrix();
//            matrix.postRotate(RadianUtil.toAngel(-yaw) + mRotation, center.x, center.y);
//            float[] points = new float[]{originalTop.x, originalTop.y, originalLeft.x, originalLeft.y, originalBottom.x, originalBottom.y, originalRight.x, originalRight.y};
//            matrix.mapPoints(points);
//
//            Path path = new Path();
//            path.moveTo(points[0], points[1]);
//            path.lineTo(points[2], points[3]);
//            path.lineTo(points[4], points[5]);
//            path.lineTo(points[6], points[7]);
//            path.lineTo(points[0], points[1]);
//            canvas.drawPath(path, mPaintOrientation);
//            Path textPath = new Path();
//            textPath.moveTo(points[4], points[5]);
//            textPath.lineTo(points[4], points[5] - 500);
//            canvas.drawPath(path, mPaintOrientation);
//
//            canvas.drawTextOnPath(locationBean.getLocationName(), textPath, 0f, 0f, mPaintWhite);
//
//
//            if (i > 0) {
//                Path linePath = new Path();
//                Location previous = locationBeanList.get(i - 1);
//
//                PointF previousCenter = mParent.get().mapCoordinateToWidthCoordinateF(previous.getX(), previous.getY());
//                linePath.moveTo(previousCenter.x, previousCenter.y);
//                linePath.lineTo(center.x, center.y);
//                canvas.drawPath(linePath, mPaintLine);
//
//            }
//
//        }









    }
}