package com.fsck.sector25;

import java.lang.Thread.State;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class sector25view extends SurfaceView implements SurfaceHolder.Callback {

    private static sector25thread thread;
    @SuppressWarnings("unused")
    private String TAG = "sector25";

    public static final float VELOCITY_SCALE = .15f;
    public static final boolean DRAW_HITBOXES = false;

    public enum GameState {
        STATE_PAUSE, STATE_RUNNING, STATE_DEAD, STATE_MENU,
        STATE_WIN
    }

    /*
     * Animation thread
     */
    class sector25thread extends Thread {

        // TODO: Make handler static to avoid memory leaks!
        private Handler handler;
        // private TextView scoreText;
        private int score;
        private Paint paint = new Paint();
        private SurfaceHolder mSurfaceHolder = null;
        private long mLastTime = 0;
        private long mLastSmoke = 0;
        private long mLastShot = 0;
        private boolean mRun = false;
        private boolean surfaceSizeSet = false;
        private int height, width;
        private Level level;
        private Menu menu;
        private Character character;
        private GameHUD hud;
        private Bitmap background;
        private Context context;
        private sector25view parentView;
        private DataStore store;

        /** states */

        private GameState mState = GameState.STATE_MENU;

        public sector25thread(SurfaceHolder surfaceHolder, Context context,
                Handler handler, sector25view parentView) {
            this.parentView = parentView;
            this.context = context;
            Resources res = context.getResources();
            this.handler = handler;
            mSurfaceHolder = surfaceHolder;
            hud = new GameHUD(context, handler);
            character = new Character(res);
            level = new Level(0, res, this.parentView);
            paint.setColor(Color.WHITE);
            paint.setTextSize(32);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            background = BitmapFactory.decodeResource(res,
                    R.drawable.background);
            menu = new Menu();
            menu.setHealth(hud.getHealthbar());
            store = new DataStore(1, context);

        }

        /**
         * Draws
         */
        private void doDraw(Canvas canvas) {
            synchronized (mSurfaceHolder) {
                if (canvas != null) {
                    canvas.save();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(background, 0, 0, paint);

                    paint.setAlpha(255);
                    level.draw(canvas, paint);

                    if (mState == GameState.STATE_RUNNING) {
                        hud.draw(canvas, paint);
                        character.draw(canvas, paint);
                    } else if (mState == GameState.STATE_MENU) {
                        // TODO: Menu
                        // 1) Animate in and out
                        // 2) code actual touch targets for buttons
                        // 3) Add hover differentiation on buttons
                        // 4) Make character shoot enemies that come near
                        menu.draw(canvas, paint);
                        if (menu.page() == 0)
                            character.draw(canvas, paint);
                    } else if (mState == GameState.STATE_PAUSE) {
                        hud.draw(canvas, paint);
                        character.draw(canvas, paint);
                        canvas.drawARGB(155, 0, 0, 0);
                    } else if (mState == GameState.STATE_DEAD) {
                        hud.draw(canvas, paint);
                        character.draw(canvas, paint);
                        canvas.drawARGB(155, 0, 0, 0);
                        paint.setTextSize(40);
                        canvas.drawText("GAME OVER", width / 2 - 100,
                                height / 2, paint);
                        canvas.drawText("Score: " + score, width / 2 - 100,
                                height / 2 + 100, paint);
                    } else if (mState == GameState.STATE_WIN) {
                        hud.draw(canvas, paint);
                        character.draw(canvas, paint);
                        canvas.drawARGB(155, 0, 0, 0);
                        paint.setTextSize(40);
                        canvas.drawText("Level complete!", width / 2 - 100,
                                height / 2, paint);
                        canvas.drawText("Score: " + score, width / 2 - 100,
                                height / 2 + 100, paint);
                    }

                    canvas.restore();
                }

                if (DRAW_HITBOXES) {
                    character.drawHit(canvas, paint);
                    level.drawHit(canvas);
                }
            }
        }

        /***
         * Updates each frame
         */
        private void update() {
            synchronized (mSurfaceHolder) {
                // Measure time
                long now = System.currentTimeMillis();
                double elapsedFrame = (now - mLastTime);
                double elapsedSmoke = (now - mLastSmoke);
                double elapsedShot = (now - mLastShot);

                // Update the default game play
                if (elapsedFrame > 33) {

                    if (mState == GameState.STATE_RUNNING) {
                        updateRunning(now, elapsedShot);
                        updateSmoke(now, elapsedSmoke);
                    } else if (mState == GameState.STATE_MENU) {
                        updateMenu();
                        updateSmoke(now, elapsedSmoke);
                    } else if (mState == GameState.STATE_PAUSE) {
                        updatePaused();
                    } else if (mState == GameState.STATE_DEAD) {
                        updateDead();
                    }

                    mLastTime = now;
                }
            }
        }

        private void updateDead() {
            level.update(new Vector(15, 0), character.getPosition(), true);
        }

        private void updatePaused() {
            level.update(new Vector(15, 0), character.getPosition(), true);
        }

        private void updateSmoke(long now, double elapsedSmoke) {
            // add smoke
            if (elapsedSmoke > 300) {
                if (!(mState == GameState.STATE_MENU && menu.page() == 1)) {
                    level.addSmoke(character.getSmokePosition(),
                            character.getSmokeVelocity());
                    // placeholder adding enemies
                    level.addEnemy(character.getPosition());
                }
                mLastSmoke = now;
            }
        }

        private void updateMenu() {
            level.update(new Vector(15, 0), character.getPosition(), false);
            character.update(
                    new Vector(0, 0),
                    level.menuShoot(character.getPosition(),
                            character.getShotX(), character.getShotY()));
            if (menu.page() == 1) {
                level.clear();
            }
        }

        private void updateRunning(long now, double elapsedShot) {
            Vector charVelocity = hud.getLeftVector();
            Vector gunDirection = hud.getRightVector().normalize();

            // Place holder for score; arcade mode where score
            // is determined by distance traveled to the right.
            // score += charVelocity.getX();
            hud.setScore(score);

            hud.update();

            character.update(charVelocity, gunDirection);
            level.update(charVelocity, character.getPosition(), false);

            int count = 0;
            ArrayList<Integer> remove = new ArrayList<Integer>();
            for (Enemy enemy : level.getEnemies()) {
                if (character.testHit(enemy.getHitBox())) {
                    character.takeDamage(enemy.getDamage());
                    hud.getHealthbar().setHealth(character.getHealth());
                    remove.add(count);
                }
                count++;
            }
            for (int i = remove.size() - 1; i >= 0; i--) {
                level.removeEnemy(remove.get(i));
            }

            // shoot (place holder, will have to create different
            // shots/upgrades)
            if (elapsedShot > 100) {
                if (level.shoot(gunDirection, character.getShotX(),
                        character.getShotY())) {
                    mLastShot = now;
                }
            }

            if (hud.win()) {
                mState = GameState.STATE_WIN;
            } else if (character.isDead()) {
                mState = GameState.STATE_DEAD;
            }
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        update();
                        if (surfaceSizeSet)
                            doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void setRunning(boolean b) {
            mRun = b;
        }

        public void setState(GameState state) {
            synchronized (mSurfaceHolder) {
                mState = state;
            }
        }

        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mState == GameState.STATE_RUNNING)
                    setState(GameState.STATE_PAUSE);
            }
        }

        public void unpause() {
            synchronized (mSurfaceHolder) {
                mLastTime = System.currentTimeMillis() + 100;
            }
            // TODO: return to last state
            // setState(GameState.STATE_RUNNING);
        }

        public Bundle saveState(Bundle map) {
            synchronized (mSurfaceHolder) {
                if (map != null) {
                    // TODO: add save state stuff; example:
                    // map.putInt(KEY_DIFFICULTY, Integer.valueOf(mDifficulty));
                }
            }
            return map;
        }

        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
                // setState(GameState.STATE_PAUSE);
                // TODO: add restore stuff; example:
                // mX = savedState.getDouble(KEY_X);
            }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                this.width = width;
                this.height = height;
                character.set(width, height);
                character.setPositionMenu();
                level.set(width, height);
                hud.set(width, height);
                background = Bitmap.createScaledBitmap(background, width,
                        height, false);
                // need to do this for every enemy type, not sure if there is a
                // better way.
                Cylon.set(getResources(), width, height);
                Wyrm.set(getResources(), width, height);
                Menu.set(getResources(), width, height);
                surfaceSizeSet = true;
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (mState == GameState.STATE_RUNNING) {
                hud.touch(event);
            } else if (mState == GameState.STATE_PAUSE) {
                mState = GameState.STATE_RUNNING;
            } else if (mState == GameState.STATE_MENU) {
                menu.touch(event);
            } else if (mState == GameState.STATE_DEAD) {
                mState = GameState.STATE_MENU;
                menu.resetPage();
            } else if (mState == GameState.STATE_WIN) {
                mState = GameState.STATE_MENU;
            }
            return true;
        }

        public Paint getPaint() {
            return paint;
        }

        public SurfaceHolder getmSurfaceHolder() {
            return mSurfaceHolder;
        }

        public long getmLastTime() {
            return mLastTime;
        }

        public long getmLastSmoke() {
            return mLastSmoke;
        }

        public long getmLastShot() {
            return mLastShot;
        }

        public boolean isRunning() {
            return mRun;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public Character getCharacter() {
            return character;
        }

        public Bitmap getBackground() {
            return background;
        }

        public GameState getThreadState() {
            return mState;
        }

        void setThreadState(GameState state) {
            this.mState = state;
        }

        public GameHUD getHUD() {
            return hud;
        }

        public void startGame() {
            score = 0;
            level.setLevel(0);
            character.reset();
            mState = GameState.STATE_RUNNING;
        }

        public void notifyEnemyDead(Enemy enemy) {
            score += enemy.getScore();
            hud.addKill();
        }

    }

    public sector25view(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new sector25thread(holder, context, new Handler() {
        }, this);
        setFocusable(true);
    }

    public sector25thread getThread() {
        return thread;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus)
            thread.pause();
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!(thread.getState() == State.NEW)) {
            thread = new sector25thread(holder, this.getContext(),
                    new Handler() {
                    }, this);
        }
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        // TODO: thread.interrupt instead of join?
        // http://stackoverflow.com/questions/12714887/surfaceview-and-thread-already-started-exception
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return thread.onTouchEvent(event);
    }

    public static void setGameState(GameState state) {
        thread.setThreadState(state);
    }

    public static void startGame() {
        thread.startGame();
    }

    public static GameState getGameState() {
        return thread.getThreadState();
    }

}
