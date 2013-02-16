package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {

    private int width;
    private int height;
    private float targetX;
    private float targetY;
    private Point position;
    private Vector velocity;
    private float maxVelocity;
    private int direction;
    private Bitmap sprite;

    private static final float hitboxRadius = 15;

    public Enemy(Bitmap enemy, int width, int height, Point characterPos) {
        sprite = enemy;
        position = new Point(0, 0);
        velocity = new Vector(0, 0);
        maxVelocity = 8;
        sprite = Bitmap.createScaledBitmap(sprite, (int) (width / 20),
                (int) (width / 20), false);
        this.height = width;
        this.width = height;
        setPosition(characterPos);
        targetX = width / 2;
        targetY = height / 2;
    }

    private void setPosition(Point characterPos) {
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

    private float distance(float x1, float y1, float x2, float y2) {
        float xDif = x1 - x2;
        float yDif = y1 - y2;
        return (float) Math.sqrt(xDif * xDif + yDif * yDif);
    }

    public void update(Vector charVelocity, Point characterPos) {

        Vector moveDirection = position.unitVecTo(characterPos);
        moveDirection = moveDirection.scale(maxVelocity);
        moveDirection = moveDirection.sub(charVelocity);

        // float deltaX = targetX - this.x;
        // float deltaY = targetY - this.y;
        // double vy, vx;
        //
        // // Head straight to the character's position
        // vx = maxVelocity * Math.sin(Math.atan2(deltaX, deltaY));
        // vy = maxVelocity * Math.cos(Math.atan2(deltaX, deltaY));
        //
        // this.x += vx;
        // this.x -= x * sector25view.VELOCITY_SCALE;
        // this.y += vy;
        // this.y -= y * sector25view.VELOCITY_SCALE;

        // Add some randomization to reduce stacking
        // this.x += Math.random() * velocity * Math.signum(vx);
        // this.y += Math.random() * velocity * Math.signum(vy);
        moveDirection = moveDirection.add(Vector.random());

        // jump around if character runs too far away
        // intent is to make the player not be able to run away forever
        if (position.distance(characterPos) > 2 * width) {
            setPosition(characterPos);
        }
        position = position.move(moveDirection);
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
    
    public Point getPosition(){
        return position;
    }
}
