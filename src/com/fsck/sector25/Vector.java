package com.fsck.sector25;

public class Vector {

    private float mX;
    private float mY;

    public Vector(float x, float y) {
        mX = x;
        mY = y;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public Vector add(Vector v) {
        Vector temp = new Vector(mX + v.mX, mY + v.mY);
        return temp;
    }

    public Vector sub(Vector v) {
        Vector temp = new Vector(mX - v.mX, mY - v.mY);
        return temp;
    }

    public float dotProduct(Vector v) {
        return mX * v.mX + mY * v.mY;
    }

    public Vector scale(float a) {
        return new Vector(mX * a, mY * a);
    }

    public Vector normalize() {
        float a = mag();
        if (a == 0) {
            return new Vector(0, 0);
        }
        return new Vector(mX / a, mY / a);
    }

    public float mag() {
        return (float) Math.sqrt(Math.pow(mX, 2) + Math.pow(mY, 2));
    }

    public static Vector random() {
        return new Vector((float) Math.random(), (float) Math.random());
    }

    public float angle() {
        return (float) Math.toDegrees(Math.atan(mY / Math.abs(mX)));
    }

    public boolean isZero() {
        return mX == 0 && mY == 0;
    }

    public static Vector zero() {
        return new Vector(0, 0);
    }

    public Vector rotate(float radians) {
        float x_p = (float) (Math.cos(radians) * mX - Math.sin(radians) * mY);
        float y_p = (float) (Math.sin(radians) * mX + Math.cos(radians) * mY);
        return new Vector(x_p, y_p);
    }
}
