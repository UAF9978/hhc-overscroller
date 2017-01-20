package com.hhc.overscroller.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;
import android.widget.TextView;

public class JellyTextView extends TextView {

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;

    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;

    private float lastX;
    private float lastY;

    private float startX;
    private float startY;

    public JellyTextView(Context context) {
        super(context);
        init(context);
    }

    public JellyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JellyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        //来回颤动的效果 BounceInterpolation
        mScroller = new OverScroller(context, new BounceInterpolator());

        ViewConfiguration mViewConfig = ViewConfiguration.get(getContext());
        mScaledMinimumFlingVelocity = mViewConfig.getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = mViewConfig.getScaledMaximumFlingVelocity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("hehc", "onTouchEvent getX()=" + getX() + "__getY()=" + getY());
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;

                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int velocityX = (int)mVelocityTracker.getXVelocity();
                int velocityY = (int)mVelocityTracker.getYVelocity();
                recodeInfo(velocityX, velocityY, mScaledMinimumFlingVelocity);
                Log.i("hehc", "velocityX:" + velocityX + ",velocityY:" + velocityY + ",mScaledMinimumFlingVelocity:" + mScaledMinimumFlingVelocity);
                if (velocityX > mScaledMinimumFlingVelocity) {
                    //mScroller.fling((int)getX(), (int)getY(), velocityX, velocityY, (int)startX, (int)startX,(int)startY, (int)startY);
                    //invalidate();
                    int x = (int) getX();
                    int y = (int) getY();
                    int wx = (int) (x - startX);
                    int wy = (int) (y - startY);
                    mScroller.startScroll(x, y, -wx, -wy);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
        Log.i("hehc", "onSizeChanged getX()=" + startX + "__getY()=" + startY);
    }

    public void spingBack() {
        int x = (int) getX();
        int y = (int) getY();
        int wx = (int) (x - startX);
        int wy = (int) (y - startY);
        if (mScroller.springBack(x, y, (int)startX, (int)startX, (int)startY, (int)startY)) {
            Log.i("hehc", "spingBack getX()=" + getX() + "__getY()=" + getY());
            invalidate();
        }
    }

    public void fling(int velocityX, int velocityY) {
        int x = (int) getX();
        int y = (int) getY();
        int wx = (int) (x - startX);
        int wy = (int) (y - startY);
        mScroller.fling((int)getX(), (int)getY(), velocityX, velocityY, (int)startX, (int)startX,(int)startY, (int)startY);
        invalidate();
    }

    /**
     * 记录当前速度
     *
     * @param velocityX x轴速度
     * @param velocityY y轴速度
     */
    private void recodeInfo(float velocityX, float velocityY, float maxVelocity) {
        final String info = String.format("maxVelocity=%f\nvelocityX=%f\nvelocityY=%f", maxVelocity, velocityX, velocityY);
        setText(info);
    }
}  