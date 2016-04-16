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
 * Created by HBL on 2016/3/4.
 */
public class LineView extends View {
    BarAnimation anim;
    Paint linePaint, textPaint, tickMarksPaint;
    int count = 0;
    int hearte[] = {58, 50, 67, 73, 55, 60, 50};

    // 指定了一个模糊的样式和半径来处理 Paint 的边缘
    private BlurMaskFilter mBlur = null;

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        anim = new BarAnimation();
        linePaint = new Paint();
        textPaint = new Paint();
        tickMarksPaint = new Paint();
        linePaint.setColor(0xff9c27b0);
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
        linePaint.setStyle(Paint.Style.FILL);
        mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.INNER);
        // 模糊效果
        linePaint.setMaskFilter(mBlur);
    }

    public void startAnim() {
        anim.setDuration(700);
        anim.setInterpolator(new LinearInterpolator());
        this.startAnimation(anim);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Log.d("LineView", "width=" + width + " height=" + height);
        float geX = width / 7, geY = 3 * height / 4;
        canvas.translate(geX / 2, geY);

        canvas.drawLine(0, -(float) (40 / 120.0) * geY, width - geX, -(float) (40 / 120.0) * geY, tickMarksPaint);
        canvas.drawLine(0, -(float) (50 / 120.0) * geY, width - geX, -(float) (50 / 120.0) * geY, tickMarksPaint);
        canvas.drawLine(0, -(float) (60 / 120.0) * geY, width - geX, -(float) (60 / 120.0) * geY, tickMarksPaint);
        canvas.drawLine(0, -(float) (70 / 120.0) * geY, width - geX, -(float) (70 / 120.0) * geY, tickMarksPaint);
        canvas.drawLine(0, -(float) (80 / 120.0) * geY, width - geX, -(float) (80 / 120.0) * geY, tickMarksPaint);
        canvas.drawLine(0, -(float) (90 / 120.0) * geY, width - geX, -(float) (90 / 120.0) * geY, tickMarksPaint);

        float startX = 0, startY = -(float) (hearte[0] / 120.0) * geY, endX = 0, endY = 0;
        canvas.drawText(String.valueOf(hearte[0]), startX - 15, startY - 20, textPaint);
        canvas.drawCircle(startX, startY, 12, linePaint);
        for (int i = 1; i < count; i++) {
            endX = i * geX;
            endY = -(float) (hearte[i] / 120.0) * geY;
            canvas.drawLine(startX, startY, endX, endY, linePaint);
            canvas.drawText(String.valueOf(hearte[i]), endX - 15, endY - 20, textPaint);

            canvas.drawCircle(endX, endY, 12, linePaint);
            startX = endX;
            startY = endY;
        }
    }

    private class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                count = (int) (interpolatedTime * 7);
                postInvalidate();
            } else {
                count = 7;
            }
        }
    }
}
