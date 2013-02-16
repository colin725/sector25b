package com.fsck.sector25;

public class Point {

    private float x;
    private float y;

    public Point(){
        x = 0;
        y = 0;
    }
    
    public Point(float x, float y) {
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

    public float distance(Point p) {
        double x2 = Math.pow(x - p.x, 2);
        double y2 = Math.pow(y - p.y, 2);
        double r = Math.sqrt(x2 + y2);
        return (float) r;
    }

    public Vector unitVecTo(Point p) {
        float deltaX = p.x - this.x;
        float deltaY = p.y - this.y;

        Vector temp = new Vector((float) Math.sin(Math.atan2(deltaX, deltaY)),
                (float) Math.cos(Math.atan2(deltaX, deltaY)));
        return temp.normalize();
    }

    public Point move(Vector v) {
        return new Point(this.x + v.getX(), this.y + v.getY());
    }
    
    public static Point random(){
        return new Point((float) Math.random(), (float) Math.random());
    }
}
