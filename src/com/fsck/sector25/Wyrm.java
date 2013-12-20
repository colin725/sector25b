package com.fsck.sector25;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Wyrm extends Enemy {

    private static final float hitboxRadius = 15;
    protected static Bitmap sprite;
    protected static int enemyDrawable;
    private int rotationDirection;
    private Point rotationCenter;

    static {
        enemyDrawable = R.drawable.cylon;
    }

    public Wyrm(Point characterPos, int maxHealth) {
        super(characterPos, maxHealth);
        color = Color.YELLOW;
        position = new Point(0, 0);
        rotationCenter = new Point(0, 0);
        velocity = new Vector(0, 0);
        setPosition(characterPos);
        this.maxHealth = maxHealth;
        this.currHealth = maxHealth;
        this.score = 100;

        Random r = new Random();
        maxVelocity = 4 + r.nextFloat() * 4;
        rotationDirection = r.nextBoolean() ? 1 : -1;
    }

    @Override
    protected void setPosition(Point charPoint) {
        super.setPosition(charPoint);
        Vector direction = position.unitVecTo(charPoint);
        float dist = position.distance(charPoint) / 2;
        rotationCenter.setX(direction.getX() * dist);
        rotationCenter.setY(direction.getY() * dist);
        velocity = position.unitVecTo(rotationCenter)
                .rotate((float) (rotationDirection * Math.PI / 2f))
                .scale(maxVelocity);
    }

    @Override
    public void update(Vector charVelocity, Point characterPos) {
        // Calculate direction, scale up to velocity, subtract char velocity,
        // add small amount of randomness
        // velocity = position.unitVecTo(characterPos)
        // .add(charVelocity.normalize()).scale(maxVelocity)
        // .sub(charVelocity).add(Vector.random());

        Vector v = rotationCenter.unitVecTo(characterPos);
        rotationCenter = rotationCenter.move(v);

        velocity = velocity.add(position.unitVecTo(rotationCenter)).normalize().scale(maxVelocity);

        velocity = velocity.sub(charVelocity);

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
