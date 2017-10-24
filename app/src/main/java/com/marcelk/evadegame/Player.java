package com.marcelk.evadegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;

/**
 * Created by Marcel on 31/07/2016.
 */
public class Player extends GameObject{
    private Bitmap straight,left,right;
    private int health = 100;
    private double enemyspeed=60;
    private double distance=0;
    private double enemyDistance = -1;
    private double dya,dxa,maxSpeed;
    private boolean accelerate,decelerate,turnleft,turnright;
    private long lastspeedupdate;

    public Player(Bitmap straight,Bitmap left,Bitmap right,int w,int h,int maxSpeed,double distanceAhead)
    {
        this.straight = straight;
        this.left = left;
        this.right = right;
        x = (GamePanel.WIDTH / 2)-(w/2);
        y = GamePanel.HEIGHT * 47 /60;
        dy = 0;
        dx = 0;
        distance = 0;
        enemyDistance = -distanceAhead;
        height = h;
        width = w;
        rectangle.set(x + straight.getWidth()/30,y + straight.getHeight() / 40,x+w-straight.getWidth()/30,y+ straight.getHeight() - straight.getHeight()/40);
        this.maxSpeed = maxSpeed;
    }

    public double getEnemydistance() {
        return enemyDistance;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getEnemyspeed() {
        return enemyspeed;
    }
    public void setEnemyspeed(double enemyspeed) {
        this.enemyspeed = enemyspeed;
    }

    public double getDxa() {
        return dxa;
    }
    public void setDxa(double dxa) {
        this.dxa = dxa;
    }

    public void setLastspeedupdate(long lastspeedupdate) {
        this.lastspeedupdate = lastspeedupdate;
    }

    public void setAccelerate(boolean accelerate) {
        this.accelerate = accelerate;
    }
    public void setDecelerate(boolean decelerate) {
        this.decelerate = decelerate;
    }

    public void setTurnleft(boolean turnleft) {
        this.turnleft = turnleft;
        System.out.println("LEft altered");
    }
    public void setTurnright(boolean turnright) {
        this.turnright = turnright;
        System.out.println("Right altered");
    }

    public int getHealth() {
        return health;
    }

    public void reducehealth() {
        health -= (dy / 2);
        if (health<0){health=0;}
    }
    public void reducehealthfromside() {
        health -= (dy/4 + dx/4);
        if (health<0){health=0;}
    }
    public void update()
    {
        //Resistive forces
        double friction = -0.1;
        if(rectangle.left<GamePanel.WIDTH/6.2){friction = -0.2;}
        if(rectangle.right>GamePanel.WIDTH*5.2/6){friction = -0.2;}
        dya = friction * dy;
        //Accelerate forwards whilst driving forwards
        if(accelerate && !decelerate && dy >= 0) {dya += 10;}
        //Accelerate backwards whilst driving backwards
        if(!accelerate && decelerate && dy <= 0) {dya -= 10;}
        //Brake whilst driving forwards
        if(!accelerate && decelerate && dy > 0) {dya -= 50;}
        //Brake whilst driving backwards
        if(accelerate && !decelerate && dy < 0) {dya += 50;}
        //If car is colliding
        if(inCollision){dya = -200;}
        long CurrentTime = System.nanoTime();
        //Calculate vertical speed and distance travelled
        dy += dya * (CurrentTime - lastspeedupdate)/1000000000;
        //If in cruise control mode and speed is greater that cruise speed
        if(dy>maxSpeed){dy= maxSpeed;}
        //Update distance by player
        double changeInDistance = dy * (CurrentTime - lastspeedupdate);
        changeInDistance=changeInDistance/1000000000;
        changeInDistance=changeInDistance/3600;
        distance += changeInDistance;
        //Update distance by enemy
        if(GamePanel.gamemode==1){enemyspeed = maxSpeed-2;}
        changeInDistance = enemyspeed * (CurrentTime - lastspeedupdate);
        changeInDistance=changeInDistance/1000000000;
        changeInDistance=changeInDistance/3600;
        enemyDistance += changeInDistance;
        //Update x, dxa and dxa
        dxa = -5*dx;
        if(Math.abs(dx)>0 && !turnleft & !turnright){dx=0;dxa=0;}
        if(turnleft){dxa-=200;}
        if(turnright){dxa+=200;}
        dx += dxa*(CurrentTime - lastspeedupdate)/1000000000;
        x+=dx;
        //Check if x would take player of screen
        if(x<0){x=0;}
        else{
            if(x>GamePanel.WIDTH - width){x=GamePanel.WIDTH - width;}
        }
        rectangle.left =x+GamePanel.WIDTH/40;
        rectangle.right =x+width-GamePanel.WIDTH/40;
        //Change last update
        lastspeedupdate = CurrentTime;
    }
    public void draw(Canvas canvas)
    {
        if(dx==0){canvas.drawBitmap(straight,x,y,null);}
        if(dx>0){canvas.drawBitmap(right,x,y,null);}
        if(dx<0){canvas.drawBitmap(left,x,y,null);}
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectangle,paint);
    }

    public double getDistance() {
        return distance;
    }
}
