package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by HBL on 2016/3/5.
 */
public class ColumnView extends View {
    Paint paint;

    BarAnimation anim;
    int step[] = {5600, 349, 780, 2000, 4521, 3333, 3232};
    int color[] = {0xfff44336, 0xffE91E63, 0xff9C27B0, 0xff673AB7, 0xff3F51B5, 0xff2196F3, 0xff03a9f4};
    private BlurMaskFilter mBlur = null;
    float geX, geY;
    int width, height;
    float leftTopX[], leftTopY[], rightBtmX[], templeftTopY[];

    public ColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        anim = new BarAnimation();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER);
        paint.setMaskFilter(mBlur);
        templeftTopY = new float[7];
    }

    public void startAnim() {
        anim.setDuration(2000);
        anim.setInterpolator(new LinearInterpolator());
        this.startAnimation(anim);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        geX = width / 15;
        geY = 3 * height / 4;
        leftTopX = new float[7];
        leftTopY = new float[7];
        rightBtmX = new float[7];
        for (int i = 0; i < 7; i++) {
            leftTopX[i] = 2 * i * geX;
            leftTopY[i] = -(float) (step[i] / 6000.0) * geY;
            rightBtmX[i] = leftTopX[i] + geX;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(geX, geY);
        for (int i = 0; i < 7; i++) {
            paint.setColor(color[i]);
            paint.setAlpha(150);

            canvas.drawRect(leftTopX[i], templeftTopY[i], rightBtmX[i], 0, paint);
        }
    }

    private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < 7; i++) {
                    templeftTopY[i] = leftTopY[i] * interpolatedTime;
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    templeftTopY[i] = leftTopY[i];
                }
            }
            postInvalidate();

        }
    }
}
