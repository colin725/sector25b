package com.fsck.sector25;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Joystick {

    private int mWidth;
    private int mHeight;
    private float mWidthBig;
    private float mWidthSmall;
    private float mToEdge;

    /* X and Y of left joystick */
    private float mX1;
    private float mY1;

    /* X and Y of right joystick */
    private float mX2;
    private float mY2;

    /* Tracking ID of input events for each joystick */
    private int mJoystickID1 = -1;
    private int mJoystickID2 = -1;

    public Joystick() {
    }

    public void set(int width, int height){
        mWidth = width;
        mHeight = height;
        mWidthBig = (float)height/6;
        mWidthSmall = (float)height/10;
        mToEdge = 2 * mWidthBig - mWidthSmall;
    }

    public void drawLeft(Canvas canvas, Paint paint){
        paint.setAlpha(75);
        canvas.drawCircle(mToEdge, mHeight - mToEdge, mWidthBig, paint);
        paint.setAlpha(135);
        canvas.drawCircle(mToEdge + mX1, mHeight - mToEdge + mY1, mWidthSmall, paint);
        paint.setAlpha(255);
    }

    public void drawRight(Canvas canvas, Paint paint){
        paint.setAlpha(75);
        canvas.drawCircle(mWidth - mToEdge, mHeight - mToEdge, mWidthBig, paint);
        paint.setAlpha(135);
        canvas.drawCircle(mWidth - mToEdge + mX2, mHeight - mToEdge + mY2, mWidthSmall, paint);
        paint.setAlpha(255);
    }

    //return X joystick distance from center from -1 to 1
    public float getX1(){
        return (mX1);
    }

    public float getX2(){
        return (mX2);
    }

    //return Y joystick distance from center from -1 to 1
    public float getY1(){
        return (mY1);
    }

    public float getY2(){
        return (mY2);
    }

    private void actionDown(MotionEvent event, int ID){
        if (Math.sqrt((event.getX(ID)-mToEdge)*(event.getX(ID)-mToEdge) +
                (event.getY(ID)-(mHeight-mToEdge))*(event.getY(ID)-(mHeight-mToEdge))) 
                < mWidthBig){
            mJoystickID1 = ID;
        }
        if (Math.sqrt((event.getX(ID)-(mWidth-mToEdge))*(event.getX(ID)-(mWidth-mToEdge)) +
                (event.getY(ID)-(mHeight-mToEdge))*(event.getY(ID)-(mHeight-mToEdge))) 
                < mWidthBig){
            mJoystickID2 = ID;
        }
    }

    private void actionDown(MotionEvent event){
        final int pointerIndex = event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        actionDown(event, event.getPointerId(pointerIndex));
    }

    public void actionUp(MotionEvent event, int ID){
        if(ID == mJoystickID1){
            mX1 = 0;
            mY1 = 0;
            mJoystickID1 = -1;
        } else if (ID == mJoystickID2){
            mX2 = 0;
            mY2 = 0;
            mJoystickID2 = -1;
        }
    }

    public void actionMove(MotionEvent event, int ID){
        if(event.getPointerId(ID) == mJoystickID1){
            mX1 = event.getX(ID) - mToEdge;
            mY1 = event.getY(ID) - (mHeight - mToEdge);
            double a = Math.sqrt((double) (mX1 * mX1 + mY1 * mY1));
            if (a > mWidthBig) {
                mX1 = (float) (mX1 / a * mWidthBig);
                mY1 = (float) (mY1 / a * mWidthBig);
            }
        } else if (event.getPointerId(ID) == mJoystickID2){
            mX2 = event.getX(ID) - (mWidth - mToEdge);
            mY2 = event.getY(ID) - (mHeight - mToEdge);
            double a = Math.sqrt((double) (mX2 * mX2 + mY2 * mY2));
            if (a > mWidthBig) {
                mX2 = (float) (mX2 / a * mWidthBig);
                mY2 = (float) (mY2 / a * mWidthBig);
            }
        }
    }

    public void touch(MotionEvent event){
        int eventAction = event.getAction();
        int actionCode = eventAction & MotionEvent.ACTION_MASK;

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
            actionDown(event);
        }

        if (eventAction == MotionEvent.ACTION_DOWN) {
            actionDown(event, event.getPointerId(0));
        }

        if (actionCode == MotionEvent.ACTION_POINTER_UP) {
            final int pointerIndex = event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            actionUp(event, event.getPointerId(pointerIndex));
        }

        if (eventAction == MotionEvent.ACTION_UP) {
            actionUp(event, event.getPointerId(0));
        }

        if (eventAction == MotionEvent.ACTION_MOVE) {
            for(int i=0; i < event.getPointerCount(); i++){
                actionMove(event, i);
            }
        }
    }

    public void clear() {
        mX1 = 0;
        mY1 = 0;
        mX2 = 0;
        mY2 = 0;
    }
}