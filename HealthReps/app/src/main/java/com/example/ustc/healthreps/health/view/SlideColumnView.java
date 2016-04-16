package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HBL on 2016/3/8.
 */
public class SlideColumnView extends View {

    private int minXinit;//左右滑动时第一个点允许最小的x坐标
    private int maxXinit;//第一个点允许允许最大的x坐标
    Paint linePaint, textPaint, tickMarksPaint;
    // 指定了一个模糊的样式和半径来处理 Paint 的边缘
    private int intervalX;//两个点之间的X坐标值的间隔
    List<Integer> x_coords = new ArrayList<Integer>();
    ;//x坐标点的值
    float startX = 0;//滑动时候，上一次手指的x坐标
    private int width;//控件宽度
    private int height;//控件高度

    private int xinit;//第一个点x坐标
    private int xori;//圆点x坐标
    private int yori;//圆点y坐标
    int color[] = {0xfff44336, 0xffE91E63, 0xff9C27B0, 0xff673AB7, 0xff3F51B5, 0xff2196F3, 0xff03a9f4};
    private BlurMaskFilter mBlur = null;
    public SlideColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        textPaint = new Paint();
        tickMarksPaint = new Paint();

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
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAlpha(200);
        mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER);
        // 模糊效果
        linePaint.setMaskFilter(mBlur);

        x_coords.add(5600);
        x_coords.add(1500);
        x_coords.add(1444);
        x_coords.add(3200);
        x_coords.add(1200);

        x_coords.add(1233);
        x_coords.add(4500);
        x_coords.add(2233);
        x_coords.add(3400);
        x_coords.add(3600);

        x_coords.add(5920);
        x_coords.add(189);
        x_coords.add(65);
        x_coords.add(2345);
        x_coords.add(789);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed)
        {
            xori = width / 15;
            yori = 3 * height / 4;
            intervalX = xori;
       //     Log.d("SlideColumnView", "xori=" + xori + " intervalX =" + intervalX + " yori=" + yori); //xori=48 intervalX =48 yori=378
            xinit = xori;
       //     Log.d("SlideColumnView", "xinit=" + xinit);//xinit=122

            minXinit = width - xori - (x_coords.size() - 1) *2* intervalX;//504-48-
            maxXinit = xinit;
           // Log.d("SlideColumnView", "minXinit=" + minXinit + " maxXinit=" + maxXinit);
        }
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
      //  Log.d("SlideColumnView","height="+height+" width="+width);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (intervalX * 2*(x_coords.size() - 1) <= width) {
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
    protected void onDrawX(Canvas canvas)
    {
        float leftTopX = 0, leftTopY = 0, rightBtmX = 0, rightBtmY = 0;
        for (int i = 0; i <  x_coords.size(); i++) {
            linePaint.setColor(color[i%7]);
            linePaint.setAlpha(150);
            leftTopX = 2 * i * intervalX+xinit;
            leftTopY = (float) ((6000-x_coords.get(i)) / 6000.0) * yori;
            rightBtmX = leftTopX + xori;
            rightBtmY = yori;
        //    Log.d("SlideColumnView", leftTopY + "");
            canvas.drawRect(leftTopX, leftTopY, rightBtmX, rightBtmY, linePaint);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawX(canvas);
    }
}
