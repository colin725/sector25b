package com.fsck.sector25;

import java.util.ArrayList;

import com.fsck.sector25.GameHUD.GameStyle;
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
    private long mLast[] = new long[2];

    public enum EnemyType {
        CYLON, WYRM
    }

    public Level(int level, Resources res) {
        mEnemies = new ArrayList<Enemy>();
        mStars = new Stars(res);
        mSmoke = new Smoke(res);
        mProjectiles = new Projectiles(res);
        mGrid = new Grid();
    }

    public int[] update(Vector charVelocity, boolean paused) {
        int[] killed = new int[2];
        mStars.move(charVelocity);
        if (charVelocity.isZero())
            mStars.move(Vector.random());
        mSmoke.move(charVelocity);
        if (!paused) {
            for (int i = mEnemies.size() - 1; i >= 0; i--) {
                Enemy enemy = mEnemies.get(i);
                enemy.update(charVelocity.scale(sector25view.VELOCITY_SCALE));

                // check for hits
                if (mProjectiles.testHit(enemy.getHitBox())) {
                    enemy.takeDamage(1);
                }
                mProjectiles.testCharacterHit();

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
                    .getY());
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

    public void drawHit(Canvas canvas, Paint paint) {
        mProjectiles.drawHit(canvas, paint);
        for (Enemy enemy : mEnemies) {
            enemy.drawHit(canvas, paint);
        }
    }

    public void addSmoke() {
        mSmoke.add(Character.getSmokePosition(), Character.getSmokeVelocity());
    }

    public void addEnemy(EnemyType type) {
        if (mEnemies.size() < 125) {
            switch (type) {
                case CYLON:
                    mEnemies.add(new Cylon());
                    break;

                case WYRM:
                    mEnemies.add(new Wyrm());
                    break;
            }
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
            Projectiles.add(x, y, gunDirection.getX(), gunDirection.getY(), 0);
            return true;
        }
        return false;
    }

    public Vector menuShoot() {
        // Aim at nearest enemy that has not been aimed at.
        float aimx = 0;
        float aimy = 0;
        float distance = (float)mHeight / 2.5f;
        Point position = Character.getPosition();
        float x = Character.getShotX();
        float y = Character.getShotY();
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
                Projectiles.add(x, y, pos.getX(), pos.getY(), 0);
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
        mLast = new long[2];
    }

    public void addEnemies(long now) {
        /*
         *  placeholder for adding enemies
         *  TODO: Implement unique enemy additions per level
         */
        if (GameHUD.getGameStyle() == GameStyle.BOSS && mEnemies.size() == 0) {
            mEnemies.add(new Boss1());
        }

        float cylonTimer = GameHUD.getGameStyle() == GameStyle.BOSS ? 800 : 300;
        float wyrmTimer = GameHUD.getGameStyle() == GameStyle.BOSS ? 6000 : 5000;

        // cylon
        if (now - mLast[0] > cylonTimer) {
            addEnemy(EnemyType.CYLON);
            mLast[0] = now;
        }
        if (now - mLast[1] > wyrmTimer) {
            addEnemy(EnemyType.WYRM);
            mLast[1] = now;
        }
    }
}
