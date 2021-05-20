package kr.ac.jbnu.se.mobile.game2048;

import android.graphics.drawable.Drawable;

public class Rectangle {
    private int startX, startY, endX, endY;

    public Rectangle(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }
}
