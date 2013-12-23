package com.fsck.sector25;

import java.util.Random;

import android.graphics.Color;
import android.os.SystemClock;

public class Boss1 extends Enemy {

    protected static int mMaxHealth = 100;
    protected static int mColor = Color.RED;
    protected static int mScore = 2000;
    protected static long mLastShot;
    protected static final Random mRandom = new Random();

    public Boss1() {
        mPosition = new Point(0, 0);
        mVelocity = new Vector(0, 0);
        setPosition();
        mCurrHealth = mMaxHealth;
        mMaxVelocity = 8;
        setRadius(90);
    }

    @Override
    public void update(Vector charVelocity) {

        // Calculate direction, scale up to mVelocity, subtract char mVelocity,
        // add small amount of randomness
        Point characterPos = Character.getPosition();
        mVelocity = mPosition.unitVecTo(characterPos).scale(mMaxVelocity)
                .sub(charVelocity).add(Vector.random());
        mPosition = mPosition.move(mVelocity);

        // respawn randomly if character runs too far away
        // intent is to make the player not be able to run away forever
        if (mPosition.distance(characterPos) > 2 * mWidth) {
            setPosition();
        }

        if (SystemClock.currentThreadTimeMillis() - mLastShot > 1300) {
            for (int i = 1; i < 13; i++) {
                double random = mRandom.nextDouble();
                int vx = (int) (20 * Math.cos(360 * (i / 12 + random)));
                int vy = (int) (20 * Math.sin(360 * (i / 12 + random)));
                Projectiles.add(mPosition.getX(), mPosition.getY(), vx, vy, 1);
            }
            mLastShot = SystemClock.currentThreadTimeMillis();
        }
    }

    @Override
    protected void setPosition() {
        boolean good = false;
        Random r = new Random();
        while (!good) {
            this.mPosition.setX((r.nextFloat() * 2 - 0.5f) * mWidth);
            this.mPosition.setY((r.nextFloat() * 2 - 0.5f) * mHeight);
            if (mPosition.distance(Character.getPosition()) > mHeight) {
                good = true;
            }
        }
    }

    @Override
    public boolean isDead() {
        if (mCurrHealth <= 0) {
            GameHUD.bossKill();
        }
        return mCurrHealth <= 0;
    }

    @Override
    public int getScore() {
        return mScore;
    }

    @Override
    public int getColor() {
        return mColor;
    }

    @Override
    public int getMaxHealth() {
        return mMaxHealth;
    }
}
