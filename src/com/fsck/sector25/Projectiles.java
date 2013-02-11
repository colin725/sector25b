package com.fsck.sector25;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class Projectiles {
    ArrayList<float[]> projectiles = new ArrayList<float[]>();
    Sprite sprite;

    public Projectiles(Resources res) {
        sprite = new Sprite(BitmapFactory.decodeResource(res,
                R.drawable.laze), 28);
    }

    public void draw(Canvas canvas, Paint paint){
        for (float[] i : projectiles){
            //TODO: make sprite frames draw according to i[4]
            sprite.draw(canvas, (int)i[0], (int)i[1]);
        }
    }

    public void update(){
        for (float[] i : projectiles){
            //increments frame of each projectile
            i[4] = (i[4]+1)%28;
            //movement
            i[0] += i[2];
            i[1] += i[3];
        }

        //TODO:remove projectiles off screen

        sprite.Update();
    }

    public void add(float x, float y, float vx, float vy){
        float[] newProjectile = new float[5];
        newProjectile[0] = x; //position x
        newProjectile[1] = y; //position y

        double a = Math.sqrt((double) (vx * vx + vy * vy));
        newProjectile[2] = (float) (vx / a * 10); //velocity x
        newProjectile[3] = (float) (vy / a * 10); //velocity y

        newProjectile[4] = 0; //initial frame
        projectiles.add(newProjectile);
    }
}
