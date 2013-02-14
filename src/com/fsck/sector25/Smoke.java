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

public class Smoke {
    ArrayList<float[]> smokes = new ArrayList<float[]>();
    Bitmap sprite;

    public Smoke(Resources res) {
        sprite = BitmapFactory.decodeResource(res,
                R.drawable.smoke);
    }

    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < smokes.size(); i++){
            float[] j = smokes.get(i);
            paint.setAlpha((int)j[2]);
            canvas.drawBitmap(sprite, j[0] - sprite.getWidth(),
                    j[1] - sprite.getHeight(), paint);
            paint.setAlpha(255);
        }
    }

    public void update(){
        for (int i = smokes.size()-1; i >= 0; i--){
            float[] j = smokes.get(i);

            //move away from character
            j[0]+=j[3];
            j[1]+=j[4];

            //Decrease alpha until gone
            j[2] -= 10;
            if(j[2] <= 0) smokes.remove(i);
            else smokes.set(i, j);
        }
    }

    public void add(float x, float y, float vx, float vy){
        float[] newSmoke = new float[5];
        newSmoke[0] = x; //position x
        newSmoke[1] = y; //position y
        newSmoke[2] = 255; //alpha
        newSmoke[3] = vx; //velocity x
        newSmoke[4] = vy; //velocity y
        smokes.add(newSmoke);
    }

    public void move(float x, float y){
        for (float[] i : smokes){
            i[0] -= x/10;
            i[1] -= y/10;
        }
    }
}
