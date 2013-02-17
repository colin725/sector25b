package com.fsck.sector25;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Level {

    private int width;
    private int height;
    private float displacement;
    private ArrayList<Enemy> enemies;
    private Stars stars;
    private Smoke smoke;
    private Projectiles projectiles;

    public Level(int level, Resources res) {
        enemies = new ArrayList<Enemy>();
        stars = new Stars(res);
        smoke = new Smoke(res);
        projectiles = new Projectiles(res);
    }

    public void update(Vector charVelocity, Point characterPos) {
        stars.move(charVelocity);
        if (charVelocity.isZero())
            stars.move(Vector.random());
        smoke.move(charVelocity);

        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(charVelocity.scale(sector25view.VELOCITY_SCALE),
                    characterPos);

            // check for hits
            if (projectiles.testHit(enemy.getHitBox())) {
                enemies.remove(i);
              smoke.add(enemy.getPosition(), Vector.zero());
            } else {
                enemies.set(i, enemy);
            }
        }

        projectiles.update(charVelocity.scale(sector25view.VELOCITY_SCALE)
                .getX(),
                charVelocity.scale(sector25view.VELOCITY_SCALE).getY(),
                characterPos.getX(), characterPos.getY(), height * 2);
        smoke.update();
    }

    public void draw(Canvas canvas, Paint paint) {
        stars.draw(canvas, paint);
        smoke.draw(canvas, paint);
        projectiles.draw(canvas, paint);
        for (Enemy enemy : enemies) {
            enemy.draw(canvas, paint);
        }
    }

    public void drawHit(Canvas canvas) {
        projectiles.drawHit(canvas, null);
        for (Enemy enemy : enemies) {
            enemy.drawHit(canvas, null);
        }
    }

    public void addSmoke(Point smokePosition, Vector smokeVelocity) {
        smoke.add(smokePosition, smokeVelocity);
    }

    public void addEnemy(Point characterPos) {
        if (enemies.size() < 100)
            enemies.add(new Enemy(characterPos));
    }

    public void set(int width, int height) {
        this.width = width;
        this.height = height;
        stars.set(width, height);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy(int i){
        enemies.remove(i);
    }

    public boolean shoot(Vector gunDirection, float x, float y) {
        if (!gunDirection.isZero()) {
            projectiles.add(x, y, gunDirection.getX(), gunDirection.getY());
            return true;
        }
        return false;
    }
}