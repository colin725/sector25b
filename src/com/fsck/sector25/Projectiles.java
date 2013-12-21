package com.fsck.sector25;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Projectiles {

    ArrayList<float[]> mProjectiles = new ArrayList<float[]>();
    private static Sprite mSprite;
    private static final float hitboxRadius = 12;

    public Projectiles(Resources res) {
        mSprite = new Sprite(BitmapFactory.decodeResource(res, R.drawable.laze),
                28);
    }

    public void draw(Canvas canvas, Paint paint) {
        for (float[] i : mProjectiles) {
            // TODO: make mSprite frames draw according to i[4]
            mSprite.draw(canvas, (int) i[0], (int) i[1]);
        }
    }

    // Draw hitboxes for testing purposes ~
    public void drawHit(Canvas canvas, Paint paint) {
        for (float[] i : mProjectiles) {
            canvas.drawCircle(i[0], i[1], 12, paint);
        }
    }

    public boolean testHit(float[] enemy) {
        for (int i = mProjectiles.size() - 1; i >= 0; i--) {
            float[] projectile = mProjectiles.get(i);
            float xdist = projectile[0] - enemy[0];
            float ydist = projectile[1] - enemy[1];
            float distance = (float) Math.sqrt(xdist * xdist + ydist * ydist);
            if (distance < enemy[2] + hitboxRadius) {
                mProjectiles.remove(i);
                return true;
            }
        }
        return false;
    }

    public void update(float dx, float dy, float characterX, float characterY, float maxDistance) {
        for (float[] i : mProjectiles) {
            // increments frame of each projectile
            i[4] = (i[4] + 1) % 28;
            // movement (projectile speed - character speed)
            i[0] = (float) (i[0] + i[2] - dx / 3);
            i[1] = (float) (i[1] + i[3] - dy / 3);
        }

        for (int i = mProjectiles.size() - 1; i >= 0; i--) {
            float[] projectile = mProjectiles.get(i);
            float xDif = projectile[0] - characterX;
            float yDif = projectile[1] - characterY;
            if (Math.sqrt(xDif * xDif + yDif * yDif) > maxDistance)
                mProjectiles.remove(i);
        }

        mSprite.Update();
    }

    public void add(float x, float y, float vx, float vy) {
        float[] newProjectile = new float[5];
        newProjectile[0] = x; // mPosition x
        newProjectile[1] = y; // mPosition y

        double a = Math.sqrt((double) (vx * vx + vy * vy));
        newProjectile[2] = (float) (vx / a * 25); // mVelocity x
        newProjectile[3] = (float) (vy / a * 25); // mVelocity y

        newProjectile[4] = 0; // initial frame
        mProjectiles.add(newProjectile);
    }

    public void clear() {
        mProjectiles = new ArrayList<float[]>();
    }
}
