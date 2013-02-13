package com.fsck.sector25;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class sector25view extends SurfaceView implements SurfaceHolder.Callback {

    private sector25thread thread;
    private String TAG = "sector25";

    public static final double VELOCITY_SCALE = .25;

    public enum GameState {
        STATE_PAUSE, STATE_RUNNING
    }

    /*
     * Animation thread
     */
    class sector25thread extends Thread {

        private Paint paint = new Paint();
        private Paint textStroke = new Paint();
        private SurfaceHolder mSurfaceHolder = null;
        private long mLastTime = 0;
        private long mLastSmoke = 0;
        private long mLastShot = 0;
        private boolean mRun = false;
        private int height, width;
        private boolean joystick = false;
        private boolean joystick2 = false;
        private Character character;
        private Enemy[] enemies;
        private Stars stars;
        private Smoke smoke;
        private Joystick js = new Joystick();
        private float x1, y1, x2, y2;
        private double vx;
        private double vy;
        private Projectiles projectiles;
        private Bitmap background;

        /** states */

        private GameState mState = GameState.STATE_RUNNING;

        public sector25thread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            Resources res = context.getResources();

            mSurfaceHolder = surfaceHolder;
            character = new Character(res);
            enemies = new Enemy[3];
            for (int i = 0; i < enemies.length; i++) {
                enemies[i] = new Enemy(res);
            }
            stars = new Stars(res);
            smoke = new Smoke(res);
            projectiles = new Projectiles(res, this);

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(32);
            paint.setTypeface(Typeface.DEFAULT_BOLD);

            textStroke.setColor(Color.BLACK);
            textStroke.setStyle(Paint.Style.STROKE);
            textStroke.setAntiAlias(true);
            textStroke.setStrokeWidth(1);
            textStroke.setTextSize(32);
            textStroke.setTypeface(Typeface.DEFAULT_BOLD);

            background = BitmapFactory.decodeResource(res,
                    R.drawable.background);
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
                    stars.draw(canvas, paint);
                    smoke.draw(canvas, paint);
                    projectiles.draw(canvas, paint);
                    character.draw(canvas, paint);
                    for (Enemy enemy : enemies) {
                        enemy.draw(canvas, paint);
                    }
                    js.drawLeft(canvas, paint);
                    js.drawRight(canvas, paint);

                    canvas.restore();
                }
            }
        }

        /***
         * Updates each frame
         */
        private void update() {
            // Measure time
            long now = System.currentTimeMillis();
            double elapsedFrame = (now - mLastTime);
            double elapsedSmoke = (now - mLastSmoke);
            double elapsedShot = (now - mLastShot);

            // Update the default game play
            if (elapsedFrame > 33) {
                x1 = js.getX1();
                y1 = js.getY1();
                x2 = js.getX2();
                y2 = js.getY2();

                vx = x1 * VELOCITY_SCALE;
                vy = y1 * VELOCITY_SCALE;
                
                stars.move(x1, y1);
                smoke.move(x1, y1);

                if (x1 == 0 && y1 == 0)
                    stars.move((float) Math.random(), (float) Math.random());
                character.setDirection(x2, x1);

                for (Enemy enemy : enemies) {
                    enemy.update(x1, y1);
                }

                projectiles.update();
                smoke.update();
                mLastTime = now;
                // add smoke
                if (elapsedSmoke > 500) {
                    smoke.add(character.getSmokeX(), character.getSmokeY(),
                            character.getSmokeVX(), character.getSmokeVY());
                    mLastSmoke = now;
                }

                // shoot (place holder, will have to create different
                // shots/upgrades)
                if (elapsedShot > 100) {
                    if (x2 != 0 || y2 != 0) {
                        projectiles.add(character.getShotX(),
                                character.getShotY(),x2, y2);
                        mLastShot = now;
                    }
                }
            }

        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        if (mState == GameState.STATE_RUNNING)
                            update();
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
            setState(GameState.STATE_RUNNING);
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
                setState(GameState.STATE_PAUSE);
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
                for (Enemy enemy : enemies) {
                    enemy.set(width, height);
                }
                stars.set(width, height);
                js.set(width, height);
                background = Bitmap.createScaledBitmap(background, width,
                        height, false);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            js.touch(event);
            return true;
        }

        public Paint getPaint() {
            return paint;
        }

        public Paint getTextStroke() {
            return textStroke;
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

        public Enemy[] getEnemies() {
            return enemies;
        }

        public Stars getStars() {
            return stars;
        }

        public Smoke getSmoke() {
            return smoke;
        }

        public Joystick getJs() {
            return js;
        }

        public Projectiles getProjectiles() {
            return projectiles;
        }

        public Bitmap getBackground() {
            return background;
        }

        public GameState getThreadState() {
            return mState;
        }

        public double getVX() {
            return vx;
        }

        public double getVY() {
            return vy;
        }
    }

    public sector25view(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new sector25thread(holder, context, new Handler() {
        });
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
        thread.setRunning(true);
        // TODO: stop thread.start() from being called twice
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

}
