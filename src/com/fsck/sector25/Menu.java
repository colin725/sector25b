package com.fsck.sector25;

import com.fsck.sector25.GameHUD.GameStyle;
import com.fsck.sector25.sector25view.GameState;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Menu {

    private static MenuPage mPage = MenuPage.MAINMENU;
    private static int mWidth;
    private static int mHeight;
    private static Bitmap mMenu;
    private static Bitmap mPopupmenu;
    private static Bitmap mButton;
    private static Bitmap mSelect;
    private static Bitmap mSelect2;
    private static Bitmap mSelect3;
    private static Bitmap mSel;
    private float mButtonX1;
    private float mButtonY1;
    private float mButtonX2;
    private float mButtonY2;
    private int mSelected;
    private int mPopup;
    private int mMapPosition;
    private Healthbar mHealth;
    private static int[][] mButtons;
    private float mLevelMap[][];
    private int[][] mConnection;
    private static Bitmap[] mPlanets;
    private static Bitmap mArrow;

    public enum MenuPage {
        MAINMENU, LEVELSELECT
    }

    public Menu() {
    }

    public static void setSize(Resources res, int screenWidth, int screenHeight) {
        mWidth = screenWidth;
        mHeight = screenHeight;
        mMenu = BitmapFactory.decodeResource(res, R.drawable.menu);
        mMenu = Bitmap.createScaledBitmap(mMenu, (int) (mWidth * 0.45),
                (int) (mHeight), false);
        mSelect = BitmapFactory.decodeResource(res, R.drawable.selected);
        mSelect2 = BitmapFactory.decodeResource(res, R.drawable.selected2);
        mSelect = Bitmap.createScaledBitmap(mSelect, (int) (mMenu.getHeight() / 13.75),
                (int) (mMenu.getHeight() / 7), false);
        mSelect3 = BitmapFactory.decodeResource(res, R.drawable.selected3);
        mSelect3 = Bitmap.createScaledBitmap(mSelect3, (int) (mWidth / 3.75f),
                (int) (mWidth / 13f), false);
        mButtons = new int[][] {
                { mWidth / 5 * 3, mHeight / 15, mWidth, mHeight / 10 * 3 },
                { mWidth / 5 * 3, mHeight / 10 * 3, mWidth, mHeight / 2 },
                { mWidth / 5 * 3, mHeight / 2, mWidth, mHeight / 10 * 7 },
                { mWidth / 5 * 3, mHeight / 10 * 7, mWidth, mHeight / 13 * 12 }, };

        mArrow = BitmapFactory.decodeResource(res, R.drawable.arrow);
        mArrow = Bitmap.createScaledBitmap(mArrow, mWidth / 20, mWidth / 50, false);
        mPopupmenu = BitmapFactory.decodeResource(res, R.drawable.menuback);
        mPopupmenu = Bitmap.createScaledBitmap(mPopupmenu, mWidth / 4 * 3, mHeight / 4 * 3, false);
        mButton = BitmapFactory.decodeResource(res, R.drawable.button);
        mButton = Bitmap.createScaledBitmap(mButton, mWidth / 4, mWidth / 16, false);

        mPlanets = new Bitmap[7];

        mPlanets[0] = BitmapFactory.decodeResource(res, R.drawable.planet1);
        mPlanets[1] = BitmapFactory.decodeResource(res, R.drawable.planet2);
        mPlanets[2] = BitmapFactory.decodeResource(res, R.drawable.planet3);
        mPlanets[3] = BitmapFactory.decodeResource(res, R.drawable.planet4);
        mPlanets[4] = BitmapFactory.decodeResource(res, R.drawable.planet5);
        mPlanets[5] = BitmapFactory.decodeResource(res, R.drawable.planet6);

        mPlanets[6] = BitmapFactory.decodeResource(res, R.drawable.mapguy);
        mPlanets[6] = Bitmap.createScaledBitmap(mPlanets[6], mWidth / 15, mWidth / 10,
                false);

        for(int i = 0; i < mPlanets.length-2; i++){
            mPlanets[i] = Bitmap.createScaledBitmap(mPlanets[i], (int)((float)mPlanets[i].getWidth()/4000*(float)mWidth),
                    (int)((float)mPlanets[i].getWidth()/4000*(float)mWidth), false);
        }
        mPlanets[5] = Bitmap.createScaledBitmap(mPlanets[5], (int)((float)mPlanets[5].getWidth()/2000*(float)mWidth),
                (int)((float)mPlanets[5].getWidth()/2000*(float)mWidth), false);
    }

    public void setMenuMap(int level) {
        // TODO: Load levels from a text file, or dynamically create them.
        // Probably go for the dynamic, just add some randomness.
        switch (level) {
            /*
             * mLevelMap marks the mPlanets
             * 0 : X
             * 1 : Y
             * 2 : bitmap (which kind of planet, which also denotes gameplay)
             * 
             * mConnection connects them
             * 0, 1, 2 are possible connections
             * 3 is a chosen path
             */
            case 1:
                mLevelMap = new float[][]{
                        {mWidth / 15, mHeight/2, -1},
                        {mWidth/5, mHeight/4, 0},
                        {mWidth/11*2.4f, mHeight/2, 1},
                        {mWidth/13*3, mHeight/5*4, 2},
                        {mWidth/5*2.1f, mHeight/5, 3},
                        {mWidth/5*2.05f, mHeight/9*4, 4},
                        {mWidth/5*1.95f, mHeight/6*5, 3},
                        {mWidth/5*3f, mHeight/9*2, 2},
                        {mWidth/5*3, mHeight/2, 1},
                        {mWidth/4*2.3f, mHeight/4*3, 0},
                        {mWidth/5*3.76f, mHeight/3.2f, 3},
                        {mWidth/5*3.82f, mHeight/5*3.5f, 4},
                        {mWidth/12*11.1f, mHeight/2.05f, 5}
                };

                mConnection = new int[][]{
                    {1, 2, 3, 0},
                    {4, 5, -1, 0},
                    {5, -1, -1, 0},
                    {6, -1, -1, 0},
                    {7, -1, -1, 0},
                    {8, -1, -1, 0},
                    {8, 9, -1, 0},
                    {10, -1, -1, 0},
                    {11, -1, -1, 0},
                    {11, -1, -1, 0},
                    {12, -1, -1, 0},
                    {12, -1, -1, 0},
                    {-1, -1, -1, 0}
                };
                break;
        }
        mMapPosition = 0;

        mButtonX1 = mWidth / 5 * 3.25f;
        mButtonY1 = mHeight / 2f;
        mButtonX2 = mWidth / 5 * 3.25f;
        mButtonY2 = mHeight / 4 * 2.75f;
    }

    /*
     * Moves the character along the level selection
     */
    private void mapMove(int position) {
        for(int i = 0; i < mConnection.length; i++){
            mConnection[i][3] = -1;
        }
        clearConnect(position);
        mMapPosition = position;
    }


    private void clearConnect(int position) {
        mConnection[position][3] = 0;
        for(int i = 0; i < 3; i++) {
            if(mConnection[position][i] >= 0) {
                clearConnect(mConnection[position][i]);
            }
        }
    }

    public void draw(Canvas canvas, Paint paint, GameState state) {
        // Dull the stuff into background on pause/die/win menus
        if (state == GameState.STATE_PAUSE || state == GameState.STATE_DEAD ||
                state == GameState.STATE_WIN) {
            canvas.drawARGB(155, 0, 0, 0);
        }

        if (state == GameState.STATE_MENU) {
            // TODO: Animate mMenu in and out

            switch (mPage) {
                case MAINMENU:
                    canvas.drawBitmap(mMenu, mWidth - mMenu.getWidth(), 0, paint);
                    if (mSelected > 0) {
                        canvas.drawBitmap(mSelect, mWidth - mSelect.getWidth(), mSelected
                            * mHeight / 4.45f - mHeight / 8, paint);
                    }
                    break;

                case LEVELSELECT:
                    //draw mSelected circle if hovering/mSelected
                    if (mSelected > 0) {
                        canvas.drawBitmap(mSel, mLevelMap[mSelected][0] - mSel.getWidth() / 2,
                                mLevelMap[mSelected][1] - mSel.getHeight() / 2, paint);
                    }
    
                    drawPlanets(canvas, paint);
                    drawConnections(canvas, paint);
                    drawPopup(canvas, paint);
                    break;
            }
        }

        drawMiscText(canvas, paint, state);
    }

    // Draw mPlanets on level path
    private void drawPlanets(Canvas canvas, Paint paint) {
        int count = 0;
        for (float[] planet : mLevelMap) {
            if (planet[2] >= 0 && count != mMapPosition){
                canvas.drawBitmap(mPlanets[(int) planet[2]], planet[0]
                    - mPlanets[(int) planet[2]].getWidth() / 2, planet[1]
                    - mPlanets[(int) planet[2]].getHeight() / 2, paint);
            } else if (count == mMapPosition) {
                canvas.drawBitmap(mPlanets[6],
                        planet[0] - mPlanets[6].getWidth() / 2, 
                        planet[1] - mPlanets[6].getHeight() / 2, paint);
            }
            count++;
        }
    }

    // Draw connections between mPlanets on level path
    private void drawConnections(Canvas canvas, Paint paint) {
        int count = 0;
        for (int[] connect : mConnection) {
            for (int i = 0; i < 3; i++) {
                if (connect[i] > 0) {
                    float levelX = mLevelMap[connect[i]][0];
                    float levelY = mLevelMap[connect[i]][1];
                    float arrowX = mLevelMap[count][0] / 2 + levelX / 2;
                    float arrowY = mLevelMap[count][1] / 2 + levelY / 2;
                    float degrees = (float) Math.toDegrees(Math.atan((levelY
                            - arrowY) / Math.abs(levelX - arrowX)));

                    if (connect[3] < 0 || connect[3] > 0 && connect[3] != i) {
                        // path was not chosen, draw as faded
                        paint.setAlpha(40);
                    }

                    canvas.save();
                    canvas.rotate(degrees, arrowX, arrowY);
                    canvas.drawBitmap(mArrow, arrowX - mArrow.getWidth() / 2,
                            arrowY - mArrow.getHeight() / 2, paint);
                    canvas.restore();
                    paint.setAlpha(255);
                }
            }
            count++;
        }
    }

    // Draw pop-up mMenu when user selects a level
    private void drawPopup(Canvas canvas, Paint paint) {
        if (mPopup > 0) {
            paint.setTextSize(40);

            // draw the menu background pop-up thing
            canvas.drawBitmap(mPopupmenu, mWidth / 2 - mPopupmenu.getWidth()
                    / 2, mHeight / 2 - mPopupmenu.getHeight() / 2, paint);

            // draw the planet on the pop-up
            canvas.save();
            canvas.scale(2, 2, mWidth / 3.4f, mHeight / 3);
            canvas.drawBitmap(mPlanets[(int) mLevelMap[mPopup][2]], mWidth / 3.4f
                    - mPlanets[(int) mLevelMap[mPopup][2]].getWidth() / 2, mHeight / 3
                    - mPlanets[(int) mLevelMap[mPopup][2]].getHeight() / 2, paint);
            canvas.restore();

            // show current health on the pop-up
            canvas.drawText("Current Health", (mWidth / 5.5f), mHeight / 4 * 2.8f, paint);
            mHealth.draw(canvas, (int) (mWidth / 3.4f), mHeight / 4 * 3);

            // mission text depending on color or selected planet
            drawMissionTest(canvas, paint);

            // Draw option buttons (start, cancel)
            canvas.drawBitmap(mButton, mButtonX1 - mButton.getWidth() / 2,
                    mButtonY1 - mButton.getHeight() / 2, paint);
            canvas.drawBitmap(mButton, mButtonX2 - mButton.getWidth() / 2,
                    mButtonY2 - mButton.getHeight() / 2, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText("Start", (mWidth / 5 * 3.08f), mHeight / 1.95f , paint);
            canvas.drawText("Cancel", (mWidth / 5 * 3), mHeight / 4 * 2.8f , paint);
            paint.setColor(Color.WHITE);

            // Draw outline around hovered button
            if (mSelected == 1) {
                canvas.drawBitmap(mSelect3, mButtonX1 - mSelect3.getWidth() / 2,
                        mButtonY1 - mSelect3.getHeight() / 2, paint);
            } else if (mSelected == 2) {
                canvas.drawBitmap(mSelect3, mButtonX2 - mSelect3.getWidth() / 2,
                        mButtonY2 - mSelect3.getHeight() / 2, paint);
            }
        }
    }

    private void drawMissionTest(Canvas canvas, Paint paint) {
        /*
         * Draw text for current mission on the level
         * TODO: scale text according to screen mWidth, center text
         * http://catchthecows.com/?p=72
         */

        // default to time
        String text1 = "";
        String text2 = "";
        switch (getGameStyle()) {
            case TIME:
                text1 = "Survive for";
                text2 = "60 seconds";
                break;
            case KILLS:
                text1 = "Kill 90";
                text2 = "enemies";
                break;
            case DISTANCE:
                text1 = "Reach a distance of";
                text2 = "100m to the right";
                break;
            case BOSS:
                text1 = "Just kill the";
                text2 = "boss man";
                break;
        }
        canvas.drawText("Current Mission:", (mWidth / 5 * 2.5f), mHeight / 4 , paint);
        canvas.drawText(text1, (mWidth / 5 * 2.5f), mHeight / 3.35f , paint);
        canvas.drawText(text2, (mWidth / 5 * 2.5f), mHeight / 2.9f , paint);

    }

    public void drawMiscText(Canvas canvas, Paint paint, GameState state) {
        paint.setTextSize(40);
        int score = GameHUD.getScore();
        if (state == GameState.STATE_DEAD) {
            canvas.drawText("GAME OVER", mWidth / 2 - 100, mHeight / 2, paint);
            canvas.drawText("Score: " + score, mWidth / 2 - 100,
                    mHeight / 2 + 100, paint);
        } else if (state == GameState.STATE_WIN) {
            canvas.drawText("Level complete!", mWidth / 2 - 100, mHeight / 2,
                    paint);
            canvas.drawText("Score: " + score, mWidth / 2 - 100,
                    mHeight / 2 + 100, paint);
        }
    }

    // Check if user is hovering over a planet in the menu
    private void select(float x, float y) {
        mSelected = 0;
        if (mPage == MenuPage.MAINMENU) {
            for (int i = 0; i < mButtons.length; i++) {
                if (x > mButtons[i][0] && x < mButtons[i][2] && y > mButtons[i][1]
                        && y < mButtons[i][3]) {
                    mSelected = i + 1;
                }
            }
        } else {
            if (mPopup == 0) {
                for (int i = 1; i < mLevelMap.length; i++){
                    if (contains(x, y, mLevelMap[i][0], mLevelMap[i][1], mPlanets[(int) mLevelMap[i][2]].getWidth(),
                            mPlanets[(int) mLevelMap[i][2]].getHeight())) {
                        mSelected = i;
                        mSel = Bitmap.createScaledBitmap(mSelect2, (int)(mPlanets[(int) mLevelMap[i][2]].getWidth()*1.2f), 
                                (int)(mPlanets[(int) mLevelMap[i][2]].getHeight()*1.2f), false);
                    }
                }
            } else {
                if (contains(x, y, mButtonX1, mButtonY1, mButton.getWidth(),
                        mButton.getHeight())) {
                    mSelected = 1;
                }
                if (contains(x, y, mButtonX2, mButtonY2, mButton.getWidth(),
                        mButton.getHeight())) {
                    mSelected = 2;
                }
            }
        }
    }

    boolean contains(float x, float y, float x2, float y2, int width, int height) {
        return (x > x2 - width / 2 && x < x2 + width / 2 &&
                y > y2 - height / 2 && y < y2 + height / 2);
    }

    public void touch(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            select(event.getX(0), event.getY(0));
        }

        if (eventAction == MotionEvent.ACTION_UP) {
            switch (mPage) {
                case MAINMENU:
                    switch (mSelected) {
                        case 1:
                            /*
                             *  Play button.  This starts a new game so reset anything
                             *  which remains between levels (health).
                             */
                            mPage = MenuPage.LEVELSELECT;
                            mHealth.reset();
                            GameHUD.setScore(0);
                            setMenuMap(1);
                            break;
    
                        case 2:
                            // Arcade
                            break;
                        case 3:
                            // Scores
                            break;
                        case 4:
                            // About
                            break;
                    }
                    break;

                case LEVELSELECT:
                    /*
                     * Level select screen
                     */
                    if (mSelected > 0 && mPopup == 0) {
                        // Selected a planet/level
                        if (mConnection[mMapPosition][0] == mSelected
                                || mConnection[mMapPosition][1] == mSelected
                                || mConnection[mMapPosition][2] == mSelected) {
                            mPopup = mSelected;
                        }
                    } else {
                        // pop-up to start level
                        if (mSelected == 1) {
                            // Set up level
                            mapMove(mPopup);
                            GameHUD.clear();
                            GameHUD.setGameStyle(getGameStyle());
                            mPopup = 0;
                            sector25view.startGame();
                        } else if (mSelected == 2) {
                            // cancel
                            mPopup = 0;
                        }
                    }
                    break;
            }
            mSelected = 0;
        }

        if (eventAction == MotionEvent.ACTION_MOVE) {
            select(event.getX(0), event.getY(0));
        }
    }

    private GameStyle getGameStyle() {
        if (!(mPopup > 0)) {
            // broken game logic, this shouldn't happen
            Log.e("S25", "getGameStyle error");
            return null;
        }
        int planetColor = (int)mLevelMap[mPopup][2];
        GameStyle gs = GameStyle.TIME;
        switch (planetColor) {
            case 0:
                gs = GameStyle.KILLS;
                break;
            case 1:
                gs = GameStyle.TIME;
                break;
            case 2:
                gs = GameStyle.DISTANCE;
                break;
            case 3:
                gs = GameStyle.KILLS; // placeholder
                break;
            case 4:
                gs = GameStyle.DISTANCE; // placeholder
                break;
            case 5:
                gs = GameStyle.BOSS;
                break;
        }
        return gs;
    }

    public void setHealth(Healthbar health) {
        this.mHealth = health;
    }

    public void resetPage(){
        mPage = MenuPage.MAINMENU;
    }

    public static MenuPage getPage() {
        return mPage;
    }
}
