package com.fsck.sector25;

import com.fsck.sector25.sector25view.GameState;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Menu {

    private int page;
    private static int width;
    private static int height;
    private static Bitmap menu;
    private static Bitmap popupmenu;
    private static Bitmap button;
    private static Bitmap select;
    private static Bitmap select2;
    private static Bitmap select3;
    private static Bitmap sel;
    private float button1X;
    private float button1Y;
    private float button2X;
    private float button2Y;
    private int selected;
    private int popup;
    private int mapPosition;
    private Healthbar health;
    private static int[][] buttons;
    private float levelMap[][];
    private int[][] connection;
    private static Bitmap[] planets;
    private static Bitmap arrow;

    public Menu() {
    }

    public static void setSize(Resources res, int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
        menu = BitmapFactory.decodeResource(res, R.drawable.menu);
        menu = Bitmap.createScaledBitmap(menu, (int) (width * 0.45),
                (int) (height), false);
        select = BitmapFactory.decodeResource(res, R.drawable.selected);
        select2 = BitmapFactory.decodeResource(res, R.drawable.selected2);
        select = Bitmap.createScaledBitmap(select, (int) (menu.getHeight() / 13.75),
                (int) (menu.getHeight() / 7), false);
        select3 = BitmapFactory.decodeResource(res, R.drawable.selected3);
        select3 = Bitmap.createScaledBitmap(select3, (int) (width / 3.75f),
                (int) (width / 13f), false);
        buttons = new int[][] {
                { width / 5 * 3, height / 15, width, height / 10 * 3 },
                { width / 5 * 3, height / 10 * 3, width, height / 2 },
                { width / 5 * 3, height / 2, width, height / 10 * 7 },
                { width / 5 * 3, height / 10 * 7, width, height / 13 * 12 }, };

        arrow = BitmapFactory.decodeResource(res, R.drawable.arrow);
        arrow = Bitmap.createScaledBitmap(arrow, width / 20, width / 50, false);
        popupmenu = BitmapFactory.decodeResource(res, R.drawable.menuback);
        popupmenu = Bitmap.createScaledBitmap(popupmenu, width / 4 * 3, height / 4 * 3, false);
        button = BitmapFactory.decodeResource(res, R.drawable.button);
        button = Bitmap.createScaledBitmap(button, width / 4, width / 16, false);

        planets = new Bitmap[7];

        planets[0] = BitmapFactory.decodeResource(res, R.drawable.planet1);
        planets[1] = BitmapFactory.decodeResource(res, R.drawable.planet2);
        planets[2] = BitmapFactory.decodeResource(res, R.drawable.planet3);
        planets[3] = BitmapFactory.decodeResource(res, R.drawable.planet4);
        planets[4] = BitmapFactory.decodeResource(res, R.drawable.planet5);
        planets[5] = BitmapFactory.decodeResource(res, R.drawable.planet6);

        planets[6] = BitmapFactory.decodeResource(res, R.drawable.mapguy);
        planets[6] = Bitmap.createScaledBitmap(planets[6], width / 15, width / 10,
                false);

        for(int i = 0; i < planets.length-2; i++){
            planets[i] = Bitmap.createScaledBitmap(planets[i], (int)((float)planets[i].getWidth()/4000*(float)width),
                    (int)((float)planets[i].getWidth()/4000*(float)width), false);
        }
        planets[5] = Bitmap.createScaledBitmap(planets[5], (int)((float)planets[5].getWidth()/2000*(float)width),
                (int)((float)planets[5].getWidth()/2000*(float)width), false);
    }

    public void setMenuMap(int level) {
        switch (level) {
            /*
             * levelMap marks the planets
             * 0 : X
             * 1 : Y
             * 2 : bitmap
             * 
             * connection connects them
             * 0, 1, 2 are possible connections
             * 3 is a chosen path
             */
            case 1:
                levelMap = new float[][]{
                        {width / 15, height/2, -1},
                        {width/5, height/4, 0},
                        {width/11*2.4f, height/2, 1},
                        {width/13*3, height/5*4, 2},
                        {width/5*2.1f, height/5, 3},
                        {width/5*2.05f, height/9*4, 4},
                        {width/5*1.95f, height/6*5, 3},
                        {width/5*3f, height/9*2, 2},
                        {width/5*3, height/2, 1},
                        {width/4*2.3f, height/4*3, 0},
                        {width/5*3.76f, height/3.2f, 3},
                        {width/5*3.82f, height/5*3.5f, 4},
                        {width/12*11.1f, height/2.05f, 5}
                };

                connection = new int[][]{
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
        mapPosition = 0;

        button1X = width / 5 * 3.25f;
        button1Y = height / 2f;
        button2X = width / 5 * 3.25f;
        button2Y = height / 4 * 2.75f;
    }

    private void mapMove(int position) {
        for(int i = 0; i < connection.length; i++){
            connection[i][3] = -1;
        }
        clearConnect(position);
        mapPosition = position;
    }


    private void clearConnect(int position) {
        connection[position][3] = 0;
        for(int i = 0; i < 3; i++) {
            if(connection[position][i] >= 0) {
                clearConnect(connection[position][i]);
            }
        }
    }

    public void draw(Canvas canvas, Paint paint, GameState state, int score) {
        // Dull the stuff into background
        if (state == GameState.STATE_PAUSE || state == GameState.STATE_DEAD ||
                state == GameState.STATE_WIN) {
            canvas.drawARGB(155, 0, 0, 0);
        }

        if (state == GameState.STATE_MENU) {
            // TODO: Menu
            // 1) Animate in and out
            // 3) Add hover differentiation on buttons
                // I don't think this is needed anymore....

            /*
             * Page 0
             * Main menu
             */
            if (page == 0) {
                canvas.drawBitmap(menu, width - menu.getWidth(), 0, paint);
                if (selected > 0) {
                    canvas.drawBitmap(select, width - select.getWidth(), selected
                            * height / 4.45f - height / 8, paint);
                }
            } 

            /*
             * Page 1
             * Map / level selection
             */
            else if (page == 1) {
                //draw selected circle
                if (selected > 0) {
                    canvas.drawBitmap(sel, levelMap[selected][0] - sel.getWidth() / 2,
                            levelMap[selected][1] - sel.getHeight() / 2, paint);
                }

                //draw planets
                int count = 0;
                for (float[] planet : levelMap) {
                    if (planet[2] >= 0 && count != mapPosition){
                        canvas.drawBitmap(planets[(int) planet[2]], planet[0]
                            - planets[(int) planet[2]].getWidth() / 2, planet[1]
                            - planets[(int) planet[2]].getHeight() / 2, paint);
                    } else if (count == mapPosition) {
                        canvas.drawBitmap(planets[6],
                                planet[0] - planets[6].getWidth() / 2, 
                                planet[1] - planets[6].getHeight() / 2, paint);
                    }
                    count++;
                }

                //draw connection arrows
                count = 0;
                for (int[] connect : connection) {
                    for (int i = 0; i < 3; i++) {
                        if (connect[i] > 0) {
                            float levelX = levelMap[connect[i]][0];
                            float levelY = levelMap[connect[i]][1];
                            float arrowX = levelMap[count][0] / 2 + levelX / 2;
                            float arrowY = levelMap[count][1] / 2 + levelY / 2;
                            float degrees = (float) Math.toDegrees(Math.atan((levelY
                                    - arrowY) / Math.abs(levelX - arrowX)));

                            if (connect[3] < 0 || connect[3] > 0 && connect[3] != i) {
                                // path was not used
                                paint.setAlpha(40);
                            }
                            canvas.save();
                            canvas.rotate(degrees, arrowX, arrowY);
                            canvas.drawBitmap(arrow, arrowX - arrow.getWidth() / 2,
                                    arrowY - arrow.getHeight() / 2, paint);
                            canvas.restore();
                            paint.setAlpha(255);
                        }
                    }
                    count++;
                }

                //draw pop-up on click
                if (popup > 0) {
                    canvas.drawBitmap(popupmenu, width / 2 - popupmenu.getWidth()
                            / 2, height / 2 - popupmenu.getHeight() / 2, paint);
                    canvas.save();
                    canvas.scale(2, 2, width / 3.4f, height / 3);
                    canvas.drawBitmap(planets[(int) levelMap[popup][2]], width / 3.4f
                            - planets[(int) levelMap[popup][2]].getWidth() / 2, height / 3
                            - planets[(int) levelMap[popup][2]].getHeight() / 2, paint);
                    canvas.restore();
                    health.draw(canvas, (int) (width / 3.4f), height / 4 * 3);
                    /*
                     * TODO: scale text according to screen width
                     * http://catchthecows.com/?p=72
                     */
                    canvas.drawText("Current Health", (width / 5f), height / 4 * 2.8f, paint);
                    canvas.drawText("Current Mission:", (width / 5 * 2.5f), height / 4 , paint);
                    canvas.drawText("Survive for", (width / 5 * 2.5f), height / 3.35f , paint);
                    canvas.drawText("60 seconds", (width / 5 * 2.5f), height / 2.9f , paint);

                    canvas.drawBitmap(button, button1X - button.getWidth() / 2,
                            button1Y - button.getHeight() / 2, paint);
                    canvas.drawBitmap(button, button2X - button.getWidth() / 2,
                            button2Y - button.getHeight() / 2, paint);
                    paint.setColor(Color.BLACK);
                    canvas.drawText("Start", (width / 5 * 3.08f), height / 1.95f , paint);
                    canvas.drawText("Cancel", (width / 5 * 3), height / 4 * 2.8f , paint);
                    paint.setColor(Color.WHITE);
                    if (selected == 1) {
                        canvas.drawBitmap(select3, button1X - select3.getWidth() / 2,
                                button1Y - select3.getHeight() / 2, paint);
                    } else if (selected == 2) {
                        canvas.drawBitmap(select3, button2X - select3.getWidth() / 2,
                                button2Y - select3.getHeight() / 2, paint);
                    }
                }
            }
        }

        drawText(canvas, paint, state, score);
    }

    private void select(float x, float y) {
        selected = 0;
        if (page == 0) {
            for (int i = 0; i < buttons.length; i++) {
                if (x > buttons[i][0] && x < buttons[i][2] && y > buttons[i][1]
                        && y < buttons[i][3]) {
                    selected = i + 1;
                }
            }
        } else {
            if (popup == 0) {
                for (int i = 1; i < levelMap.length; i++){
                    if (x > levelMap[i][0] - planets[(int) levelMap[i][2]].getWidth()/2 &&
                            x < levelMap[i][0] + planets[(int) levelMap[i][2]].getWidth()/2 &&
                            y > levelMap[i][1] - planets[(int) levelMap[i][2]].getHeight()/2 &&
                            y < levelMap[i][1] + planets[(int) levelMap[i][2]].getHeight()/2) {
                        selected = i;
                        sel = Bitmap.createScaledBitmap(select2, (int)(planets[(int) levelMap[i][2]].getWidth()*1.2f), 
                                (int)(planets[(int) levelMap[i][2]].getHeight()*1.2f), false);
                    }
                }
            } else {
                if ( x > button1X - button.getWidth() / 2 &&
                        x < button1X + button.getWidth() / 2 &&
                        y > button1Y - button.getHeight() / 2 &&
                        y < button1Y + button.getHeight() / 2) {
                    selected = 1;
                }
                if (x > button2X - button.getWidth() / 2 &&
                        x < button2X + button.getWidth() / 2 &&
                        y > button2Y - button.getHeight() / 2 &&
                        y < button2Y + button.getHeight() / 2) {
                    selected = 2;
                }
            }
        }
    }

    public void touch(MotionEvent event) {
        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN) {
            select(event.getX(0), event.getY(0));
        }

        if (eventAction == MotionEvent.ACTION_UP) {

            if (page == 0) {
                switch (selected) {
                case 1:
                    // Play
                    if (selected == 1) {
                        page = 1;
                        setMenuMap(1);
                    }
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
            }
            else if (page == 1) {
                if (selected > 0 && popup == 0) {
                    if (connection[mapPosition][0] == selected ||
                            connection[mapPosition][1] == selected ||
                            connection[mapPosition][2] == selected) {
                        popup = selected;
                    }
                } else {
                    if (selected == 1) {
                        //TODO: set up level stuff
                        mapMove(popup);
                        popup = 0;
                        GameHUD.clear();
                        sector25view.startGame();
                    } else if (selected == 2) {
                        popup = 0;
                    }
                }
            }
            selected = 0;
        }

        if (eventAction == MotionEvent.ACTION_MOVE) {
            select(event.getX(0), event.getY(0));
        }
    }

    public void setHealth(Healthbar health) {
        this.health = health;
    }

    public int page() {
        return page;
    }
    
    public void resetPage(){
        page = 0;
    }

    public void drawText(Canvas canvas, Paint paint, GameState state, int score) {
        paint.setTextSize(40);
        if (state == GameState.STATE_DEAD) {
            canvas.drawText("GAME OVER", width / 2 - 100,
                    height / 2, paint);
            canvas.drawText("Score: " + score, width / 2 - 100,
                    height / 2 + 100, paint);
        } else if (state == GameState.STATE_WIN) {
            canvas.drawText("Level complete!", width / 2 - 100,
                    height / 2, paint);
            canvas.drawText("Score: " + score, width / 2 - 100,
                    height / 2 + 100, paint);
        }
    }
}
