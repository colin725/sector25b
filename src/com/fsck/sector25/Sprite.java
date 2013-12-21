package com.fsck.sector25;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    private Bitmap mAnimation;
    private Rect mRectangle;
    public int mNumFrames;
    public int mCurrentFrame;
    private int mSpriteHeight;
    private int mSpriteWidth;
    private int mUpdate;

    public Sprite() {
    }

    public Sprite(Bitmap bitmap, int frameCount) {
        mRectangle = new Rect(0, 0, 0, 0);
        mCurrentFrame = 0;

        mAnimation = bitmap;
        mSpriteHeight = bitmap.getHeight();
        mSpriteWidth = bitmap.getWidth() / frameCount;
        mRectangle.top = 0;
        mRectangle.bottom = mSpriteHeight;
        mRectangle.left = 0;
        mRectangle.right = mSpriteWidth;
        mNumFrames = frameCount;
    }

    public void Update() {
        mUpdate++;
        if (mUpdate % 2 == 0) {
            mCurrentFrame = (mCurrentFrame + 1) % mNumFrames;
            mRectangle.left = mCurrentFrame * mSpriteWidth;
            mRectangle.right = mRectangle.left + mSpriteWidth;
        }
    }

    public void setFrame(int frame) {
        mCurrentFrame = frame;
        mRectangle.left = mCurrentFrame * mSpriteWidth;
        mRectangle.right = mRectangle.left + mSpriteWidth;
    }

    public int getWidth(){
        return mAnimation.getWidth()/mNumFrames;
    }

    public int getHeight(){
        return mAnimation.getHeight();
    }

    public void draw(Canvas canvas, int x, int y) {
        Rect dest = new Rect(x - mSpriteWidth/2, y - mSpriteHeight/2, x + mSpriteWidth/2,
                y + mSpriteHeight/2);
        canvas.drawBitmap(mAnimation, mRectangle, dest, null);
    }
}
