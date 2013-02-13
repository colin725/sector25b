package com.fsck.sector25;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    private Bitmap animation;
    private Rect sRectangle;
    public int numFrames;
    public int currentFrame;
    private int spriteHeight;
    private int spriteWidth;
    private int update;

    public Sprite() {
    }

    public Sprite(Bitmap bitmap, int frameCount) {
        sRectangle = new Rect(0, 0, 0, 0);
        currentFrame = 0;

        this.animation = bitmap;
        this.spriteHeight = bitmap.getHeight();
        this.spriteWidth = bitmap.getWidth() / frameCount;
        this.sRectangle.top = 0;
        this.sRectangle.bottom = spriteHeight;
        this.sRectangle.left = 0;
        this.sRectangle.right = spriteWidth;
        this.numFrames = frameCount;
    }

    public void Update() {
        update++;
        if (update % 2 == 0) {
            currentFrame = (currentFrame + 1) % numFrames;
            sRectangle.left = currentFrame * spriteWidth;
            sRectangle.right = sRectangle.left + spriteWidth;
        }
    }

    public int getWidth(){
        return animation.getWidth();
    }

    public int getHeight(){
        return animation.getHeight();
    }

    public void draw(Canvas canvas, int x, int y) {
        Rect dest = new Rect(x - spriteWidth/2, y - spriteHeight/2, x + spriteWidth/2,
                y + spriteHeight/2);
        canvas.drawBitmap(animation, sRectangle, dest, null);
    }
}
