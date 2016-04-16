package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by HBL on 2016/3/11.
 */
public class BubbleView extends View {
    Paint cirPaint;
    BarAnimation anim = new BarAnimation();
    ;
    int step[] = {56, 34, 78, 56, 45, 33, 32};
    int color[] = {0xfff44336, 0xffE91E63, 0xff9C27B0, 0xff673AB7, 0xff3F51B5, 0xff2196F3, 0xff03a9f4};
    int width, height;
    float radius[], maxRadius[];
    float geX, geY;
    float X[], Y[];
    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cirPaint = new Paint();
        cirPaint.setStyle(Paint.Style.FILL);
        cirPaint.setAlpha(100);
        cirPaint.setAntiAlias(true);
        X=new float[7];
       Y=new float[7];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        geX = width / 7;
        geY = 3 * height / 4;
        maxRadius = new float[7];
        radius=new float[7];
        for (int i = 0; i < 7; i++) {
            maxRadius[i] = (float) (step[i] / 80.0) * (geX / 2);
            X[i]=i * geX;
            Y[i] = (float) (step[i] / 80.0) * (geY-geX / 2);
            Log.d("BubbleView", X[i] + " "+Y[i]);
        }
        setMeasuredDimension(width, height);
    }

    public void startAnim() {
        anim.setDuration(700);
        anim.setInterpolator(new LinearInterpolator());
        this.startAnimation(anim);
    }

    private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < 7; i++)
                    radius[i] = maxRadius[i] * interpolatedTime;

            } else {
                for (int i = 0; i < 7; i++)
                    radius[i] = maxRadius[i];
            }
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(geX / 2, geY);

        for (int i = 0; i < 7; i++) {
            cirPaint.setColor(color[i]);
            canvas.drawCircle(X[i], -Y[i], radius[i], cirPaint);
        }
    }
}
