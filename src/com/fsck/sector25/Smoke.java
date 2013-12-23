package com.fsck.sector25;

import java.util.ArrayList;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Smoke {
    ArrayList<float[]> mSmokes = new ArrayList<float[]>();
    private static Bitmap mSprite;
    protected static final Random mRandom = new Random();

    public Smoke(Resources res) {
        mSprite = BitmapFactory.decodeResource(res, R.drawable.smoke);
    }

    public void draw(Canvas canvas, Paint paint) {
        for (int i = 0; i < mSmokes.size(); i++) {
            float[] j = mSmokes.get(i);
            paint.setAlpha((int) j[2]);
            canvas.rotate(j[5], j[0] - mSprite.getWidth() / 2,
                    j[1] - mSprite.getHeight() / 2);
            canvas.drawBitmap(mSprite, j[0] - mSprite.getWidth(), j[1]
                    - mSprite.getHeight(), paint);
            paint.setAlpha(255);
            canvas.rotate(-1 * j[5], j[0] - mSprite.getWidth() / 2, j[1]
                    - mSprite.getHeight() / 2);
        }
    }

    public void update() {
        for (int i = mSmokes.size() - 1; i >= 0; i--) {
            float[] j = mSmokes.get(i);

            // move away from character
            j[0] += j[3];
            j[1] += j[4];

            // Decrease alpha until gone
            j[2] -= 10;
            if (j[2] <= 0)
                mSmokes.remove(i);
            else
                mSmokes.set(i, j);
        }
    }

    public void add(Point position, Vector velocity) {
        float[] newSmoke = new float[6];
        newSmoke[0] = position.getX(); // mPosition x
        newSmoke[1] = position.getY(); // mPosition y
        newSmoke[2] = 255; // alpha
        newSmoke[3] = velocity.getX(); // mVelocity x
        newSmoke[4] = velocity.getY(); // mVelocity y
        newSmoke[5] = mRandom.nextFloat() * 360;
        mSmokes.add(newSmoke);
    }

    public void move(Vector v) {
        for (float[] i : mSmokes) {
            i[0] -= v.getX() / 10;
            i[1] -= v.getY() / 10;
        }
    }

    public void clear() {
        mSmokes = new ArrayList<float[]>();
    }
}
