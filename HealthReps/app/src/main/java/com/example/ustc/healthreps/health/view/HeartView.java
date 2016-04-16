package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.ustc.healthreps.health.ui.DayInforFragment;

/**
 * Created by HBL on 2016/3/4.
 */
public class HeartView extends View {
   // private BarAnimation anim;
    int totalScore = 0;
    Paint paint;

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
      //  anim=new BarAnimation();
        paint = new Paint();
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int redcolor = 0xF44336;
        int bluecolor = 0x2196F3;
        int greencolor = 0x64DD17;
        totalScore=Integer.valueOf(DayInforFragment.str01);
      int red, green, blue;
        int tempcolor = 0xff4caf50;
        if (totalScore <= 125) {

            tempcolor = redcolor + ((bluecolor - redcolor) /125) * (totalScore);
            red = (tempcolor & 0xff0000) >> 16;
            green = (tempcolor & 0x00ff00) >> 8;
            blue = (tempcolor & 0x0000ff);
            paint.setColor(Color.rgb(red, green, blue));
            Log.d("HeartView",Integer.toHexString(Color.rgb(red, green, blue))+"    1");

        } else {
            tempcolor = bluecolor + (greencolor - bluecolor) / 125 * (totalScore - 125);
            red = (tempcolor & 0xff0000) >> 16;
            green = (tempcolor & 0x00ff00) >> 8;
            blue = (tempcolor & 0x0000ff);
            paint.setColor(Color.rgb(red, green, blue));
            Log.d("HeartView",Integer.toHexString(Color.rgb(red, green, blue))+"    2");
        }
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();
        canvas.translate(screenWidth / 2, screenHeight / 3);
        Path path = new Path();
        path.moveTo(-screenWidth / 4, 0);
        float x;
        float y;
        for (float i = -2.0f; i < 2; i += 0.02) {
            x = i;
            float temp = Math.abs(x);
            y = (float) Math.sqrt(2 * temp - Math.pow(x, 2));
            path.lineTo(screenWidth / 8 * x, -screenWidth / 8 * y);
        }
        path.lineTo(screenWidth / 4, 0);
        canvas.drawPath(path, paint);
        path.moveTo(-screenWidth / 4, 0);
        for (float i = -2.0f; i < 2; i += 0.02) {
            x = i;
            double temp = Math.sqrt(2) - Math.sqrt(Math.abs(x));
            y = (float) (-2.55 * Math.sqrt(temp));
            path.lineTo(screenWidth / 8 * x, -screenWidth / 8 * y);
        }
        path.lineTo(screenWidth / 4, 0);
        canvas.drawPath(path, paint);
    }

    public void startAnim() {
       // this.startAnimation(anim);
    }

   /* private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
               // totalScore = (int) (20 * interpolatedTime);
                totalScore++;
                Log.d("HeartView",totalScore+"");
                postInvalidate();
            } else {
                totalScore = 255;
               // postInvalidate();
            }



        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(700);
            //设置动画结束后保留效果
            setFillAfter(true);
            setInterpolator(new LinearInterpolator());
        }
    }*/
}
