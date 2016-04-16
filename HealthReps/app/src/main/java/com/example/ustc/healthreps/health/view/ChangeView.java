package com.example.ustc.healthreps.health.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.StringTokenizer;

/**
 * Created by HBL on 2016/2/2.
 */
public class ChangeView extends View {
    public static final int MODE_PRIVATE = 0;

    private Paint mPaint;
    private Paint mPointPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mbackLinePaint;
    private Context mContext;

    public ChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mbackLinePaint = new Paint();
        mbackLinePaint.setColor(Color.WHITE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.WHITE);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.YELLOW);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(25F);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

     //   Bitmap bitmap;
    /*	for (int i = 20; i < 440; i += 80) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ww0);
			canvas.drawBitmap(bitmap, i, 0f, mPaint);

		}
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ww1);
		for (int i = 20; i < 440; i += 80) {
			canvas.drawBitmap(bitmap, i, 400f, mPaint);

		}*/
        int[] topTem = new int[]{3, 3, 5, 7, 4, 4};


        int top = 20;

        float space = 0f;
        float space1 = 0f;
        int temspace = 300 / 60;
        for (int i = 0; i < topTem.length; i++) {
            space = (top - topTem[i]) * temspace;
            if (i != topTem.length - 1) {
                space1 = (top - topTem[i + 1]) * temspace;
                canvas.drawText(topTem[i] + "°", 30 + 80 * i, 110 + space,
                        mTextPaint);
                canvas.drawLine(40 + 80 * i, 120 + space, 120 + 80 * i,
                        120 + space1, mLinePaint);
                canvas.drawCircle(40 + 80 * i, 120 + space, 5, mPointPaint);
                canvas.drawCircle(40 + 80 * (i + 1), 120 + space1, 5,
                        mPointPaint);
            } else {
                canvas.drawText(topTem[i] + "°", 30 + 80 * i, 110 + space,
                        mTextPaint);
            }

        }


    }


}
