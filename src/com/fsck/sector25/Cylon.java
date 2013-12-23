package com.fsck.sector25;

import java.util.Random;

import android.graphics.Color;

public class Cylon extends Enemy {

    protected static int mMaxHealth = 1;
    protected static int mColor = Color.CYAN;
    protected static int mScore = 10;

    public Cylon() {
        mPosition = new Point(0, 0);
        mVelocity = new Vector(0, 0);
        setPosition();
        mCurrHealth = mMaxHealth;
        mMaxVelocity = 4 + new Random().nextFloat() * 4;
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
