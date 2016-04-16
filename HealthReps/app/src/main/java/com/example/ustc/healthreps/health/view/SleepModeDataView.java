package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.ustc.healthreps.health.PointWithColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CaoRuijuan on 4/8/16.
 */
public class SleepModeDataView extends View {
    private int minXinit;//左右滑动时第一个点允许最小的x坐标
    private int maxXinit;//第一个点允许允许最大的x坐标
    Paint linePaint, textPaint, tickMarksPaint;
    // 指定了一个模糊的样式和半径来处理 Paint 的边缘
    private BlurMaskFilter mBlur = null;
    private int intervalX;//两个点之间的X坐标值的间隔
    List<Integer> x_coords = new ArrayList<Integer>();
    ;//x坐标点的值
    float startX = 0;//滑动时候，上一次手指的x坐标
    private int width;//控件宽度
    private int height;//控件高度

    private int xinit;//第一个点x坐标
    private int xori;//圆点x坐标
    private int yori;//圆点y坐标

    public static int defaultColor = 0xFF000000;    //默认颜色为黑色
    List<PointWithColor> dataAndColors;


    //为每个数值分配颜色
    public List<PointWithColor> getDataColors(List<Integer> datas){

        List<PointWithColor> dataAndColors = new ArrayList<>();
        int[] colors = {
                0xFF008000,  //绿色
                0xFF3CFF00,  //偏绿
                0xFF73FF00,  //偏绿黄
                0xFFAAFF00,  //偏黄绿
                0xFFD9FF00,  //偏黄
                0xFFFFFF00,  //黄色
                0xFFFFB300,  //偏黄
                0xFFFF7B00,  //橘黄
                0xFFFF3700,  //偏红
                0xFFCC0000   //红色
        };

        //选出最值
        int max = 0;
        int min = 255;
        for(int data:datas){
            if(data>max)
                max = data;
            if(data<min)
                min = data;
        }

        //划分区间
        int desc = max - min;
        double descx = desc/10.0;
        double[] descs = new double[10];
        int[] descxs = new int[10];
        for(int i = 0;i<10;i++){
            descs[i] = min + i * descx;
            descxs[i] = i;
        }

        //为每个区间设置值
        for(int i = 0; i < datas.size(); i++){
            PointWithColor pointWithColor = null;
            for(int j = 0;j<10;j++){
                int xdata = datas.get(i);
                if(xdata>=descs[j]){
                    if(j < 9){
                        if(xdata<descs[j+1])
                        {
                            pointWithColor = new PointWithColor(xdata,colors[j],j);
                        }
                    }
                    else {
                        pointWithColor = new PointWithColor(xdata,colors[j],j);
                    }
                }
            }
            pointWithColor.max = max;
            pointWithColor.min = min;
            dataAndColors.add(pointWithColor);
        }

        return dataAndColors;
    }

    //获取某个值的颜色
    public PointWithColor getDataColor(int data){
        for(PointWithColor pointWithColor:dataAndColors){
            if(pointWithColor.data == data){
                return pointWithColor;
            }
        }
        return null;
    }

    public SleepModeDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        textPaint = new Paint();
        tickMarksPaint = new Paint();
        linePaint.setColor(0xfff50057);
        tickMarksPaint.setColor(0xff000000);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(3);
        tickMarksPaint.setStrokeWidth(1);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(30);
        linePaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        linePaint.setDither(true);
        tickMarksPaint.setAntiAlias(true);
        tickMarksPaint.setAlpha(20);
        textPaint.setStyle(Paint.Style.FILL);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAlpha(200);
        mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER);
        // 模糊效果
        linePaint.setMaskFilter(mBlur);

        int[] heartRates = {71,78,255,93,84,94,101,90,85,77,84,255,74,70,74,84,90};

        //去除无效数据
        for(int i = 0;i<heartRates.length; i++){
            if (heartRates[i] > 0 && heartRates[i] < 255)
                x_coords.add(heartRates[i]);
        }

        dataAndColors = getDataColors(x_coords);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            xori = width / 14;
            yori = 3 * height / 4;
            intervalX = width / 7;
            //   Log.d("SlideLineView", "xori=" + xori + " intervalX =" + intervalX + " yori=" + yori);//xinit=122
            xinit = width / 14;
            // Log.d("SlideLineView", "xinit=" + xinit);//xinit=122

            minXinit = width - xori - (x_coords.size() - 1) * intervalX;
            maxXinit = xinit;
            //   Log.d("SlideLineView", "minXinit=" + minXinit + " maxXinit=" + maxXinit);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawX(canvas);////画折线图
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        // Log.d("SlideLineView","height="+height+" width="+width);
        setMeasuredDimension(width, height);
    }
    protected void onDrawX(Canvas canvas) {

        Path path = new Path();
        linePaint.setColor(0xff33b5e5);
        linePaint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(xori, (float) ((130 - 20) / 130.0) * yori, width, (float) ((130 - 20) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 30) / 130.0) * yori, width, (float) ((130 - 30) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 40) / 130.0) * yori, width, (float) ((130 - 40) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 50) / 130.0) * yori, width, (float) ((130 - 50) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 60) / 130.0) * yori, width, (float) ((130 - 60) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 70) / 130.0) * yori, width, (float) ((130 - 70) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 80) / 130.0) * yori, width, (float) ((130 - 80) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 90) / 130.0) * yori, width, (float) ((130 - 90) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 100) / 130.0) * yori, width, (float) ((130 - 100) / 130.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((130 - 110) / 130.0) * yori, width, (float) ((130 - 110) / 130.0) * yori, tickMarksPaint);

        for (int i = 0; i < x_coords.size(); i++) {
            PointWithColor pointWithColor = getDataColor(x_coords.get(i));

            int pointX = i * intervalX + xinit;
//            float pointY = (float)((1 - pointWithColor.pvalue/10.0) * (yori/1.3)) + 7;
            int desc = pointWithColor.max-pointWithColor.min;
            float p = (float) ((pointWithColor.data - pointWithColor.min)/(desc * 1.0));
            float pointY = (float)((1 - p) * (yori/1.5)) + 73;
            if (i == 0) {
                path.moveTo(pointX, pointY);
            } else {
                path.lineTo(pointX, pointY);
            }

            textPaint.setColor(0xff33b5e5);
            canvas.drawText(String.valueOf(x_coords.get(i)), pointX, pointY - 20, textPaint);
            textPaint.setColor(pointWithColor.color);
            canvas.drawCircle(pointX, pointY, 13, textPaint);
            //  Log.d("SlideLineView", "pointX=" + pointX + " pointY=" + pointY);
        }
        textPaint.setColor(0xff5c5cff);
        canvas.drawPath(path, linePaint);
       /* linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.WHITE);
        linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        RectF rectF = new RectF(0, 0, xori - 12, height);
        canvas.drawRect(rectF, linePaint);*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (intervalX * (x_coords.size() - 1) <= width) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dis = event.getX() - startX;
                startX = event.getX();
                if (xinit + dis > maxXinit) {
                    xinit = maxXinit;
                } else if (xinit + dis < minXinit) {
                    xinit = minXinit;
                } else {
                    xinit = (int) (xinit + dis);
                }
                invalidate();
                break;
        }
        return true;
    }
}