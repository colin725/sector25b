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
    ArrayList<int[]> projectiles = new ArrayList<int[]>();
    Sprite sprite;

    public Projectiles(Resources res) {
        sprite = new Sprite(BitmapFactory.decodeResource(res,
                R.drawable.laze), 28);
    }

    public void draw(Canvas canvas, Paint paint){
        sprite.draw(canvas, 50, 50);
    }

    public void update(){
        for (int[] i : projectiles){
            i[4] = (i[4]+1)%28;
        }
    }

    public void add(int x, int y, int dx, int dy){
        int[] newProjectile = new int[5];
        newProjectile[0] = x; //position x
        newProjectile[1] = y; //position y
        newProjectile[2] = dx; //direction x
        newProjectile[3] = dy; //position y
        newProjectile[4] = 0; //initial frame
        projectiles.add(newProjectile);
    }
}
