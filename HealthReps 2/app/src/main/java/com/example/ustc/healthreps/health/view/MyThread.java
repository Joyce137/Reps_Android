package com.example.ustc.healthreps.health.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Administrator on 2015/10/24.
 */
public class MyThread extends Thread {


    SurfaceHolder holder;
    private boolean isrun;

    @Override
    public void run() {
        int totalScore =50;
        int redcolor = 0xffF44336;
        int bluecolor = 0xff2196F3;
        int greencolor = 0xff64DD17;
        //  super.run();
        int tempcolor=0xff4caf50;
        Paint paint = new Paint();
        //  paint.setColor(0xffF44336);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int red,green,blue;
        if (totalScore<=50)
        {
            tempcolor=redcolor+((bluecolor-redcolor)/50)*(totalScore);
            red = (tempcolor & 0xff0000) >> 16;
            green = (tempcolor & 0x00ff00) >> 8;
            blue = (tempcolor & 0x0000ff);
            paint.setColor(Color.rgb(red, green, blue));

        }

        else {
            tempcolor=bluecolor+(greencolor-bluecolor)/50*(totalScore-50);
            red = (tempcolor & 0xff0000) >> 16;
            green = (tempcolor & 0x00ff00) >> 8;
            blue = (tempcolor & 0x0000ff);
            paint.setColor(Color.rgb(red, green, blue));
        }
        //  paint.setColor(tempcolor);


        Canvas canvas = null;
        canvas = holder.lockCanvas();
        // canvas.drawColor(0xffffbb33);
        canvas.drawColor(Color.WHITE);
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();
        canvas.translate(screenWidth / 2, screenHeight / 3);
        //  Log.d("MyThread", canvas.getWidth() / 2 + " " + canvas.getHeight() / 2);
        Path path = new Path();

        path.moveTo(-screenWidth / 4, 0);

        float x;
        float y;
        for (float i = -2.0f; i < 2; i += 0.02) {
            x = i;
            float temp = Math.abs(x);
            y = (float) Math.sqrt(2 * temp - Math.pow(x, 2));
            Log.d("MyThread", 90 * x + " " + 90 * y);
            path.lineTo(screenWidth / 8 * x, -screenWidth / 8 * y);
        }


        path.lineTo(screenWidth / 4, 0);

        canvas.drawPath(path, paint);

        path.moveTo(-screenWidth / 4, 0);
        for (float i = -2.0f; i < 2; i += 0.02) {
            x = i;
            double temp = Math.sqrt(2) - Math.sqrt(Math.abs(x));
            y = (float) (-2.55 * Math.sqrt(temp));
            Log.d("MyThread", 90 * x + " " + 90 * y);
            path.lineTo(screenWidth / 8 * x, -screenWidth / 8 * y);
        }
        path.lineTo(screenWidth / 4, 0);
        canvas.drawPath(path, paint);
        holder.unlockCanvasAndPost(canvas);

    }

    MyThread(SurfaceHolder holder) {
        this.holder = holder;
        isrun = true;
    }

    public boolean isRun() {
        return isrun;
    }

    public void setRun(boolean isrun) {
        this.isrun = isrun;
    }
}