package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class Character {
    private int width;
    private int height;
    private float x;
    private float y;
    private int direction;
    private Bitmap alien;

    public Character(Resources res) {
        alien = BitmapFactory.decodeResource(res,
                R.drawable.alien);
    }

    public void set(int width, int height){
        this.width = width;
        this.height = height;
        x = width/2;
        y = height*2/5;
    }

    public float getShotX(){
        return (x + alien.getWidth()/2 - alien.getWidth()*direction);
    }

    public float getShotY(){
        return y;
    }

    public void setDirection(float x){
        if (x > 0){
            direction = 0;
        } else{
            direction = 1;
        }
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.save();
        canvas.translate(x, y);
        if(direction == 1) canvas.scale(-1, 1);
        canvas.drawBitmap(alien, - alien.getWidth()/2,
            - alien.getHeight()/2, paint);
        canvas.restore();
    }
}
