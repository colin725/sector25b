package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Cylon extends Enemy {

    private static final float hitboxRadius = 20;
    protected static int enemyDrawable;
    protected static Bitmap sprite;

    static {
        enemyDrawable = R.drawable.cylon;
    }

    public Cylon(Point characterPos, int maxHealth) {
        super(characterPos, maxHealth);
        position = new Point(0, 0);
        velocity = new Vector(0, 0);
        setPosition(characterPos);
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
        this.score = 10;
        Random r = new Random();
        maxVelocity = 4 + r.nextFloat() * 4;
        color = Color.CYAN;
    }

    @Override
    public void update(Vector charVelocity, Point characterPos) {

        // Calculate direction, scale up to velocity, subtract char velocity,
        // add small amount of randomness
        velocity = position.unitVecTo(characterPos).scale(maxVelocity)
                .sub(charVelocity).add(Vector.random());

        // jump around if character runs too far away
        // intent is to make the player not be able to run away forever
        if (position.distance(characterPos) > 2 * width) {
            setPosition(characterPos);
        }
        position = position.move(velocity);
    }

    @Override
    public void takeDamage(int damage) {
        this.currHealth -= damage;
    }

    @Override
    public boolean isDead() {
        return currHealth <= 0;
    }

    @Override
    public float[] getHitBox() {
        float[] hitbox = new float[3];
        hitbox[0] = position.getX();
        hitbox[1] = position.getY();
        hitbox[2] = hitboxRadius;
        return hitbox;
    }

    @Override
    public void drawHit(Canvas canvas, Paint paint) {
        canvas.drawCircle(position.getX(), position.getY(), hitboxRadius, paint);
    }

    public static void setSize(Resources res, int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
        sprite = BitmapFactory.decodeResource(res, enemyDrawable);
        sprite = Bitmap.createScaledBitmap(sprite, (int) (width / 20),
                (int) (width / 20), false);
    }

}
