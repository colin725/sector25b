package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Character {

    private Point position;
    private Point smokePosition;
    private int direction;
    private float directionGun;
    private Sprite spaceman;
    float[][] hitbox = new float[3][3];
    private int width;
    private int height;
    Bitmap man;
    Bitmap arm1;
    Bitmap arm2;
    Bitmap gun;

    public Character(Resources res) {
        man = BitmapFactory.decodeResource(res, R.drawable.spaceman);
        arm1 = BitmapFactory.decodeResource(res, R.drawable.arm1);
        arm2 = BitmapFactory.decodeResource(res, R.drawable.arm2);
        gun = BitmapFactory.decodeResource(res, R.drawable.gun);
    }

    public void set(int width, int height) {
        position = new Point(width / 2, height * 2 / 5);
        this.width = width;
        this.height = height;

        spaceman = new Sprite(Bitmap.createScaledBitmap(man, width / 5,
                width / 5, false), 2);
        arm1 = Bitmap.createScaledBitmap(arm1, width * 1 / 30, width * 1 / 40,
                false);
        arm2 = Bitmap.createScaledBitmap(arm2, width * 1 / 30, width * 1 / 45,
                false);
        gun = Bitmap.createScaledBitmap(gun, width * 1 / 13, width * 1 / 26,
                false);

        smokePosition = new Point(getSmokeX(), getSmokeY());

        hitbox[0] = new float[] { 0, spaceman.getHeight() / 4, 20 };
        hitbox[1] = new float[] { 0, -spaceman.getHeight() / 7, 30 };
        hitbox[2] = new float[] { 0, 0, 20 };
    }

    public void setPositionMenu() {
        position = new Point(width / 3 , height / 2);
        smokePosition = new Point(getSmokeX(), getSmokeY());
    }

    public void setPosition() {
        position = new Point(width / 2, height * 2 / 5);
        smokePosition = new Point(getSmokeX(), getSmokeY());
    }

    public float getShotX() {
        float shotx = (float) (spaceman.getWidth() * 2 / 3 * Math.cos(Math
                .toRadians(directionGun)));
        return position.getX() + shotx - 2 * shotx * direction;
    }

    public float getShotY() {
        float shoty = (float) (spaceman.getWidth() * 2 / 3 * Math.sin(Math
                .toRadians(directionGun)));
        return position.getY() + shoty;
    }

    private float getSmokeX() {
        return (position.getX() - spaceman.getWidth() / 4 + direction
                * spaceman.getWidth() / 2 + spaceman.getWidth() / 4);
    }

    private float getSmokeY() {
        return position.getY() + spaceman.getHeight() / 4;
    }

    public Point getSmokePosition() {
        return smokePosition;
    }

    public Vector getSmokeVelocity() {
        return new Vector(getSmokeVX(), getSmokeVY());
    }

    private float getSmokeVX() {
        return -(position.getX() - getSmokeX()) / 50;
    }

    private float getSmokeVY() {
        return (position.getY() - getSmokeY()) / 50;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public Point getPosition() {
        return position;
    }

    public void update(Vector velocity, Vector aim) {
        // Set character direction and frame
        spaceman.setFrame(1);
        if (velocity.getX() > 0) {
            direction = 0;
            spaceman.setFrame(0);
        } else if (velocity.getX() < 0) {
            direction = 1;
            spaceman.setFrame(0);
        }
        if (aim.getX() > 0) {
            direction = 0;
        } else if (aim.getX() < 0) {
            direction = 1;
        }

        // Set gun direction
        if (!aim.isZero()) {
            directionGun = aim.angle();
        }

        smokePosition.setX(getSmokeX());
        smokePosition.setY(getSmokeY());

    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.translate(position.getX(), position.getY());
        if (direction == 1)
            canvas.scale(-1, 1);
        canvas.save();

        canvas.rotate(directionGun - 5, -spaceman.getWidth() / 6, 0);
        canvas.drawBitmap(arm2, 0, 0, paint);
        canvas.restore();

        spaceman.draw(canvas, 0, 0);

        canvas.rotate(directionGun - 5, -spaceman.getWidth() / 12,
                spaceman.getWidth() / 15);
        canvas.drawBitmap(gun, -spaceman.getWidth() / 15, 0, paint);
        canvas.drawBitmap(arm1, -spaceman.getWidth() / 9,
                spaceman.getWidth() / 15, paint);

        canvas.restore();
    }

    public float[][] getHitBox() {
        return hitbox;
    }

    public boolean testHit(float[] enemy) {
        for (int i = 0; i < hitbox.length; i++) {
            float xdist = (position.getX() + hitbox[i][0]) - enemy[0];
            float ydist = (position.getY() + hitbox[i][1]) - enemy[1];
            float distance = (float) Math.sqrt(xdist * xdist + ydist * ydist);
            if (distance < enemy[2] + hitbox[i][2]) {
                return true;
            }
        }
        return false;
    }

    public void drawHit(Canvas canvas, Paint paint) {
        // TODO: better hitboxes? Might be fine like this.
        canvas.drawCircle(position.getX() + hitbox[0][0], position.getY()
                + hitbox[0][1], hitbox[0][2], paint);
        canvas.drawCircle(position.getX() + hitbox[1][0], position.getY()
                + hitbox[1][1], hitbox[1][2], paint);
        canvas.drawCircle(position.getX() + hitbox[2][0], position.getY()
                + hitbox[2][1], hitbox[2][2], paint);
    }

}
