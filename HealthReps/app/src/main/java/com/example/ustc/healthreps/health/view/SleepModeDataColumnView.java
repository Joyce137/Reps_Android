package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.example.ustc.healthreps.health.PointWithColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 4/12/16.
 */
public class SleepModeDataColumnView extends View{
    Paint paint;

    BarAnimation anim;

    private BlurMaskFilter mBlur = null;
    float geX, geY;
    int width, height;
    float leftTopX[], leftTopY[], rightBtmX[], templeftTopY[];

    List<Integer> x_coords = new ArrayList<Integer>();
    List<PointWithColor> dataAndColors;

    //X坐标轴最大值
    private float maxAxisValueX = 900;
    //X坐标轴刻度线数量
    private int axisDivideSizeX = 16;
    //Y坐标轴最大值
    private float maxAxisValueY = 120;
    //Y坐标轴刻度线数量
    private int axisDivideSizeY = 10;

    //坐标原点位置
    private int originX;
    private int originY;
    //柱状图数据
    private int columnInfo[][];

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

    public SleepModeDataColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        anim = new BarAnimation();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        mBlur = new BlurMaskFilter(10, BlurMaskFilter.Blur.OUTER);
        paint.setMaskFilter(mBlur);


        int[] heartRates = {71,78,255,93,84,94,101,90,85,77,84,255,74,70,74,84,90,79};

        //去除无效数据
        for(int i = 0;i<heartRates.length; i++){
            if (heartRates[i] > 0 && heartRates[i] < 255)
                x_coords.add(heartRates[i]);
        }

        templeftTopY = new float[x_coords.size()];

        dataAndColors = getDataColors(x_coords);
    }

    public void startAnim() {
        anim.setDuration(2000);
        anim.setInterpolator(new LinearInterpolator());
        this.startAnimation(anim);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        geX = width / (templeftTopY.length*2);
        geY = 4 * height / 5;

        originX = 10;
        originY = height - 50;

        leftTopX = new float[templeftTopY.length];
        leftTopY = new float[templeftTopY.length];
        rightBtmX = new float[templeftTopY.length];
        for (int i = 0; i < templeftTopY.length; i++) {

            PointWithColor pointWithColor = getDataColor(x_coords.get(i));
            int desc = pointWithColor.max-pointWithColor.min;
            float p = (float) ((pointWithColor.data - pointWithColor.min)/(desc * 1.0) + 0.01);

            leftTopX[i] = 2 * i * geX;
            leftTopY[i] = -p * geY;
            rightBtmX[i] = leftTopX[i] + geX;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAxisX(canvas, paint);
        drawAxisY(canvas, paint);
        drawAxisScaleMarkX(canvas, paint);
        drawAxisScaleMarkY(canvas, paint);
        drawAxisArrowsX(canvas, paint);
        drawAxisArrowsY(canvas, paint);
        drawAxisScaleMarkValueX(canvas);
//        drawAxisScaleMarkValueY(canvas, paint);

        canvas.translate(geX, geY+50);
        for (int i = 0; i < templeftTopY.length; i++) {
            paint.setColor(getDataColor(x_coords.get(i)).color);
            paint.setAlpha(150);

            canvas.drawRect(leftTopX[i], templeftTopY[i], rightBtmX[i], 0, paint);
        }
    }

    private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < templeftTopY.length; i++) {
                    templeftTopY[i] = leftTopY[i] * interpolatedTime;
                }
            } else {
                for (int i = 0; i < templeftTopY.length; i++) {
                    templeftTopY[i] = leftTopY[i];
                }
            }
            postInvalidate();

        }
    }

    /**
     * 绘制横坐标轴（X轴）
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisX(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLACK);
        //设置画笔宽度
        paint.setStrokeWidth(1);
        //设置画笔抗锯齿
        paint.setAntiAlias(true);
        //画横轴(X)
        canvas.drawLine(originX, originY, originX + width - 10, originY, paint);
    }

    /**
     * 绘制纵坐标轴(Y轴)
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisY(Canvas canvas, Paint paint) {
        //画竖轴(Y)
        canvas.drawLine(originX, originY, originX, originY - height + 10, paint);//参数说明：起始点左边x,y，终点坐标x,y，画笔
    }


    /**
     * 绘制横坐标轴刻度线(X轴)
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisScaleMarkX(Canvas canvas, Paint paint) {
        float cellWidth = width / axisDivideSizeX;
        for (int i = 0; i < axisDivideSizeX - 1; i++) {
            canvas.drawLine(cellWidth * (i + 1) + originX, originY, cellWidth * (i + 1) + originX, originY - 10, paint);
        }
    }

    /**
     * 绘制纵坐标轴刻度线(Y轴)
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisScaleMarkY(Canvas canvas, Paint paint) {
        float cellHeight = height / axisDivideSizeY;
        for (int i = 0; i < axisDivideSizeY - 2; i++) {
            canvas.drawLine(originX, (originY - cellHeight * (i + 1)), originX + 10, (originY - cellHeight * (i + 1)), paint);
        }
    }

    /**
     * 绘制横坐标轴刻度值(X轴)
     *
     * @param canvas
     */
    private void drawAxisScaleMarkValueX(Canvas canvas) {
        Paint paint = new Paint();
        //设置画笔绘制文字的属性
        paint.setColor(Color.BLACK);
        paint.setTextSize(27);
        paint.setFakeBoldText(true);

        String xValues[] = {"23:00","23:30","0:00","0:30",
                "1:00","1:30","2:00","2:30","3:00","3:30",
                "4:00","4:30","5:00","5:30","6:00","6:30"};

        float cellWidth = width / axisDivideSizeX;
        float cellValue = maxAxisValueX / axisDivideSizeX;
        for (int i = 0; i < axisDivideSizeX; i = i + 2) {
            canvas.drawText(xValues[i], cellWidth * i + originX, originY + 30, paint);
        }
    }

    /**
     * 绘制纵坐标轴刻度值(Y轴)
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisScaleMarkValueY(Canvas canvas, Paint paint) {
        float cellHeight = height / axisDivideSizeY;
        float cellValue = maxAxisValueY / axisDivideSizeY;
        for (int i = 1; i < axisDivideSizeY; i++) {
            canvas.drawText(String.valueOf(cellValue * i), originX - 80, originY - cellHeight * i + 10, paint);
        }
    }

    /**
     * 绘制横坐标轴箭头(X轴)
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisArrowsX(Canvas canvas, Paint paint) {
        //画三角形（X轴箭头）
        Path mPathX = new Path();
        mPathX.moveTo(width, originY);//起始点
        mPathX.lineTo(width - 20, originY - 10);//下一点
        mPathX.lineTo(width - 20, originY + 10);//下一点
        mPathX.close();
        canvas.drawPath(mPathX, paint);
    }

    /**
     * 绘制纵坐标轴箭头(Y轴)
     *
     * @param canvas
     * @param paint
     */
    private void drawAxisArrowsY(Canvas canvas, Paint paint) {
        //画三角形（Y轴箭头）
        Path mPathX = new Path();
        mPathX.moveTo(originX, height - originY - 50);//起始点
        mPathX.lineTo(originX - 10, height - originY - 30);//下一点
        mPathX.lineTo(originX + 10, height - originY - 30);//下一点
        mPathX.close();
        canvas.drawPath(mPathX, paint);
    }
}
