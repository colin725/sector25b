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

    /*
     * Animation thread
     */
    class sector25thread extends Thread {

        private Paint paint = new Paint();
        private Paint textStroke = new Paint();
        private SurfaceHolder mSurfaceHolder = null;
        private long mLastTime = 0;
        private long mLastShot = 0;
        private boolean mRun = false;
        private int height, width;
        private boolean joystick = false;
        private boolean joystick2 = false;
        private Character character;
        private Stars stars;
        private Joystick js = new Joystick();
        private Projectiles projectiles;
        private Bitmap background;

        /** states */
        private int mState = 2;
        public static final int STATE_PAUSE = 1;
        public static final int STATE_RUNNING = 2;

        public sector25thread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            Resources res = context.getResources();

            mSurfaceHolder = surfaceHolder;
            character = new Character(res);
            stars = new Stars(res);
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
        }

        /**
         * Draws
         */
        private void doDraw(Canvas canvas) {
            synchronized (mSurfaceHolder) {
                if(canvas != null){
                    canvas.save();
                    canvas.drawBitmap(background, 0, 0, paint);
    
                    paint.setAlpha(255);
                    stars.draw(canvas, paint);
                    projectiles.draw(canvas, paint);
                    character.draw(canvas, paint);
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
            double elapsedShot = (now - mLastShot);

            // Update the default game play
            if (elapsedFrame > 33) {
                stars.move(js.getX1(), js.getY1());
                character.setDirection(js.getX2());
                projectiles.update();
                mLastTime = now;
            }

            if (elapsedShot > 1000) {
                if (js.getX2() != 0 || js.getY2() != 0) {
                    projectiles.add(character.getShotX(), character.getShotY(), js.getX2(), js.getY2());
                    mLastShot = now;
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
                        if (mState == STATE_RUNNING) update();
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

        public void setState(int state) {
            synchronized (mSurfaceHolder) {
                mState = state;
            }
        }
 
        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mState == STATE_RUNNING) setState(STATE_PAUSE);
            }
        }

        public void unpause() {
            synchronized (mSurfaceHolder) {
                mLastTime = System.currentTimeMillis() + 100;
            }
            setState(STATE_RUNNING);
        }

        public Bundle saveState(Bundle map) {
            synchronized (mSurfaceHolder) {
                if (map != null) {
                    //TODO: add save state stuff; example:
                    //map.putInt(KEY_DIFFICULTY, Integer.valueOf(mDifficulty));
                }
            }
            return map;
        }

        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
                setState(STATE_PAUSE);
                //TODO: add restore stuff; example:
                //mX = savedState.getDouble(KEY_X);
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
                background = Bitmap.createScaledBitmap(background, width, height, false);
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
            js.touch(event);
            return true;
        }
    }

    public sector25view(Context context, AttributeSet attrs) {
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new sector25thread(holder, context, new Handler() {});
        setFocusable(true);
    }

    public sector25thread getThread() {
        return thread;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
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
        //TODO: stop thread.start() from being called twice
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        //TODO: thread.interrupt instead of join? 
        //http://stackoverflow.com/questions/12714887/surfaceview-and-thread-already-started-exception
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
