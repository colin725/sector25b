package com.fsck.sector25;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public abstract class Enemy {

    protected static int mWidth;
    protected static int mHeight;
    protected static final boolean mHealthbars = false;
    protected static final int mRadius = 22;
    protected Point mPosition;
    protected Vector mVelocity;
    protected float mMaxVelocity;
    protected int mCurrHealth;
    protected int mAimedAt = 0;

    public Enemy(Point characterPos) {
    }

    public abstract void update(Vector charVelocity, Point characterPos);
    public abstract int getScore();
    public abstract int getColor();
    public abstract int getMaxHealth();

    public float[] getHitBox() {
        float[] hitbox = new float[3];
        hitbox[0] = mPosition.getX();
        hitbox[1] = mPosition.getY();
        hitbox[2] = mRadius;
        return hitbox;
    }

    public void takeDamage(int damage) {
        this.mCurrHealth -= damage;
    }

    public boolean isDead() {
        return mCurrHealth <= 0;
    }

    protected void setPosition(Point characterPos) {
        boolean good = false;
        Random r = new Random();
        // get new mPosition until it isn't on top of the character
        while (!good) {
            this.mPosition.setX((r.nextFloat() * 2 - 0.5f) * 1.5f * mWidth);
            this.mPosition.setY((r.nextFloat() * 2 - 0.5f) * 1.5f * mHeight);
            if (mPosition.distance(characterPos) > (mHeight / 2)) {
                good = true;
            }
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(mPosition.getX(), mPosition.getY());

        canvas.drawCircle(-getRadius() / 2, -getRadius() / 2, 20, paint);

        paint.setStyle(Style.STROKE);
        paint.setColor(getColor());
        paint.setStrokeWidth(getRadius() / 3);
        canvas.drawCircle(-getRadius() / 2, -getRadius() / 2, 20, paint);

        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        canvas.restore();
    }

    public void drawHit(Canvas canvas, Paint paint) {
        canvas.drawCircle(mPosition.getX(), mPosition.getY(), mRadius, paint);
    }

    public void drawHealth(Canvas canvas, Paint paint) {
        if (mHealthbars) {
            int color = paint.getColor();
            float healthRatio = ((float) mCurrHealth) / ((float) getMaxHealth());
            if (healthRatio <= .2 || (mCurrHealth == 1 && getMaxHealth() > 1)) {
                paint.setColor(Color.RED);
            } else if (healthRatio <= .5) {
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.GREEN);
            }
            canvas.drawRect(-getRadius() / 2, -getRadius() + 5,
                    -(getRadius() / 2) + (healthRatio * (getRadius())),
                    -getRadius(), paint);
            paint.setColor(color);
        }

        canvas.restore();
    }

    public int getRadius() {
        return mRadius;
    }

    public float getX() {
        return mPosition.getX();
    }

    public float getY() {
        return mPosition.getY();
    }

    public float getVX() {
        return mVelocity.getX();
    }

    public float getVY() {
        return mVelocity.getY();
    }

    public int aimed() {
        return mAimedAt;
    }

    public void aim() {
        mAimedAt = 1;
    }

    public void shot() {
        mAimedAt = 2;
    }

    public Point getPosition() {
        return mPosition;
    }

    public int getCurrHealth() {
        return mCurrHealth;
    }

    public int getDamage() {
        return 5;
    }
}
