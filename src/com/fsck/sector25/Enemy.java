package com.fsck.sector25;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public abstract class Enemy {

    protected static int width;
    protected static int height;
    protected static final boolean healthbars = false;

    protected Point position;
    protected Vector velocity;
    protected float maxVelocity;
    protected int currHealth;
    protected int maxHealth;
    protected int score;
    protected int color;
    protected final int radius = 22;

    private int aimedAt = 0;

    public Enemy(Point characterPos, int maxHealth) {

    }

    public abstract void update(Vector charVelocity, Point characterPos);

    public abstract float[] getHitBox();

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

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(position.getX(), position.getY());

        canvas.drawCircle(-radius / 2,
                -radius / 2, 20, paint);

        paint.setStyle(Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(radius/3);
        canvas.drawCircle(-radius / 2,
                -radius / 2, 20, paint);

        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        canvas.restore();
    }

    public void drawHealth(Canvas canvas, Paint paint) {
        if(healthbars) {
            int color = paint.getColor();
            float healthRatio = ((float) currHealth) / ((float) maxHealth);
            if (healthRatio <= .2 || (currHealth == 1 && maxHealth > 1)) {
                paint.setColor(Color.RED);
            } else if (healthRatio <= .5) {
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.GREEN);
            }
            canvas.drawRect(-radius / 2, -radius + 5,
                    -(radius / 2)
                            + (healthRatio * (radius)),
                    -radius, paint);
            paint.setColor(color);
        }

        canvas.restore();
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

    public int getDamage() {
        return 5;
    }
}
