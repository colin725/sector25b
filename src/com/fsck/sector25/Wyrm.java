package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Wyrm extends Enemy {

    protected static int mEnemyDrawable;
    protected static Bitmap mSprite;
    protected static int mMaxHealth = 5;
    protected static int mColor = Color.YELLOW;
    protected static int mScore = 50;
    private int mRotationDirection;
    private Point mRotationCenter;

    static {
        mEnemyDrawable = R.drawable.cylon;
    }

    public Wyrm(Point characterPos) {
        super(characterPos);
        mPosition = new Point(0, 0);
        mRotationCenter = new Point(0, 0);
        mVelocity = new Vector(0, 0);
        setPosition(characterPos);
        mCurrHealth = mMaxHealth;
        Random r = new Random();
        mMaxVelocity = 4 + r.nextFloat() * 4;
        mRotationDirection = r.nextBoolean() ? 1 : -1;
    }

    @Override
    protected void setPosition(Point charPoint) {
        super.setPosition(charPoint);
        Vector direction = mPosition.unitVecTo(charPoint);
        float dist = mPosition.distance(charPoint) / 2;
        mRotationCenter.setX(direction.getX() * dist);
        mRotationCenter.setY(direction.getY() * dist);
        mVelocity = mPosition.unitVecTo(mRotationCenter)
                .rotate((float) (mRotationDirection * Math.PI / 2f))
                .scale(mMaxVelocity);
    }

    @Override
    public void update(Vector charVelocity, Point characterPos) {
        // Calculate direction, scale up to mVelocity, subtract char mVelocity,
        // add small amount of randomness
        // mVelocity = mPosition.unitVecTo(characterPos)
        // .add(charVelocity.normalize()).scale(mMaxVelocity)
        // .sub(charVelocity).add(Vector.random());

        Vector v = mRotationCenter.unitVecTo(characterPos);
        mRotationCenter = mRotationCenter.move(v);

        mVelocity = mVelocity.add(mPosition.unitVecTo(mRotationCenter))
                .normalize().scale(mMaxVelocity);

        mVelocity = mVelocity.sub(charVelocity);

        // jump around if character runs too far away
        // intent is to make the player not be able to run away forever
        if (mPosition.distance(characterPos) > 2 * mWidth) {
            setPosition(characterPos);
        }
        mPosition = mPosition.move(mVelocity);
    }

    // is there a good pattern to have this in the base class without being
    // ugly?
    public static void setSize(Resources res, int screenWidth, int screenHeight) {
        mWidth = screenWidth;
        mHeight = screenHeight;
        mSprite = BitmapFactory.decodeResource(res, mEnemyDrawable);
        mSprite = Bitmap.createScaledBitmap(mSprite, (int) (mWidth / 20),
                (int) (mWidth / 20), false);
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
