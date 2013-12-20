package com.fsck.sector25;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Level {

    private int height;
    private ArrayList<Enemy> enemies;
    private Stars stars;
    private Smoke smoke;
    private Projectiles projectiles;
    private Grid grid;

    public Level(int level, Resources res) {
        enemies = new ArrayList<Enemy>();
        stars = new Stars(res);
        smoke = new Smoke(res);
        projectiles = new Projectiles(res);
        grid = new Grid();
    }

    public int[] update(Vector charVelocity, Point characterPos, boolean paused) {
        int[] killed = new int[2];
        stars.move(charVelocity);
        if (charVelocity.isZero())
            stars.move(Vector.random());
        smoke.move(charVelocity);
        if (!paused) {
            for (int i = enemies.size() - 1; i >= 0; i--) {
                Enemy enemy = enemies.get(i);
                enemy.update(charVelocity.scale(sector25view.VELOCITY_SCALE),
                        characterPos);

                // check for hits
                if (projectiles.testHit(enemy.getHitBox())) {
                    enemy.takeDamage(1);
                }

                if (enemy.isDead()) {
                    enemies.remove(i);
                    smoke.add(enemy.getPosition(), Vector.zero());
                    killed[0]++;
                    killed[1]+= enemy.getScore();
                } else {
                    enemies.set(i, enemy);
                }
            }

            projectiles.update(charVelocity.scale(sector25view.VELOCITY_SCALE)
                    .getX(), charVelocity.scale(sector25view.VELOCITY_SCALE)
                    .getY(), characterPos.getX(), characterPos.getY(),
                    height * 2);
        }
        smoke.update();
        grid.update(charVelocity.scale(sector25view.VELOCITY_SCALE));

        return killed;
    }

    public void draw(Canvas canvas, Paint paint) {
        // grid.draw(canvas, paint);
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
            if (enemies.size() % 10 == 0) {
                // every 10th enemy is a Wyrm
                enemies.add(new Wyrm(characterPos, 5));
            } else {
                enemies.add(new Cylon(characterPos, 1));
            }
    }

    public void setSize(int width, int height) {
        this.height = height;
        stars.set(width, height);
        grid.set(width, height);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemy(int i) {
        enemies.remove(i);
    }

    public boolean shoot(Vector gunDirection, float x, float y) {
        if (!gunDirection.isZero()) {
            projectiles.add(x, y, gunDirection.getX(), gunDirection.getY());
            return true;
        }
        return false;
    }

    public Vector menuShoot(Point position, float x, float y) {
        // Aim at nearest enemy that has not been aimed at.
        float aimx = 0;
        float aimy = 0;
        float distance = height / 3;
        Boolean newTarget = false;
        int target = 0;
        for (Enemy enemy : enemies) {
            if ((enemy.aimed() == 0 || !enemy.isDead())
                    && enemy.getPosition().distance(position) < distance) {
                distance = enemy.getPosition().distance(position);
                newTarget = true;
                target = enemies.indexOf(enemy);
                ;
                aimx = enemy.getX() - position.getX();
                aimy = enemy.getY() - position.getY();
                break;
            }
        }
        Vector aim = new Vector(aimx, aimy);

        for (Enemy enemy : enemies) {
            if (enemy.aimed() == 1) {
                Point pos = new Point(enemy.getX() + 3 * enemy.getVX() - x,
                        enemy.getY() + 3 * enemy.getVY() - y);
                projectiles.add(x, y, pos.getX(), pos.getY());
                if (newTarget)
                    enemy.shot();
            }
        }

        if (newTarget)
            enemies.get(target).aim();
        return aim;
    }

    public void clear() {
        enemies = new ArrayList<Enemy>();
        smoke.clear();
        projectiles.clear();
    }

    public void setLevel(int i) {
        enemies.clear();
        projectiles.clear();
        
    }
}
