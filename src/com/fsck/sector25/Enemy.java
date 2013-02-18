package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {

    private static int width;
    private static int height;
    private static Bitmap sprite;

    private Point position;
    private Vector velocity;
    private float maxVelocity;

    private int aimedAt = 0;

    private static final float hitboxRadius = 15;

    public Enemy(Point characterPos) {
        position = new Point(0, 0);
        velocity = new Vector(0, 0);
        setPosition(characterPos);
    }

    public static void set(Resources res, int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
        sprite = BitmapFactory.decodeResource(res, R.drawable.enemy);
        sprite = Bitmap.createScaledBitmap(sprite, (int) (width / 20),
                (int) (width / 20), false);
    }

    private void setPosition(Point characterPos) {
        boolean good = false;
        Random r = new Random();
        maxVelocity = 5 + r.nextFloat() * 5;
        // get new position until it isn't on top of the character
        while (!good) {
            this.position.setX((r.nextFloat() * 2 - 0.5f) * 1.5f * width);
            this.position.setY((r.nextFloat() * 2 - 0.5f) * 1.5f * height);
            if (position.distance(characterPos) > (height / 2)) {
                good = true;
            }
        }
    }

    public void update(Vector charVelocity, Point characterPos) {

        velocity = position.unitVecTo(characterPos).scale(maxVelocity)
                .sub(charVelocity).add(Vector.random());

        // jump around if character runs too far away
        // intent is to make the player not be able to run away forever
        if (position.distance(characterPos) > 2 * width) {
            setPosition(characterPos);
        }
        position = position.move(velocity);
    }

    public float[] getHitBox() {
        float[] hitbox = new float[3];
        hitbox[0] = position.getX();
        hitbox[1] = position.getY();
        hitbox[2] = hitboxRadius;
        return hitbox;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(position.getX(), position.getY());

        canvas.drawBitmap(sprite, -sprite.getWidth() / 2,
                -sprite.getHeight() / 2, paint);
        canvas.restore();
    }

    public void drawHit(Canvas canvas, Paint paint) {
        canvas.drawCircle(position.getX(), position.getY(), hitboxRadius, paint);
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
}
