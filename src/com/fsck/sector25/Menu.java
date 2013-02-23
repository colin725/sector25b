package com.fsck.sector25;

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
    private Healthbar health;
    private static int[][] buttons;
    private float levelMap[][];
    private static Bitmap[] planets;
    private static Bitmap arrow;

    public Menu() {
    }

    public static void set(Resources res, int screenWidth, int screenHeight) {
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
             * hacky level position stuff
             * 0 : X
             * 1 : Y
             * 2 : bitmap
             * 3,4,5 : possible connections
             */
            case 1:
                levelMap = new float[][]{
                        {width / 15, height/2, 6, 1, 2, 3},
                        {width/5, height/4, 0, 4, 5, -1},
                        {width/11*2.4f, height/2, 1, 5, -1, -1},
                        {width/13*3, height/5*4, 2, 6, -1, -1},
                        {width/5*2.1f, height/5, 3, 7, -1, -1},
                        {width/5*2.05f, height/9*4, 4, 8, -1, -1},
                        {width/5*1.95f, height/6*5, 3, 8, 9, -1},
                        {width/5*3f, height/9*2, 2, 10, -1, -1},
                        {width/5*3, height/2, 1, 11, -1, -1},
                        {width/4*2.3f, height/4*3, 0, 11, -1, -1},
                        {width/5*3.76f, height/3.2f, 3, 12, -1, -1},
                        {width/5*3.82f, height/5*3.5f, 4, 12, -1, -1},
                        {width/12*11.1f, height/2.05f, 5, -1, -1, -1}
                };
        }

        button1X = width / 5 * 3.25f;
        button1Y = height / 2f;
        button2X = width / 5 * 3.25f;
        button2Y = height / 4 * 2.75f;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (page == 0) {
            canvas.drawBitmap(menu, width - menu.getWidth(), 0, paint);
            if (selected > 0) {
                canvas.drawBitmap(select, width - select.getWidth(), selected
                        * height / 4.45f - height / 8, paint);
            }
        } else if (page == 1) {
            if (selected > 0) {
                canvas.drawBitmap(sel, levelMap[selected][0] - sel.getWidth() / 2,
                        levelMap[selected][1] - sel.getHeight() / 2, paint);
            }
            for (float[] planet : levelMap) {
                canvas.drawBitmap(planets[(int) planet[2]], planet[0]
                        - planets[(int) planet[2]].getWidth() / 2, planet[1]
                        - planets[(int) planet[2]].getHeight() / 2, paint);
                for (int i = 0; i < 3; i++) {
                    if (planet[3 + i] > 0) {
                        float levelX = levelMap[(int) planet[3 + i]][0];
                        float levelY = levelMap[(int) planet[3 + i]][1];
                        float arrowX = planet[0] + (levelX - planet[0]) / 2;
                        float arrowY = planet[1] + (levelY - planet[1]) / 2;
                        float degrees = (float) Math.toDegrees(Math.atan((levelY
                                - planet[1]) / Math.abs(levelX - planet[0])));
                        canvas.save();
                        canvas.rotate(degrees, arrowX, arrowY);
                        canvas.drawBitmap(arrow, arrowX - arrow.getWidth() / 2,
                                arrowY - arrow.getHeight() / 2, paint);
                        canvas.restore();
                    }
                }
            }
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
                    popup = selected;
                } else {
                    if (selected == 1) {
                        //TODO: set up level stuff
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
}
