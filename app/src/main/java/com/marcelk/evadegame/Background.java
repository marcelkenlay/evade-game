package com.marcelk.evadegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Marcel on 31/07/2016.
 */
public class Background {
    private Bitmap image,collidedImage;
    private int x,y,dy;

    public Background(Bitmap res1, Bitmap res2)
    {
        image = res1;
        collidedImage = res2;
        dy = 0;
        x=0;
        y= 0;
    }
    public void update(){
        y+=dy;
        if(y>GamePanel.HEIGHT ){
            y =0;
        }
    }

    public void draw(Canvas canvas){
        if(System.currentTimeMillis() - GamePanel.lastCollision <500) {
            canvas.drawBitmap(image,x,y,null);
        }else{canvas.drawBitmap(collidedImage,x,y,null);}
        if(y>0){
            if(System.currentTimeMillis() - GamePanel.lastCollision <500) {
                canvas.drawBitmap(image,x,y-GamePanel.HEIGHT,null);
            }else{canvas.drawBitmap(collidedImage,x,y-GamePanel.HEIGHT,null);}
        }
    }


    public void setVector(int dy)
    {
        this.dy = dy;
    }
}
