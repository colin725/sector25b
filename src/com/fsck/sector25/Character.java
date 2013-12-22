package com.fsck.sector25;

import com.fsck.sector25.sector25view.GameState;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Character {

    private Point mPosition;
    private Point mSmokePosition;
    private int mDirection;
    private float mDirectionGun;
    private Sprite mSpaceman;
    private float[][] mHitbox = new float[3][3];
    private int mWidth;
    private int mHeight;
    private Bitmap mMan;
    private Bitmap mArm1;
    private Bitmap mArm2;
    private Bitmap mGun;

    public Character(Resources res) {
        mMan = BitmapFactory.decodeResource(res, R.drawable.spaceman);
        mArm1 = BitmapFactory.decodeResource(res, R.drawable.arm1);
        mArm2 = BitmapFactory.decodeResource(res, R.drawable.arm2);
        mGun = BitmapFactory.decodeResource(res, R.drawable.gun);
    }

    public void setSize(int width, int height) {
        mPosition = new Point(width / 2, height * 2 / 5);
        this.mWidth = width;
        this.mHeight = height;

        mSpaceman = new Sprite(Bitmap.createScaledBitmap(mMan, width / 5,
                width / 5, false), 2);
        mArm1 = Bitmap.createScaledBitmap(mArm1, width * 1 / 30,
                width * 1 / 40, false);
        mArm2 = Bitmap.createScaledBitmap(mArm2, width * 1 / 30,
                width * 1 / 45, false);
        mGun = Bitmap.createScaledBitmap(mGun, width * 1 / 13, width * 1 / 26,
                false);

        mSmokePosition = new Point(getSmokeX(), getSmokeY());

        mHitbox[0] = new float[] { 0, mSpaceman.getHeight() / 4, 20 };
        mHitbox[1] = new float[] { 0, -mSpaceman.getHeight() / 7, 30 };
        mHitbox[2] = new float[] { 0, 0, 20 };
    }

    public void setPositionMenu() {
        mPosition = new Point((int)(mWidth / 3.5), mHeight / 2);
        mSmokePosition = new Point(getSmokeX(), getSmokeY());
    }

    public void setPositionDefault() {
        mPosition = new Point(mWidth / 2, mHeight * 2 / 5);
        mSmokePosition = new Point(getSmokeX(), getSmokeY());
    }

    public float getShotX() {
        float shotx = (float) (mSpaceman.getWidth() * 2 / 3 * Math.cos(Math
                .toRadians(mDirectionGun)));
        return mPosition.getX() + shotx - 2 * shotx * mDirection;
    }

    public float getShotY() {
        float shoty = (float) (mSpaceman.getWidth() * 2 / 3 * Math.sin(Math
                .toRadians(mDirectionGun)));
        return mPosition.getY() + shoty;
    }

    public Vector getShot() {
        float shotx = (float) (mSpaceman.getWidth() * 2 / 3 * Math.cos(Math
                .toRadians(mDirectionGun)));
        float shoty = (float) (mSpaceman.getWidth() * 2 / 3 * Math.sin(Math
                .toRadians(mDirectionGun)));
        return new Vector(shotx, shoty);
    }

    private float getSmokeX() {
        return (mPosition.getX() - mSpaceman.getWidth() / 4 + mDirection
                * mSpaceman.getWidth() / 2 + mSpaceman.getWidth() / 4);
    }

    private float getSmokeY() {
        return mPosition.getY() + mSpaceman.getHeight() / 4;
    }

    public Point getSmokePosition() {
        return mSmokePosition;
    }

    public Vector getSmokeVelocity() {
        return new Vector(getSmokeVX(), getSmokeVY());
    }

    private float getSmokeVX() {
        return -(mPosition.getX() - getSmokeX()) / 50;
    }

    private float getSmokeVY() {
        return (mPosition.getY() - getSmokeY()) / 50;
    }

    public float getX() {
        return mPosition.getX();
    }

    public float getY() {
        return mPosition.getY();
    }

    public Point getPosition() {
        return mPosition;
    }

    public void update(Vector velocity, Vector aim) {
        // Set character mDirection and frame
        mSpaceman.setFrame(1);
        if (velocity.getX() > 0) {
            mDirection = 0;
            mSpaceman.setFrame(0);
        } else if (velocity.getX() < 0) {
            mDirection = 1;
            mSpaceman.setFrame(0);
        }
        if (aim.getX() > 0) {
            mDirection = 0;
        } else if (aim.getX() < 0) {
            mDirection = 1;
        }

        // Set mGun mDirection
        if (!aim.isZero()) {
            mDirectionGun = aim.angle();
        }

        mSmokePosition.setX(getSmokeX());
        mSmokePosition.setY(getSmokeY());

    }

    public void draw(Canvas canvas, Paint paint, GameState state, int page) {

        if (!(state == GameState.STATE_MENU && page != 0)) {

            canvas.save();
            canvas.translate(mPosition.getX(), mPosition.getY());
            if (mDirection == 1)
                canvas.scale(-1, 1);
            canvas.save();

            canvas.rotate(mDirectionGun - 5, -mSpaceman.getWidth() / 6, 0);
            canvas.drawBitmap(mArm2, 0, 0, paint);
            canvas.restore();

            mSpaceman.draw(canvas, 0, 0);

            canvas.rotate(mDirectionGun - 5, -mSpaceman.getWidth() / 12,
                    mSpaceman.getWidth() / 15);
            canvas.drawBitmap(mGun, -mSpaceman.getWidth() / 15, 0, paint);
            canvas.drawBitmap(mArm1, -mSpaceman.getWidth() / 9,
                    mSpaceman.getWidth() / 15, paint);

            canvas.restore();
        }
    }

    public float[][] getHitBox() {
        return mHitbox;
    }

    public boolean testHit(float[] enemy) {
        for (int i = 0; i < mHitbox.length; i++) {
            float xdist = (mPosition.getX() + mHitbox[i][0]) - enemy[0];
            float ydist = (mPosition.getY() + mHitbox[i][1]) - enemy[1];
            float distance = (float) Math.sqrt(xdist * xdist + ydist * ydist);
            if (distance < enemy[2] + mHitbox[i][2]) {
                return true;
            }
        }
        return false;
    }

    public void drawHit(Canvas canvas, Paint paint) {
        // TODO: better hitboxes? Might be fine like this.
        canvas.drawCircle(mPosition.getX() + mHitbox[0][0], mPosition.getY()
                + mHitbox[0][1], mHitbox[0][2], paint);
        canvas.drawCircle(mPosition.getX() + mHitbox[1][0], mPosition.getY()
                + mHitbox[1][1], mHitbox[1][2], paint);
        canvas.drawCircle(mPosition.getX() + mHitbox[2][0], mPosition.getY()
                + mHitbox[2][1], mHitbox[2][2], paint);
    }
}
