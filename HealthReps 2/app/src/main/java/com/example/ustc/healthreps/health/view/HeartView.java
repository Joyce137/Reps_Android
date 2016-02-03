package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HeartView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private MyThread myThread;

    public HeartView(Context context) {
        super(context);
        // 通过SurfaceView获得SurfaceHolder对象
        holder = getHolder();
        // 为holder添加回调结构SurfaceHolder.Callback
        holder.addCallback(this);
        // 创建一个绘制线程，将holder对象作为参数传入，这样在绘制线程中就可以获得holder
        // 对象，进而在绘制线程中可以通过holder对象获得Canvas对象，并在Canvas上进行绘制
        //myThread = new MyThread(holder);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context);
        holder = getHolder();
        // 为holder添加回调结构SurfaceHolder.Callback
        holder.addCallback(this);
        // 创建一个绘制线程，将holder对象作为参数传入，这样在绘制线程中就可以获得holder
        // 对象，进而在绘制线程中可以通过holder对象获得Canvas对象，并在Canvas上进行绘制
        //myThread = new MyThread(holder);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyle) {
        super(context);
        holder = getHolder();
        // 为holder添加回调结构SurfaceHolder.Callback
        holder.addCallback(this);
        // 创建一个绘制线程，将holder对象作为参数传入，这样在绘制线程中就可以获得holder
        // 对象，进而在绘制线程中可以通过holder对象获得Canvas对象，并在Canvas上进行绘制

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        myThread = new MyThread(holder);
        myThread.setRun(true);
        myThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        myThread.setRun(false);
    }


}
