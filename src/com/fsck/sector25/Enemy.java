package com.fsck.sector25;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Enemy {

    protected static int width;
    protected static int height;

    protected Point position;
    protected Vector velocity;
    protected float maxVelocity;
    protected int currHealth;
    protected int maxHealth;
    protected int score;

    private int aimedAt = 0;

    public Enemy(Point characterPos, int maxHealth) {

    }

    public abstract void update(Vector charVelocity, Point characterPos);

    public abstract float[] getHitBox();

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract void drawHit(Canvas canvas, Paint paint);

    public abstract void takeDamage(int damage);

    public abstract boolean isDead();

    protected void setPosition(Point characterPos) {
        boolean good = false;
        Random r = new Random();
        // get new position until it isn't on top of the character
        while (!good) {
            this.position.setX((r.nextFloat() * 2 - 0.5f) * 1.5f * width);
            this.position.setY((r.nextFloat() * 2 - 0.5f) * 1.5f * height);
            if (position.distance(characterPos) > (height / 2)) {
                good = true;
            }
        }
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getVX() {
        return velocity.getX();
    }

    public float getVY() {
        return velocity.getY();
    }

    public int aimed() {
        return aimedAt;
    }

    public void aim() {
        aimedAt = 1;
    }

    public void shot() {
        aimedAt = 2;
    }

    public Point getPosition() {
        return position;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getScore() {
        return score;
    }
}
