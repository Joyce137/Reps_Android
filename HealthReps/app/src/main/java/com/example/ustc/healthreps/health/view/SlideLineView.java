package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HBL on 2016/3/5.
 */
public class SlideLineView extends View {
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

    public SlideLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        textPaint = new Paint();
        tickMarksPaint = new Paint();
        linePaint.setColor(0xfff50057);
        textPaint.setColor(0xfff50057);
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
        x_coords.add(56);
        x_coords.add(76);
        x_coords.add(64);
        x_coords.add(71);
        x_coords.add(50);

        x_coords.add(55);
        x_coords.add(70);
        x_coords.add(62);
        x_coords.add(70);
        x_coords.add(55);

        x_coords.add(56);
        x_coords.add(76);
        x_coords.add(64);
        x_coords.add(71);
        x_coords.add(50);

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
        linePaint.setColor(0xfff50057);
        linePaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(xori, (float) ((120 - 40) / 120.0) * yori, width, (float) ((120 - 40) / 120.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((120 - 50) / 120.0) * yori, width, (float) ((120 - 50) / 120.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((120 - 60) / 120.0) * yori, width, (float) ((120 - 60) / 120.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((120 - 70) / 120.0) * yori, width, (float) ((120 - 70) / 120.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((120 - 80) / 120.0) * yori, width, (float) ((120 - 80) / 120.0) * yori, tickMarksPaint);
        canvas.drawLine(xori, (float) ((120 - 90) / 120.0) * yori, width, (float) ((120 - 90) / 120.0) * yori, tickMarksPaint);

        for (int i = 0; i < x_coords.size(); i++) {
            int pointX = i * intervalX + xinit;
            float pointY = (float) ((120 - x_coords.get(i)) / 120.0) * yori;
            if (i == 0) {
                path.moveTo(pointX, pointY);
            } else {
                path.lineTo(pointX, pointY);
            }
            canvas.drawText(String.valueOf(x_coords.get(i)), pointX, pointY - 20, textPaint);
            canvas.drawCircle(pointX, pointY, 12, textPaint);
          //  Log.d("SlideLineView", "pointX=" + pointX + " pointY=" + pointY);
        }
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
