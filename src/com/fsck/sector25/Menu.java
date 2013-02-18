package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Menu {

    private static int width;
    private static int height;
    private static Bitmap menu;

    public Menu() {
    }

    public static void set(Resources res, int screenWidth, int screenHeight) {
        width = screenWidth;
        height = screenHeight;
        menu = BitmapFactory.decodeResource(res, R.drawable.menu);
        menu = Bitmap.createScaledBitmap(menu, (int) (width / 7 * 3),
                (int) (height), false);
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(menu, width - menu.getWidth(), 0, paint);
    }
}
