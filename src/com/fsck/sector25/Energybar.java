package com.fsck.sector25;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Energybar {
    private static Bitmap mEnergyOutline;
    private static Bitmap mEnergyFill;
    public int mEnergy = 100;
    public final int mEnergyStart = 100;

    public Energybar(Resources res) {
        mEnergyOutline = BitmapFactory.decodeResource(res, R.drawable.health1);
        mEnergyFill = BitmapFactory.decodeResource(res, R.drawable.health3);
    }

    public void incrementEnergy(int increment) {
        mEnergy += increment;
    }

    public int getEnergy() {
        return mEnergy;
    }

    public void draw(Canvas canvas, int x, int y) {
        canvas.drawBitmap(mEnergyOutline, x - mEnergyOutline.getWidth() / 2, y
                - mEnergyOutline.getHeight() / 2, null);
        Rect source = new Rect(0, 0, mEnergyFill.getWidth() * mEnergy / 100,
                mEnergyFill.getHeight());
        Rect dest = new Rect(x - mEnergyFill.getWidth() / 2, y - mEnergyFill.getHeight() / 2,
                x - mEnergyFill.getWidth() / 2 + mEnergyFill.getWidth() * mEnergy / 100,
                y + mEnergyFill.getHeight() / 2);
        canvas.drawBitmap(mEnergyFill, source, dest, null);
    }

    public void reset() {
        mEnergy = mEnergyStart;
    }
}
