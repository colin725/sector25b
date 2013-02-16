package com.fsck.sector25;

public class Vector {

    private float x;
    private float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Vector add(Vector v) {
        Vector temp = new Vector(this.x + v.x, this.y + v.y);
        return temp;
    }

    public Vector sub(Vector v) {
        Vector temp = new Vector(this.x - v.x, this.y - v.y);
        return temp;
    }

    public float dotProduct(Vector v) {
        return this.x * v.x + this.y * v.y;
    }

    public Vector scale(float a) {
        return new Vector(this.x * a, this.y * a);
    }

    public Vector normalize() {
        float a = mag();
        if (a == 0) {
            return new Vector(0, 0);
        }
        return new Vector(this.x / a, this.y / a);
    }

    public float mag() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public static Vector random() {
        return new Vector((float) Math.random(), (float) Math.random());
    }

    public float angle() {
        return (float) Math.toDegrees(Math.atan(y / Math.abs(x)));
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public static Vector zero() {
        return new Vector(0, 0);
    }

}
