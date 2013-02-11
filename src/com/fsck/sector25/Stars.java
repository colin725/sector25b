package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

public class Stars {
    private float stars[][] = new float[1500][3];
    private float planetPosition[][] = new float[5][3];
    private Bitmap[] planets;

    public Stars(Resources res) {
        for (int i = 0; i < 1500; i++) {
            stars[i][0] = (float) (Math.random() * 2000);
            stars[i][1] = (float) (Math.random() * 2000);
            stars[i][2] = (float) (Math.random() * 6);
        }
        for (int i = 0; i < 5; i++) {
            planetPosition[i][0] = (float) (Math.random() * 2000);
            planetPosition[i][1] = (float) (Math.random() * 2000);
            planetPosition[i][2] = (float) (Math.random() * 6);
        }
        planets = new Bitmap[5];
        //TODO: scale planets down to appear in background
        planets[0] = BitmapFactory.decodeResource(res,
                R.drawable.planet1);
        planets[1] = BitmapFactory.decodeResource(res,
                R.drawable.planet2);
        planets[2] = BitmapFactory.decodeResource(res,
                R.drawable.planet3);
        planets[3] = BitmapFactory.decodeResource(res,
                R.drawable.planet4);
        planets[4] = BitmapFactory.decodeResource(res,
                R.drawable.planet5);
    }

    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < 1500; i++) {
            canvas.drawPoint(stars[i][0] - 10, stars[i][1] - 10, paint);
        }

        paint.setAlpha(60);
        for (int i = 0; i < 5; i++) {
            canvas.drawBitmap(planets[i], planetPosition[i][0] - 500, planetPosition[i][1] - 500, paint);
        }
        paint.setAlpha(255);
    }

    public void move(float x, float y){
        for (int i = 0; i < 1500; i++) {
            stars[i][0] += x * stars[i][2];
            stars[i][1] += y * stars[i][2];
            if(stars[i][0] > 2000) stars[i][0] = 0;
            if(stars[i][0] < 0) stars[i][0] = 2000;
            if(stars[i][1] > 2000) stars[i][1] = 0;
            if(stars[i][1] < 0) stars[i][1] = 2000;
        }
        for (int i = 0; i < 5; i++) {
            planetPosition[i][0] -= x * planetPosition[i][2];
            planetPosition[i][1] -= y * planetPosition[i][2];
            if(planetPosition[i][0] > 2000) planetPosition[i][0] = 0;
            if(planetPosition[i][0] < 0) planetPosition[i][0] = 2000;
            if(planetPosition[i][1] > 2000) planetPosition[i][1] = 0;
            if(planetPosition[i][1] < 0) planetPosition[i][1] = 2000;
        }
    }
}
