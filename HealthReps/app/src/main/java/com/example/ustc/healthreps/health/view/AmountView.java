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
 * Created by HBL on 2016/2/1.
 */

public class AmountView extends View {
    Paint paint;
    int count = 120; //总刻度数
    int tempcount = 0;
    private BarAnimation anim;

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xff388e3c);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        anim = new BarAnimation();
    }
    public AmountView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
        Paint tmpPaint = new Paint(paint); //小刻度画笔
        tmpPaint.setStrokeWidth(2);
        float y = 180;
        for (int i = 0; i < tempcount; i++) {
            canvas.drawLine(0f, y, 0f, y + 45f, tmpPaint);
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸
        }
    }

    public void startAnim() {
        this.startAnimation(anim);
    }

    private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                tempcount = (int) (count * interpolatedTime);
                postInvalidate();
            //    Log.d("AmountView", "tempcount:" + tempcount);
            } else {
                tempcount=count;
            }
        }
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(700);
            //设置动画结束后保留效果
            setFillAfter(true);
            tempcount = 0;
            setInterpolator(new LinearInterpolator());
        }
    }
}
