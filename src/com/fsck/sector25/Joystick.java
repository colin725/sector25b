package com.fsck.sector25;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

public class Joystick {
    private int width;
    private int height;
    private float widthBig;
    private float widthSmall;
    private float toEdge;

    /* X and Y of left joystick */
    private float x1;
    private float y1;

    /* X and Y of right joystick */
    private float x2;
    private float y2;

    /* Tracking ID of input events for each joystick */
    int joystickID1 = -1;
    int joystickID2 = -1;

    /* Last know position of joysticks */
    private float pointx1 = 0;
    private float pointy1 = 0;
    private float pointx2 = 0;
    private float pointy2 = 0;

    public Joystick() {
    }

    public void set(int width, int height){
        this.width = width;
        this.height = height;
        widthBig = (float)height/6;
        widthSmall = (float)height/10;
        toEdge = 2 * widthBig - widthSmall;
    }

    public void drawLeft(Canvas canvas, Paint paint){
        paint.setAlpha(75);
        canvas.drawCircle(toEdge, height - toEdge, widthBig, paint);
        paint.setAlpha(135);
        canvas.drawCircle(toEdge + x1, height - toEdge + y1, widthSmall, paint);
    }

    public void drawRight(Canvas canvas, Paint paint){
        paint.setAlpha(75);
        canvas.drawCircle(width - toEdge, height - toEdge, widthBig, paint);
        paint.setAlpha(135);
        canvas.drawCircle(width - toEdge + x2, height - toEdge + y2, widthSmall, paint);
    }

    //return X joystick distance from center from -1 to 1
    public float getX1(){
        return (x1);
    }

    public float getX2(){
        return (x2);
    }

    //return Y joystick distance from center from -1 to 1
    public float getY1(){
        return (y1);
    }

    public float getY2(){
        return (y2);
    }

    private void actionDown(MotionEvent event, int ID){
        if (Math.sqrt((event.getX(ID)-toEdge)*(event.getX(ID)-toEdge) +
                (event.getY(ID)-(height-toEdge))*(event.getY(ID)-(height-toEdge))) 
                < widthBig){
            pointx1 = event.getX(ID);
            pointy1 = event.getY(ID);
            joystickID1 = ID;
        }
        if (Math.sqrt((event.getX(ID)-(width-toEdge))*(event.getX(ID)-(width-toEdge)) +
                (event.getY(ID)-(height-toEdge))*(event.getY(ID)-(height-toEdge))) 
                < widthBig){
            pointx2 = event.getX(ID);
            pointy2 = event.getY(ID);
            joystickID2 = ID;
        }
    }

    private void actionDown(MotionEvent event){
        final int pointerIndex = event.getAction() >> MotionEvent.ACTION_POINTER_ID_SHIFT;
        actionDown(event, event.getPointerId(pointerIndex));
    }

    public void actionUp(MotionEvent event, int ID){
        if(ID == joystickID1){
            x1 = 0;
            y1 = 0;
            joystickID1 = -1;
        } else if (ID == joystickID2){
            x2 = 0;
            y2 = 0;
            joystickID2 = -1;
        }
    }

    public void actionMove(MotionEvent event, int ID){
        if(event.getPointerId(ID) == joystickID1){
            x1 = event.getX(ID) - toEdge;
            y1 = event.getY(ID) - (height - toEdge);
            double a = Math.sqrt((double) (x1 * x1 + y1 * y1));
            if (a > widthBig) {
                x1 = (float) (x1 / a * widthBig);
                y1 = (float) (y1 / a * widthBig);
            }
            pointx1 = event.getX(ID);
            pointy1 = event.getY(ID);
        } else if (event.getPointerId(ID) == joystickID2){
            x2 = event.getX(ID) - (width - toEdge);
            y2 = event.getY(ID) - (height - toEdge);
            double a = Math.sqrt((double) (x2 * x2 + y2 * y2));
            if (a > widthBig) {
                x2 = (float) (x2 / a * widthBig);
                y2 = (float) (y2 / a * widthBig);
            }
            pointx2 = event.getX(ID);
            pointy2 = event.getY(ID);
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
}