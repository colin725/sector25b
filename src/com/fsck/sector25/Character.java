package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;

public class Character {
    private int width;
    private int height;
    private float x;
    private float y;
    private int direction;
    private float directionGun;
    private Sprite spaceman;
    Bitmap man;
    Bitmap arm1;
    Bitmap arm2;
    Bitmap gun;

    public Character(Resources res) {
        man = BitmapFactory.decodeResource(res,
                R.drawable.spaceman);
        arm1 = BitmapFactory.decodeResource(res,
                R.drawable.arm1);
        arm2 = BitmapFactory.decodeResource(res,
                R.drawable.arm2);
        gun = BitmapFactory.decodeResource(res,
                R.drawable.gun);
    }

    public void set(int width, int height){
        this.width = width;
        this.height = height;
        x = width/2;
        y = height*2/5;

        spaceman = new Sprite(Bitmap.createScaledBitmap(man, width/5, width/5, false), 2);
        arm1 = Bitmap.createScaledBitmap(arm1, width*1/30, width*1/40, false);
        arm2 = Bitmap.createScaledBitmap(arm2, width*1/30, width*1/45, false);
        gun = Bitmap.createScaledBitmap(gun, width*1/13, width*1/26, false);
    }

    public float getShotX(){
        float shotx = (float) (spaceman.getWidth()*2/3*Math.cos(Math.toRadians(directionGun)));
        return x + shotx - 2*shotx*direction;
    }

    public float getShotY(){
        float shoty = (float) (spaceman.getWidth()*2/3*Math.sin(Math.toRadians(directionGun)));
        return y + shoty;
    }

    public float getSmokeX(){
        return (x - spaceman.getWidth()/4 + direction*spaceman.getWidth()/2 + spaceman.getWidth()/4);
    }

    public float getSmokeY(){
        return y + spaceman.getHeight()/4;
    }

    public float getSmokeVX(){
        return -(x - getSmokeX())/50;
    }

    public float getSmokeVY(){
        return (y - getSmokeY())/50;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setDirection(float x, float y, float x2, float y2){
        //Set character direction and frame
        spaceman.setFrame(1);
        if (x > 0){
            direction = 0;
            spaceman.setFrame(0);
        } else if (x < 0){
            direction = 1;
            spaceman.setFrame(0);
        }
        if (x2 > 0){
            direction = 0;
        } else if (x2 < 0){
            direction = 1;
        }

        //Set gun direction
        if(x2 != 0 && y2 != 0){
            directionGun = (float) Math.toDegrees(Math.atan(y2/Math.abs(x2)));
        }
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.save();
        canvas.translate(x, y);
        if(direction == 1) canvas.scale(-1, 1);
        canvas.save();

        canvas.rotate(directionGun-5,-spaceman.getWidth()/6, 0);
        canvas.drawBitmap(arm2, 0, 0, paint);
        canvas.restore();

        spaceman.draw(canvas, 0, 0);

        canvas.rotate(directionGun-5,-spaceman.getWidth()/12, spaceman.getWidth()/15);
        canvas.drawBitmap(gun, -spaceman.getWidth()/15, 0, paint);
        canvas.drawBitmap(arm1, -spaceman.getWidth()/9, spaceman.getWidth()/15, paint);

        canvas.restore();
    }

    public void drawHit(Canvas canvas, Paint paint){
        canvas.drawCircle(x, y, 20, paint);
    }
}
