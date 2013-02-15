package com.fsck.sector25;

import java.util.ArrayList;

import com.fsck.sector25.sector25view.sector25thread;

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
    private static final float hitboxRadius = 12;

    public Projectiles(Resources res, sector25thread parentThread) {
        sprite = new Sprite(BitmapFactory.decodeResource(res, R.drawable.laze),
                28);
    }

    public void draw(Canvas canvas, Paint paint) {
        for (float[] i : projectiles) {
            // TODO: make sprite frames draw according to i[4]
            sprite.draw(canvas, (int) i[0], (int) i[1]);
        }
    }

    public void drawHit(Canvas canvas, Paint paint) {
        for (float[] i : projectiles) {
            // TODO: make sprite frames draw according to i[4]
            canvas.drawCircle(i[0], i[1], 12, paint);
        }
    }

    public boolean testHit(float[] enemy) {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            float[] projectile = projectiles.get(i);
            float xdist = projectile[0] - enemy[0];
            float ydist = projectile[1] - enemy[1];
            float distance = (float) Math.sqrt(xdist * xdist + ydist * ydist);
            if (distance < enemy[2] + hitboxRadius) {
                projectiles.remove(i);
                return true;
            }
        }
        return false;
    }

    public void update(float dx, float dy, float characterX, float characterY, float maxDistance) {
        for (float[] i : projectiles) {
            // increments frame of each projectile
            i[4] = (i[4] + 1) % 28;
            // movement (projectile speed - character speed)
            i[0] = (float) (i[0] + i[2] - dx / 3);
            i[1] = (float) (i[1] + i[3] - dy / 3);
        }

        for (int i = projectiles.size() - 1; i >= 0; i--) {
            float[] projectile = projectiles.get(i);
            float xDif = projectile[0] - characterX;
            float yDif = projectile[1] - characterY;
            if (Math.sqrt(xDif * xDif + yDif * yDif) > maxDistance)
                projectiles.remove(i);
        }

        sprite.Update();
    }

    public void add(float x, float y, float vx, float vy) {
        float[] newProjectile = new float[5];
        newProjectile[0] = x; // position x
        newProjectile[1] = y; // position y

        double a = Math.sqrt((double) (vx * vx + vy * vy));
        newProjectile[2] = (float) (vx / a * 25); // velocity x
        newProjectile[3] = (float) (vy / a * 25); // velocity y

        newProjectile[4] = 0; // initial frame
        projectiles.add(newProjectile);
    }
}
