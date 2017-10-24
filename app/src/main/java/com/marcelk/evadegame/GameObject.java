package com.marcelk.evadegame;

import android.graphics.Rect;

/**
 * Created by Marcel on 31/07/2016.
 */
public abstract class GameObject {
    protected int x, y, width, height;
    protected double dy,dx;

    public void setInCollision(boolean inCollision) {
        this.inCollision = inCollision;
    }

    protected Rect rectangle = new Rect();

    public boolean isInCollision() {
        return inCollision;
    }

    protected boolean inCollision=false;

    public Rect getRectangle() {
        return rectangle;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
