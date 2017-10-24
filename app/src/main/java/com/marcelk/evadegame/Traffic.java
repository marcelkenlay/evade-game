package com.marcelk.evadegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by mkenl on 02/08/2016.
 */
public class Traffic extends GameObject {
    Bitmap image;
    int lane;
    public boolean inSideCollision;

    public boolean isInSideCollision() {
        return inSideCollision;
    }

    public void setInSideCollision(boolean inSideCollision) {
        this.inSideCollision = inSideCollision;
    }

    public int getLane() {
        return lane;
    }

    public Traffic(int speed, int lane, int startingposition, Bitmap image)
    {
        System.out.println("TRAFFIC MADE");
        this.lane = lane;
        switch(lane){
            case 1:
                x = (int)(GamePanel.WIDTH / 4.8);
                break;
            case 2:
                x = (int) (GamePanel.WIDTH / 2.5);
                break;
            case 3:
                x = (int) (GamePanel.WIDTH / 1.55);
        }
        y = startingposition;
        dy = speed;
        rectangle.set(x+image.getWidth()/15,y,x+image.getWidth()*14/15,y+image.getHeight());
        this.image = image;
    }
    public void update(){
        if(inCollision){
            dy = GamePanel.MOVESPEED + 5;
        }
        if(inSideCollision){
            x += dx;
            dy -= dy*0.5;
            dx -= dx*0.5;
            if (rectangle.right > GamePanel.playerLeft && dx<0){
                x = GamePanel.playerLeft - image.getWidth();
            }else if(rectangle.left < GamePanel.playerRight && dx>0){
                x = GamePanel.playerRight;
            }
        }
        rectangle.left = x+image.getWidth()/15;
        rectangle.right = x+image.getWidth()*14/15;
        y += GamePanel.MOVESPEED - dy;
        rectangle.top += GamePanel.MOVESPEED - dy;
        rectangle.bottom += GamePanel.MOVESPEED - dy;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectangle,paint);
    }
}
