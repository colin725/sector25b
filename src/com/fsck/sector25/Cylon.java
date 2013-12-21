package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Cylon extends Enemy {

    protected static int mEnemyDrawable;
    protected static Bitmap mSprite;
    protected static int mMaxHealth = 1;
    protected static int mColor = Color.CYAN;
    protected static int mScore = 10;

    static {
        mEnemyDrawable = R.drawable.cylon;
    }

    public Cylon(Point characterPos) {
        super(characterPos);
        mPosition = new Point(0, 0);
        mVelocity = new Vector(0, 0);
        setPosition(characterPos);
        mCurrHealth = mMaxHealth;
        mMaxVelocity = 4 + new Random().nextFloat() * 4;
    }

    @Override
    public void update(Vector charVelocity, Point characterPos) {

        // Calculate direction, scale up to mVelocity, subtract char mVelocity,
        // add small amount of randomness
        mVelocity = mPosition.unitVecTo(characterPos).scale(mMaxVelocity)
                .sub(charVelocity).add(Vector.random());
        mPosition = mPosition.move(mVelocity);

        // respawn randomly if character runs too far away
        // intent is to make the player not be able to run away forever
        if (mPosition.distance(characterPos) > 2 * mWidth) {
            setPosition(characterPos);
        }
    }

    // is there a good pattern to have this in the base class without being ugly?
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
