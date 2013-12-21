package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Stars {
    private int mWidth;
    private int mHeight;
    private float mStars[][] = new float[1500][3];
    private float mPlanetPosition[][] = new float[5][3];
    private static Bitmap[] mPlanets;

    public Stars(Resources res) {
        for (int i = 0; i < 1500; i++) {
            mStars[i][0] = (float) (Math.random() * 1.5 - 0.25);
            mStars[i][1] = (float) (Math.random() * 1.5 - 0.25);
            mStars[i][2] = (float) (Math.random() * 0.00006);
        }
        for (int i = 0; i < 5; i++) {
            mPlanetPosition[i][0] = (float) (Math.random() * 1.5 - 0.25);
            mPlanetPosition[i][1] = (float) (Math.random() * 1.5 - 0.25);
            mPlanetPosition[i][2] = (float) (Math.random() * 0.00006);
        }
        mPlanets = new Bitmap[5];

        mPlanets[0] = BitmapFactory.decodeResource(res, R.drawable.planet1);
        mPlanets[1] = BitmapFactory.decodeResource(res, R.drawable.planet2);
        mPlanets[2] = BitmapFactory.decodeResource(res, R.drawable.planet3);
        mPlanets[3] = BitmapFactory.decodeResource(res, R.drawable.planet4);
        mPlanets[4] = BitmapFactory.decodeResource(res, R.drawable.planet5);
    }

    public void set(int width, int height){
        this.mWidth = width;
        this.mHeight = height;
        for(int i = 0; i < mPlanets.length; i++){
            mPlanets[i] = Bitmap.createScaledBitmap(mPlanets[i], (int)((float)mPlanets[i].getWidth()/2000*(float)width),
                    (int)((float)mPlanets[i].getWidth()/2000*(float)width), false);
        }
    }

    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < 1500; i++) {
            canvas.drawPoint(mStars[i][0]*mWidth, mStars[i][1]*mHeight, paint);
        }

        paint.setAlpha(60);
        for (int i = 0; i < 5; i++) {
            canvas.drawBitmap(mPlanets[i], mPlanetPosition[i][0]*mWidth, mPlanetPosition[i][1]*mHeight, paint);
        }
        paint.setAlpha(255);
    }

    public void move(Vector v){
        for (int i = 0; i < 1500; i++) {
            mStars[i][0] -= v.getX() * mStars[i][2];
            mStars[i][1] -= v.getY() * mStars[i][2];
            if(mStars[i][0] > 1.25) mStars[i][0] = -0.25f;
            if(mStars[i][0] < -0.25) mStars[i][0] = 1.25f;
            if(mStars[i][1] > 1.25) mStars[i][1] =  -0.25f;
            if(mStars[i][1] < -0.25) mStars[i][1] = 1.25f;
        }
        for (int i = 0; i < 5; i++) {
            mPlanetPosition[i][0] -= v.getX() * mPlanetPosition[i][2];
            mPlanetPosition[i][1] -= v.getY() * mPlanetPosition[i][2];
            if(mPlanetPosition[i][0] > 1.25) mPlanetPosition[i][0] = -0.25f;
            if(mPlanetPosition[i][0] < -0.25) mPlanetPosition[i][0] = 1.25f;
            if(mPlanetPosition[i][1] > 1.25) mPlanetPosition[i][1] = -0.25f;
            if(mPlanetPosition[i][1] < -0.25) mPlanetPosition[i][1] = 1.25f;
        }
    }
}
