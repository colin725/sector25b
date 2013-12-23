package com.fsck.sector25;

import java.util.Random;

import android.graphics.Color;
import android.os.SystemClock;

public class Wyrm extends Enemy {

    protected static int mMaxHealth = 5;
    protected static int mColor = Color.YELLOW;
    protected static int mScore = 50;
    private int mRotationDirection;
    protected long mLastShot;
    private Point mRotationCenter;

    public Wyrm() {
        mPosition = new Point(0, 0);
        mRotationCenter = new Point(0, 0);
        mVelocity = new Vector(0, 0);
        setPosition();
        mCurrHealth = mMaxHealth;
        Random r = new Random();
        mMaxVelocity = 4 + r.nextFloat() * 4;
        mRotationDirection = r.nextBoolean() ? 1 : -1;
    }

    @Override
    protected void setPosition() {
        super.setPosition();
        Vector direction = mPosition.unitVecTo(Character.getPosition());
        float dist = mPosition.distance(Character.getPosition()) / 2;
        mRotationCenter.setX(direction.getX() * dist);
        mRotationCenter.setY(direction.getY() * dist);
        mVelocity = mPosition.unitVecTo(mRotationCenter)
                .rotate((float) (mRotationDirection * Math.PI / 2f))
                .scale(mMaxVelocity);
    }

    @Override
    public void update(Vector charVelocity) {
        // Calculate direction, scale up to mVelocity, subtract char mVelocity,
        // add small amount of randomness
        // mVelocity = mPosition.unitVecTo(characterPos)
        // .add(charVelocity.normalize()).scale(mMaxVelocity)
        // .sub(charVelocity).add(Vector.random());

        Vector v = mRotationCenter.unitVecTo(Character.getPosition());
        mRotationCenter = mRotationCenter.move(v);

        mVelocity = mVelocity.add(mPosition.unitVecTo(mRotationCenter))
                .normalize().scale(mMaxVelocity);

        mVelocity = mVelocity.sub(charVelocity);

        // jump around if character runs too far away
        // intent is to make the player not be able to run away forever
        if (mPosition.distance(Character.getPosition()) > 2 * mWidth) {
            setPosition();
        }
        mPosition = mPosition.move(mVelocity);

        if (SystemClock.currentThreadTimeMillis() - mLastShot > 6000) {
            int vx = (int) (Character.getX() - mPosition.getX());
            int vy = (int) (Character.getY() - mPosition.getY());
            Projectiles.add(mPosition.getX(), mPosition.getY(), vx, vy, 1, 10);
            mLastShot = SystemClock.currentThreadTimeMillis();
        }
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
