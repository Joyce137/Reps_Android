package com.example.ustc.healthreps;

/**
 * Created by CaoRuijuan on 12/4/15.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;


public class InputMethodLayout extends LinearLayout {

    private static final String TAG = "InputMethodLayout";
    /** 初始化状态 **/
    public static final byte KEYBOARD_STATE_INIT = -1;
    /** 隐藏状态 **/
    public static final byte KEYBOARD_STATE_HIDE = -2;
    /** 打开状态 **/
    public static final byte KEYBOARD_STATE_SHOW = -3;
    private boolean isInit;// 是否为初始化状态
    private boolean hasKeybord;// 标识是否打开了软键盘
    private int viewHeight;// 布局高度
    private onKeyboardsChangeListener keyboarddsChangeListener;// 键盘状态监听



    public InputMethodLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public InputMethodLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    public void setOnkeyboarddStateListener(onKeyboardsChangeListener listener) {
        keyboarddsChangeListener = listener;
    }

    /**
     * 布局状态发生改变时，会触发onLayout
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (!isInit) {
            isInit = true;
            viewHeight = b;
            keyboardSateChange(KEYBOARD_STATE_INIT);
            Log.w(TAG, "1");
        } else {
            viewHeight = viewHeight < b ? b : viewHeight;

			/*Log.w(TAG, "2");
			Log.w(TAG, "viewHeight "+viewHeight);
			Log.w(TAG, "b "+b);*/
        }
        if (isInit && viewHeight > b) {
            hasKeybord = true;
            keyboardSateChange(KEYBOARD_STATE_SHOW);
            //Log.w(TAG, "显示软键盘");
        }
        if (isInit && hasKeybord && viewHeight == b) {
            hasKeybord = false;
            keyboardSateChange(KEYBOARD_STATE_HIDE);
            //Log.w(TAG, "隐藏软键盘");
        }
    }

    public void keyboardSateChange(int state) {
        if (keyboarddsChangeListener != null) {
            keyboarddsChangeListener.onKeyBoardStateChange(state);
        }
    }

    public interface onKeyboardsChangeListener {
        void onKeyBoardStateChange(int state);
    }
}