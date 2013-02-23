package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Menu {

    private int page;
    private static int width;
    private static int height;
    private static Bitmap menu;
    private static Bitmap map;
    private static Bitmap select;
    private int selected;
    private static int[][] buttons;

    public Menu() {
    }

    public static void set(Resources res, int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
        menu = BitmapFactory.decodeResource(res, R.drawable.menu);
        menu = Bitmap.createScaledBitmap(menu, (int) (width * 0.45),
                (int) (height), false);
        map = BitmapFactory.decodeResource(res, R.drawable.map1);
        map = Bitmap.createScaledBitmap(map, width, height, false);
        select = BitmapFactory.decodeResource(res, R.drawable.selected);
        select = Bitmap.createScaledBitmap(select,
                (int) (menu.getHeight() / 13.75), (int) (menu.getHeight() / 7),
                false);
        buttons = new int[][] {
                { width / 5 * 3, height / 15, width, height / 10 * 3 },
                { width / 5 * 3, height / 10 * 3, width, height / 2 },
                { width / 5 * 3, height / 2, width, height / 10 * 7 },
                { width / 5 * 3, height / 10 * 7, width, height / 13 * 12 }, };
    }

    public void draw(Canvas canvas, Paint paint) {
        if (page == 0) {
            canvas.drawBitmap(menu, width - menu.getWidth(), 0, paint);
            if (selected > 0) {
                canvas.drawBitmap(select, width - select.getWidth(), selected
                        * height / 4.45f - height / 8, paint);
            }
        } else if (page == 1) {
            canvas.drawBitmap(map, 0, 0, paint);
        }
    }

    private void select(float x, float y) {
        selected = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (x > buttons[i][0] && x < buttons[i][2] && y > buttons[i][1]
                    && y < buttons[i][3]) {
                selected = i + 1;
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
                    page = 1;
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
            } else if (page == 1) {
                sector25view.startGame();
            }
            selected = 0;
        }

        if (eventAction == MotionEvent.ACTION_MOVE) {
            select(event.getX(0), event.getY(0));
        }
    }

    public int page() {
        return page;
    }
}
