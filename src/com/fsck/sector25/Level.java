package com.fsck.sector25;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Level {

    private int mHeight;
    private ArrayList<Enemy> mEnemies;
    private Stars mStars;
    private Smoke mSmoke;
    private Projectiles mProjectiles;
    private Grid mGrid;

    public Level(int level, Resources res) {
        mEnemies = new ArrayList<Enemy>();
        mStars = new Stars(res);
        mSmoke = new Smoke(res);
        mProjectiles = new Projectiles(res);
        mGrid = new Grid();
    }

    public int[] update(Vector charVelocity, Point characterPos, boolean paused) {
        int[] killed = new int[2];
        mStars.move(charVelocity);
        if (charVelocity.isZero())
            mStars.move(Vector.random());
        mSmoke.move(charVelocity);
        if (!paused) {
            for (int i = mEnemies.size() - 1; i >= 0; i--) {
                Enemy enemy = mEnemies.get(i);
                enemy.update(charVelocity.scale(sector25view.VELOCITY_SCALE),
                        characterPos);

                // check for hits
                if (mProjectiles.testHit(enemy.getHitBox())) {
                    enemy.takeDamage(1);
                }

                if (enemy.isDead()) {
                    mEnemies.remove(i);
                    mSmoke.add(enemy.getPosition(), Vector.zero());
                    killed[0]++;
                    killed[1] += enemy.getScore();
                } else {
                    mEnemies.set(i, enemy);
                }
            }

            mProjectiles.update(charVelocity.scale(sector25view.VELOCITY_SCALE)
                    .getX(), charVelocity.scale(sector25view.VELOCITY_SCALE)
                    .getY(), characterPos.getX(), characterPos.getY(),
                    mHeight * 2);
        }
        mSmoke.update();
        mGrid.update(charVelocity.scale(sector25view.VELOCITY_SCALE));

        return killed;
    }

    public void draw(Canvas canvas, Paint paint) {
        // mGrid.draw(canvas, paint);
        mStars.draw(canvas, paint);
        mSmoke.draw(canvas, paint);
        mProjectiles.draw(canvas, paint);
        for (Enemy enemy : mEnemies) {
            enemy.draw(canvas, paint);
        }
    }

    public void drawHit(Canvas canvas) {
        mProjectiles.drawHit(canvas, null);
        for (Enemy enemy : mEnemies) {
            enemy.drawHit(canvas, null);
        }
    }

    public void addSmoke(Point smokePosition, Vector smokeVelocity) {
        mSmoke.add(smokePosition, smokeVelocity);
    }

    public void addEnemy(Point characterPos) {
        if (mEnemies.size() < 100)
            if (mEnemies.size() % 10 == 0) {
                // every 10th enemy is a Wyrm
                mEnemies.add(new Wyrm(characterPos));
            } else {
                mEnemies.add(new Cylon(characterPos));
            }
    }

    public void setSize(int width, int height) {
        mHeight = height;
        mStars.set(width, height);
        mGrid.set(width, height);
    }

    public ArrayList<Enemy> getEnemies() {
        return mEnemies;
    }

    public void removeEnemy(int i) {
        mEnemies.remove(i);
    }

    public boolean shoot(Vector gunDirection, float x, float y) {
        if (!gunDirection.isZero()) {
            mProjectiles.add(x, y, gunDirection.getX(), gunDirection.getY());
            return true;
        }
        return false;
    }

    public Vector menuShoot(Point position, float x, float y) {
        // Aim at nearest enemy that has not been aimed at.
        float aimx = 0;
        float aimy = 0;
        float distance = mHeight / 3;
        Boolean newTarget = false;
        int target = 0;
        for (Enemy enemy : mEnemies) {
            if ((enemy.aimed() == 0 || !enemy.isDead())
                    && enemy.getPosition().distance(position) < distance) {
                distance = enemy.getPosition().distance(position);
                newTarget = true;
                target = mEnemies.indexOf(enemy);
                aimx = enemy.getX() - position.getX();
                aimy = enemy.getY() - position.getY();
                break;
            }
        }
        Vector aim = new Vector(aimx, aimy);

        for (Enemy enemy : mEnemies) {
            if (enemy.aimed() == 1) {
                Point pos = new Point(enemy.getX() + 3 * enemy.getVX() - x,
                        enemy.getY() + 3 * enemy.getVY() - y);
                mProjectiles.add(x, y, pos.getX(), pos.getY());
                if (newTarget)
                    enemy.shot();
            }
        }

        if (newTarget)
            mEnemies.get(target).aim();
        return aim;
    }

    public void clear() {
        mEnemies = new ArrayList<Enemy>();
        mSmoke.clear();
        mProjectiles.clear();
    }
}
