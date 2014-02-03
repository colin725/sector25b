package com.fsck.sector25;

import java.lang.Thread.State;
import java.util.ArrayList;

import com.fsck.sector25.Menu.MenuPage;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class sector25view extends SurfaceView implements SurfaceHolder.Callback {

    private static sector25thread thread;

    public static final float VELOCITY_SCALE = .125f;
    public static final boolean DRAW_HITBOXES = false;

    public enum GameState {
        STATE_PAUSE, STATE_RUNNING, STATE_DEAD, STATE_MENU, STATE_WIN
    }

    /*
     * Animation thread
     */
    class sector25thread extends Thread {

        private Paint mPaint = new Paint();
        private SurfaceHolder mSurfaceHolder = null;
        private long mLastTime = 0;
        private long mLastSmoke = 0;
        private long mLastShot = 0;
        private boolean mRun = false;
        private boolean mSurfaceSizeSet = false;
        private int mHeight, mWidth;
        private Level mLevel;
        private Menu mMenu;
        private Character mCharacter;
        private GameHUD mHud;
        private Bitmap mBackground;
        //private DataStore mDataStore;

        /** states */

        private GameState mState = GameState.STATE_MENU;

        public sector25thread(SurfaceHolder surfaceHolder, Context context) {
            Resources res = context.getResources();

            mSurfaceHolder = surfaceHolder;
            mHud = new GameHUD(context);
            mCharacter = new Character(res);
            mLevel = new Level(0, res);
            mPaint.setColor(Color.WHITE);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mBackground = BitmapFactory.decodeResource(res,
                    R.drawable.background);
            mMenu = new Menu();
            mMenu.setHealth(mHud.getHealthbar());
            mMenu.setEnergy(mHud.getEnergybar());
            //mDataStore = new DataStore(1, context);

        }

        /**
         * Draws
         */
        private void doDraw(Canvas canvas) {
            synchronized (mSurfaceHolder) {
                if (canvas != null) {
                    canvas.save();
                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(mBackground, 0, 0, mPaint);

                    mLevel.draw(canvas, mPaint);
                    mHud.draw(canvas, mPaint, mState);
                    mCharacter.draw(canvas, mPaint, mState, Menu.getPage());
                    mMenu.draw(canvas, mPaint, mState);

                    canvas.restore();
                }

                if (DRAW_HITBOXES) {
                    mCharacter.drawHit(canvas, mPaint);
                    mLevel.drawHit(canvas, mPaint);
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
                        updateEnemies(now);
                    } else if (mState == GameState.STATE_MENU) {
                        updateMenu();
                        updateSmoke(now, elapsedSmoke);
                        updateEnemies(now);
                    } else if (mState == GameState.STATE_PAUSE) {
                        updatePaused();
                    } else if (mState == GameState.STATE_DEAD) {
                        updateDead();
                    }

                    mLastTime = now;
                }
            }
        }

        private void updateEnemies(long now) {
            if (!(mState == GameState.STATE_MENU && Menu.getPage() == MenuPage.LEVELSELECT)) {
                mLevel.addEnemies(now);
            }
        }

        private void updateDead() {
            mLevel.update(new Vector(15, 0), true);
        }

        private void updatePaused() {
            mLevel.update(new Vector(15, 0), true);
        }

        private void updateSmoke(long now, double elapsedSmoke) {
            // add smoke
            if (elapsedSmoke > 300) {
                if (!(mState == GameState.STATE_MENU && Menu.getPage() == MenuPage.LEVELSELECT)) {
                    mLevel.addSmoke();
                }
                mLastSmoke = now;
            }
        }

        private void updateMenu() {
            mLevel.update(new Vector(15, 0), false);
            mCharacter.update(
                    new Vector(0, 0),
                    mLevel.menuShoot());
            if (Menu.getPage() == MenuPage.LEVELSELECT) {
                mLevel.clear();
            }
        }

        private void updateRunning(long now, double elapsedShot) {
            Vector charVelocity = GameHUD.getLeftVector();
            Vector gunDirection = GameHUD.getRightVector().normalize();

            mHud.update();

            mCharacter.update(charVelocity, gunDirection);
            int kills[] = 
                    mLevel.update(charVelocity, false);
            GameHUD.incrementScore(kills[1]);
            mHud.addKills(kills[0]);

            int count = 0;
            ArrayList<Integer> remove = new ArrayList<Integer>();
            for (Enemy enemy : mLevel.getEnemies()) {
                if (!(enemy instanceof Boss1) && Character.testCollision(enemy.getHitBox())) {
                    mHud.takeDamage(enemy.getDamage());
                    remove.add(count);
                }
                count++;
            }
            for (int i = remove.size() - 1; i >= 0; i--) {
                mLevel.removeEnemy(remove.get(i));
            }

            // shoot (place holder, will have to create different
            // shots/upgrades)
            int energy =  mHud.getEnergybar().getEnergy();
            if (elapsedShot > 100 && energy > 5) {
                if (mLevel.shoot(gunDirection, Character.getShotX(),
                        Character.getShotY())) {
                    mLastShot = now;
                    GameHUD.incrementEnergy(-5);
                }
            }
            
            if (mHud.win()) {
                mState = GameState.STATE_WIN;
            } else if (mHud.isDead()) {
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
                        if (mSurfaceSizeSet)
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
            synchronized (mSurfaceHolder) {
                mRun = b;
            }
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
                this.mWidth = width;
                this.mHeight = height;
                Character.setSize(width, height);
                Character.setPositionMenu();
                mLevel.setSize(width, height);
                mHud.setSize(width, height);
                mBackground = Bitmap.createScaledBitmap(mBackground, width,
                        height, false);
                // need to do this for every enemy type, not sure if there is a
                // better way.
                Cylon.setSize(width, height);
                Wyrm.setSize(width, height);
                Menu.setSize(getResources(), width, height);
                Projectiles.setmMaxDistance(2 * width);
                mSurfaceSizeSet = true;
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            synchronized (mSurfaceHolder) {
                int eventAction = event.getAction();
                int actionCode = eventAction & MotionEvent.ACTION_MASK;

                if (mState == GameState.STATE_RUNNING) {
                    if (mHud.touch(event)) {
                        mState = GameState.STATE_PAUSE;
                    }
                } else if (mState == GameState.STATE_MENU) {
                    mMenu.touch(event);
                } else if (actionCode == MotionEvent.ACTION_DOWN) {
                    if (mState == GameState.STATE_PAUSE) {
                        mState = GameState.STATE_RUNNING;
                    } else if (mState == GameState.STATE_DEAD) {
                        mState = GameState.STATE_MENU;
                        Character.setPositionMenu();
                        mMenu.resetPage();
                    } else if (mState == GameState.STATE_WIN) {
                        mState = GameState.STATE_MENU;
                    }
                }
                return true;
            }
        }

        public Paint getPaint() {
            return mPaint;
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
            return mHeight;
        }

        public int getWidth() {
            return mWidth;
        }

        public Character getCharacter() {
            return mCharacter;
        }

        public Bitmap getBackground() {
            return mBackground;
        }

        public GameState getThreadState() {
            return mState;
        }

        void setThreadState(GameState state) {
            this.mState = state;
        }

        public GameHUD getHUD() {
            return mHud;
        }

        public void startGame() {
            mLevel.clear();
            Character.setPositionDefault();
            mState = GameState.STATE_RUNNING;
        }

    }

    public sector25view(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new sector25thread(holder, context);
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
            thread = new sector25thread(holder, this.getContext());
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

    public static void startGame() {
        thread.startGame();
    }

}
