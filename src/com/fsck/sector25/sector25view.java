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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

class sector25view extends SurfaceView implements SurfaceHolder.Callback {

    private sector25thread thread;
    private String TAG = "sector25";

    private float width;
    private float height;

    public static final float VELOCITY_SCALE = .25f;
    public static final boolean DRAW_HITBOXES = false;

    public enum GameState {
        STATE_PAUSE, STATE_RUNNING
    }

    /*
     * Animation thread
     */
    class sector25thread extends Thread {

        private Handler handler;
        private TextView scoreText;
        private int score;
        private Paint paint = new Paint();
        private Paint textStroke = new Paint();
        private SurfaceHolder mSurfaceHolder = null;
        private long mLastTime = 0;
        private long mLastSmoke = 0;
        private long mLastShot = 0;
        private boolean mRun = false;
        private int height, width;
        private Character character;
        private ArrayList<Enemy> enemies;
        private Stars stars;
        private Smoke smoke;
        private Healthbar healthbar;
        private Joystick js = new Joystick();
        private float x1, y1, x2, y2;
        private float vx;
        private float vy;
        private Projectiles projectiles;
        private Bitmap background;
        private Bitmap enemy;

        /** states */

        private GameState mState = GameState.STATE_RUNNING;

        public sector25thread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            Resources res = context.getResources();
            this.handler = handler;

            mSurfaceHolder = surfaceHolder;
            character = new Character(res);
            enemies = new ArrayList<Enemy>();
            stars = new Stars(res);
            smoke = new Smoke(res);
            healthbar = new Healthbar(res);
            projectiles = new Projectiles(res);

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
            enemy = BitmapFactory.decodeResource(res, R.drawable.enemy);
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
                    healthbar.draw(canvas, width / 2, height * 9 / 10);

                    canvas.restore();
                }

                if (DRAW_HITBOXES) {
                    character.drawHit(canvas, paint);
                    projectiles.drawHit(canvas, paint);
                    for (Enemy enemy : enemies) {
                        enemy.drawHit(canvas, paint);
                    }
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
                    x1 = js.getX1();
                    y1 = js.getY1();
                    x2 = js.getX2();
                    y2 = js.getY2();

                    //Place holder for score; arcade mode where score
                    //is determined by distance traveled to the right.
                    score += x1;
                    handler.postDelayed(scoreUpdate, 1);

                    Vector charVelocity = new Vector(x1,y1).scale(VELOCITY_SCALE);
                    Vector gunDirection = (new Vector(x2,y2)).normalize();
                    
                    vx = x1 * VELOCITY_SCALE;
                    vy = y1 * VELOCITY_SCALE;

                    stars.move(charVelocity);
                    smoke.move(charVelocity);

                    if (x1 == 0 && y1 == 0)
                        stars.move(Vector.random());
                    character.update(charVelocity, gunDirection);

                    for (int i = enemies.size() - 1; i >= 0; i--) {
                        Enemy enemy = enemies.get(i);
                        enemy.update(charVelocity, character.getPosition());

                        // check for hits
                        if (projectiles.testHit(enemy.getHitBox())) {
                            enemies.remove(i);
                            smoke.add(enemy.getPosition(), Vector.zero());
                            Log.d(TAG, "remove " + i);
                        } else if (character.testHit(enemy.getHitBox())) {
                            enemies.remove(i);
                            healthbar.incrementHealth(-5);
                            smoke.add(enemy.getPosition(), Vector.zero());
                            Log.d(TAG, "remove " + i);
                        } else {
                            enemies.set(i, enemy);
                        }
                    }

                    projectiles.update(vx, vy, character.getX(),
                            character.getY(), height * 2);
                    smoke.update();
                    mLastTime = now;

                    // add smoke
                    if (elapsedSmoke > 300) {
                        smoke.add(character.getSmokePosition(),
                                character.getSmokeVelocity());
                        mLastSmoke = now;

                        // placeholder adding enemies
                        if (enemies.size() < 100)
                            enemies.add(new Enemy(enemy, width, height,
                                    character.getPosition()));
                    }

                    // shoot (place holder, will have to create different
                    // shots/upgrades)
                    if (elapsedShot > 100) {
                        if (x2 != 0 || y2 != 0) {
                            projectiles.add(character.getShotX(),
                                    character.getShotY(), x2, y2);
                            mLastShot = now;
                        }
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

        private Runnable scoreUpdate = new Runnable() {
            public void run() {
                scoreText.setText("Score: " + score/100 + "  ");
            }
        };

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

        public ArrayList<Enemy> getEnemies() {
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

        public void setTextView(TextView view) {
            scoreText = view;
        }
    }

    public sector25view(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        width = 10000;
        height = 10000;
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

        if (!(thread.getState() == State.NEW)){
            thread = new sector25thread(holder, this.getContext(), new Handler() {
            });
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

}
