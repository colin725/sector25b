package com.fsck.sector25;

public class Point {

    private float mX;
    private float mY;

    public Point(){
        mX = 0;
        mY = 0;
    }
    
    public Point(float x, float y) {
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

    public float distance(Point p) {
        double x2 = Math.pow(mX - p.mX, 2);
        double y2 = Math.pow(mY - p.mY, 2);
        double r = Math.sqrt(x2 + y2);
        return (float) r;
    }

    public Vector unitVecTo(Point p) {
        float deltaX = p.mX - mX;
        float deltaY = p.mY - mY;

        Vector temp = new Vector((float) Math.sin(Math.atan2(deltaX, deltaY)),
                (float) Math.cos(Math.atan2(deltaX, deltaY)));
        return temp.normalize();
    }

    public Point move(Vector v) {
        return new Point(mX + v.getX(), mY + v.getY());
    }

    public void subtract(Vector v) {
        mX -= v.getX();
        mY -= v.getY();
    }
 
    public static Point random(){
        return new Point((float) Math.random(), (float) Math.random());
    }
}
