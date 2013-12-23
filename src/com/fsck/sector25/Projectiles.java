package com.fsck.sector25;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Projectiles {

    private static ArrayList<float[]> mProjectiles = new ArrayList<float[]>();
    private static Sprite[] mSprite = new Sprite[2];
    private static final int DEFAULT_VELOCITY = 25;
    private static float mMaxDistance;
    public static final float hitboxRadius = 12;

    public Projectiles(Resources res) {
        mSprite[0] = new Sprite(BitmapFactory.decodeResource(res, R.drawable.laze), 28);
        mSprite[1] = new Sprite(BitmapFactory.decodeResource(res, R.drawable.laze2), 28);
    }

    public void draw(Canvas canvas, Paint paint) {
        for (float[] i : mProjectiles) {
            // TODO: make mSprite frames draw according to i[4]
            mSprite[(int) i[5]].draw(canvas, (int) i[0], (int) i[1]);
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
            if (projectile[5] == 0) {
                float xdist = projectile[0] - enemy[0];
                float ydist = projectile[1] - enemy[1];
                float distance = (float) Math.sqrt(xdist * xdist + ydist * ydist);
                if (distance < enemy[2] + hitboxRadius) {
                    mProjectiles.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public void testCharacterHit() {
        for (int i = mProjectiles.size() - 1; i >= 0; i--) {
            float[] projectile = mProjectiles.get(i);
            if (projectile[5] == 1) {
                if (Character.testCollision(new Point(projectile[0], projectile[1]), hitboxRadius)) {
                    GameHUD.incrementHealth(-3);
                    mProjectiles.remove(i);
                }
            }
        }
    }

    public void update(float dx, float dy) {
        for (int i = mProjectiles.size() - 1; i >= 0; i--) {
            float[] projectile = mProjectiles.get(i);

            // increments frame of each projectile
            projectile[4] = (projectile[4] + 1) % 28;

            // movement (projectile speed - character speed)
            projectile[0] = (float) (projectile[0] + projectile[2] - dx);
            projectile[1] = (float) (projectile[1] + projectile[3] - dy);

            // check if it is far enough away to just remove
            float xDif = projectile[0] - Character.getX();
            float yDif = projectile[1] - Character.getY();
            if (Math.sqrt(xDif * xDif + yDif * yDif) > mMaxDistance)
                mProjectiles.remove(i);
        }

        mSprite[0].Update();
        mSprite[1].Update();
    }

    public static void add(float x, float y, float vx, float vy, int team) {
        add(x, y, vx, vy, team, DEFAULT_VELOCITY);
    }

    public static void add(float x, float y, float vx, float vy, int team,
            int velocity) {
        float[] newProjectile = new float[6];
        newProjectile[0] = x; // mPosition x
        newProjectile[1] = y; // mPosition y

        double a = Math.sqrt((double) (vx * vx + vy * vy));
        newProjectile[2] = (float) (vx / a * velocity)
                + GameHUD.getLeftVector().scale(sector25view.VELOCITY_SCALE / 2)
                        .getX(); // mVelocity x
        newProjectile[3] = (float) (vy / a * velocity)
                + GameHUD.getLeftVector().scale(sector25view.VELOCITY_SCALE / 2)
                        .getY(); // mVelocity y

        newProjectile[4] = 0; // initial frame
        newProjectile[5] = team; // character's or enemy's
        mProjectiles.add(newProjectile);
    }

    public void clear() {
        mProjectiles = new ArrayList<float[]>();
    }

    public static void setmMaxDistance(float maxDistance) {
        mMaxDistance = maxDistance;
    }
}
