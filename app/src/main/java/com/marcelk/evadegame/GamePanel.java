package com.marcelk.evadegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.text.DecimalFormat;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static int WIDTH = 288;
    public static int HEIGHT = 500;
    public static int MOVESPEED = 0;
    public static int playerLeft  = 0;
    public static int playerRight  = 0;
    public static boolean playing = false;
    public static boolean gameOver = false;
    public static int gamemode;
    public static double distance;
    public static long lastCollision = 0;
    public int policeY = HEIGHT+20;
    public int policeX;

    private Game game;
    private MainThread thread;
    private PoliceThread policeThread;
    private Background bg;
    //private Traffic traffic1,traffic2,traffic3,traffic4,traffic5;
    private Traffic traffic1A, traffic1B, traffic1C, traffic1D, traffic2A, traffic2B,traffic2C,traffic2D;
    private Player player;

    Random random = new Random();

    Bitmap trafficIm1 = BitmapFactory.decodeResource(getResources(),R.drawable.trafficu1);
    Bitmap trafficIm2 = BitmapFactory.decodeResource(getResources(),R.drawable.trafficu2);
    Bitmap trafficIm3 = BitmapFactory.decodeResource(getResources(),R.drawable.trafficu3);
    Bitmap trafficIm4 = BitmapFactory.decodeResource(getResources(),R.drawable.trafficu4);
    Bitmap trafficIm5 = BitmapFactory.decodeResource(getResources(),R.drawable.trafficu5);
    Bitmap policeIm = BitmapFactory.decodeResource(getResources(),R.drawable.police);
    //int[][] trafficPos = new int[5][5];

    private Rect cspeedup = new Rect();
    private Rect cspeeddown = new Rect();
    private double lastSpeedIncrease;

   public static double yRotation;

   public static void setyRotation(double value) {
       yRotation = value;
   }


    public GamePanel(Context context,Game game)
    {
        super(context);
        this.game = game;
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        policeThread = new PoliceThread(getHolder(),this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry)
        {
            try{thread.setRunning(false);
                thread.join();

            }catch(InterruptedException e){e.printStackTrace();}
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Bitmap bgDefault = BitmapFactory.decodeResource(getResources(),R.drawable.gamebgcollision);
        Bitmap bgCollided = BitmapFactory.decodeResource(getResources(),R.drawable.gamebg);
        Bitmap carL = BitmapFactory.decodeResource(getResources(),R.drawable.playerleft);
        Bitmap carR = BitmapFactory.decodeResource(getResources(),R.drawable.playerright);
        Bitmap carS = BitmapFactory.decodeResource(getResources(),R.drawable.playerstraight);
        WIDTH=bgDefault.getWidth();
        HEIGHT=bgDefault.getHeight();
        lastSpeedIncrease = System.currentTimeMillis();
        bg = new Background(bgDefault,bgCollided);
        switch(gamemode){
            case 1:
                player = new Player(carS,carL,carR,WIDTH /5,HEIGHT /7,60,0.01);
                player.setAccelerate(true);
                break;
            case 2:
                player = new Player(carS,carL,carR,WIDTH /5,HEIGHT /7,60,1);
                player.setAccelerate(true);
                break;
            case 3:
                player = new Player(carS,carL,carR,WIDTH /5,HEIGHT /7,100,1);
                break;
        }
        //traffic1 = new Traffic(35 ,1,-HEIGHT/4,TrafficImage(random.nextInt(5)));
        //traffic2 = new Traffic(35,2,-HEIGHT/2,TrafficImage(random.nextInt(5)));
        //traffic3 = new Traffic(35,3,-3*HEIGHT/4,TrafficImage(random.nextInt(5)));
        //traffic4 = new Traffic(35,1,0,TrafficImage(random.nextInt(5)));
        //traffic5 = new Traffic(35 ,2,HEIGHT/4,TrafficImage(random.nextInt(5)));;
        traffic1A = new Traffic(45, 1, 0, TrafficImage(random.nextInt(5)));
        traffic1B = new Traffic(45, 1, HEIGHT/4, TrafficImage(random.nextInt(5)));
        traffic1C = new Traffic(45, 2, HEIGHT/4, TrafficImage(random.nextInt(5)));
        traffic1D = new Traffic(45, 1, HEIGHT/2, TrafficImage(random.nextInt(5)));
        traffic2A = new Traffic(45, 1, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
        traffic2B = new Traffic(45, 2, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
        traffic2C = new Traffic(45, 2, -HEIGHT/2, TrafficImage(random.nextInt(5)));
        traffic2D = new Traffic(45, 3, -HEIGHT/2, TrafficImage(random.nextInt(5)));
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(playing) {
            int xLocation = (int) event.getX();
            int yLocation = (int) event.getY();
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(xLocation > (getWidth() / 2)&&yLocation>(getHeight()/2)){
                        if(gamemode==3){
                            player.setAccelerate(true);
                        }
                        else{
                            player.setTurnright(true);
                            player.setTurnleft(false);
                        }
                    }
                    if(xLocation < (getWidth() / 2)&&yLocation>(getHeight()/2)){
                        if(gamemode==3){
                            player.setDecelerate(true);
                        }
                        else{
                            player.setTurnright(false);
                            player.setTurnleft(true);
                        }
                    }
                    if (cspeedup.contains(xLocation,yLocation)&&player.getMaxSpeed()<100 && gamemode==2){
                            player.setMaxSpeed(player.getMaxSpeed()+2.5);
                    }
                    if (cspeeddown.contains(xLocation,yLocation)&&player.getMaxSpeed()>0 && gamemode==2){
                            player.setMaxSpeed(player.getMaxSpeed()-2.5);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(xLocation > (getWidth() / 2)&&yLocation>(getHeight()/2)){
                        if(gamemode==3){
                            player.setAccelerate(false);
                        }
                        else{
                            player.setTurnright(false);
                        }
                    }
                    if(xLocation < (getWidth() / 2)&&yLocation>(getHeight()/2)){
                        if(gamemode==3){
                            player.setDecelerate(false);
                        }
                        else{
                            player.setTurnleft(false);
                        }
                    }
                    break;
                }

            }
        else{
            playing = true;
            player.setDy(50);
            player.setDxa(0);
            player.setLastspeedupdate(System.nanoTime());
            onTouchEvent(event);}
        return true;
    }

    public void update() {

        //Check Health
        if (player.getHealth() == 0) {
            playing = false;
            gameOver = true;
            player.setAccelerate(false);
            player.setMaxSpeed(0);
            player.setDx(0);
            try {
                Thread.sleep(4000);
            } catch (Exception e) {}
            thread.setRunning(false);
            distance = player.getDistance();
            game.openGameOver();
        }
        //Check if caught by enemy
        if (player.getDistance() - player.getEnemydistance() < 0) {
            thread.setRunning(false);
            player.setAccelerate(false);
            player.setMaxSpeed(0);
            playing = false;
            gameOver = true;
            policeY = HEIGHT;
            setPolicePos();
            policeThread.setRunning(true);
            policeThread.start();
            distance = player.getDistance();
        }
        if (!gameOver && playing) {
            //Update enemy speed and player speed if necessary
            if ((System.currentTimeMillis() - lastSpeedIncrease) > 10000) {
                if (gamemode == 1) {
                    player.setMaxSpeed(player.getMaxSpeed() + 2);
                }
                lastSpeedIncrease = System.currentTimeMillis();
            }
            MOVESPEED = (int) player.getDy();
            bg.setVector((int) player.getDy());
            bg.update();
            if (gamemode == 3) {
                if (yRotation < -0.08) {
                    player.setTurnleft(true);
                    player.setTurnright(false);
                } else {
                    player.setTurnleft(false);
                    if (yRotation > 0.08) {
                        player.setTurnright(true);
                    } else {
                        player.setTurnright(false);
                    }
                }
            }
            player.update();
            playerLeft = player.rectangle.left;
            playerRight = player.rectangle.right;
            traffic1A.update();
            traffic1B.update();
            traffic1C.update();
            traffic1D.update();
            traffic2A.update();
            traffic2B.update();
            traffic2C.update();
            traffic2D.update();
            //If any traffic goes off screen set speed to 0 so it doesn't return
            if (traffic1A.getY() > HEIGHT) {
                traffic1A.setDy(0);
            }
            if (traffic1B.getY() > HEIGHT) {
                traffic1B.setDy(0);
            }
            if (traffic1C.getY() > HEIGHT) {
                traffic1C.setDy(0);
            }
            if (traffic1D.getY() > HEIGHT) {
                traffic1D.setDy(0);
            }
            if (traffic2A.getY() > HEIGHT) {
                traffic2A.setDy(0);
            }
            if (traffic2B.getY() > HEIGHT) {
                traffic2B.setDy(0);
            }
            if (traffic2C.getY() > HEIGHT) {
                traffic2C.setDy(0);
            }
            if (traffic2D.getY() > HEIGHT) {
                traffic2D.setDy(0);
            }
            //Check if entire traffic group off screen
            if (traffic1A.getY() > HEIGHT && traffic1B.getY() > HEIGHT && traffic1C.getY() > HEIGHT && traffic1D.getY() > HEIGHT && traffic2A.getY() > 0) {
                newTrafficGroup1(random.nextInt(5));
            }
            if (traffic2A.getY() > HEIGHT && traffic2B.getY() > HEIGHT && traffic2C.getY() > HEIGHT && traffic2D.getY() > HEIGHT && traffic1A.getY() > 0) {
                newTrafficGroup2(random.nextInt(5));
            }
            //Check if player has come to rest after colliding
            if (player.isInCollision() && player.getDy() < 0) {
                player.setDy(0);
                player.setInCollision(false);
                traffic1A.setInCollision(false);
                traffic1B.setInCollision(false);
                traffic1C.setInCollision(false);
                traffic1D.setInCollision(false);
                traffic2A.setInCollision(false);
                traffic2B.setInCollision(false);
                traffic2C.setInCollision(false);
                traffic2D.setInCollision(false);
                //traffic1.setInCollision(false);
                //traffic2.setInCollision(false);
                //traffic3.setInCollision(false);
                //traffic4.setInCollision(false);
                //traffic5.setInCollision(false);
            }
            //Check if traffic is close to colliding and match speeds if so
            trafficObjectCollisions(traffic1A,traffic2A);
            trafficObjectCollisions(traffic1A,traffic2B);
            trafficObjectCollisions(traffic1A,traffic2C);
            trafficObjectCollisions(traffic1A,traffic2D);
            trafficObjectCollisions(traffic1B,traffic2A);
            trafficObjectCollisions(traffic1B,traffic2B);
            trafficObjectCollisions(traffic1B,traffic2C);
            trafficObjectCollisions(traffic1B,traffic2D);
            trafficObjectCollisions(traffic1C,traffic2A);
            trafficObjectCollisions(traffic1C,traffic2B);
            trafficObjectCollisions(traffic1C,traffic2C);
            trafficObjectCollisions(traffic1C,traffic2D);
            trafficObjectCollisions(traffic1D,traffic2A);
            trafficObjectCollisions(traffic1D,traffic2B);
            trafficObjectCollisions(traffic1D,traffic2C);
            trafficObjectCollisions(traffic1D,traffic2D);
            //Check if traffic collided on side
            trafficObjectSideCollisions(traffic1A,traffic1B);
            trafficObjectSideCollisions(traffic1A,traffic1C);
            trafficObjectSideCollisions(traffic1A,traffic1D);
            trafficObjectSideCollisions(traffic1B,traffic1C);
            trafficObjectSideCollisions(traffic1B,traffic1D);
            trafficObjectSideCollisions(traffic1C,traffic1D);
            trafficObjectSideCollisions(traffic2A,traffic2B);
            trafficObjectSideCollisions(traffic2A,traffic2C);
            trafficObjectSideCollisions(traffic2A,traffic2D);
            trafficObjectSideCollisions(traffic2B,traffic2C);
            trafficObjectSideCollisions(traffic2B,traffic2D);
            trafficObjectSideCollisions(traffic2C,traffic2D);
            //Check collisions
            gameObjectCollisions(traffic1A);
            gameObjectCollisions(traffic1B);
            gameObjectCollisions(traffic1C);
            gameObjectCollisions(traffic1D);
            gameObjectCollisions(traffic2A);
            gameObjectCollisions(traffic2B);
            gameObjectCollisions(traffic2C);
            gameObjectCollisions(traffic2D);
        }
    }

    public void gameObjectCollisions(Traffic trafficObj){
        if(Rect.intersects(player.getRectangle(),trafficObj.getRectangle())&&!player.isInCollision()&&!trafficObj.isInSideCollision()){
            if(player.getRectangle().top>trafficObj.getRectangle().bottom - getHeight()/50) {
                player.setInCollision(true);
                trafficObj.setInCollision(true);
                player.reducehealth();
            }else{
                trafficObj.setInSideCollision(true);
                if (player.getRectangle().right<trafficObj.getRectangle().right){
                    trafficObj.setDx(20);
                }else {trafficObj.setDx(-5);}
                player.setDy(player.getDy()*0.9);
                player.setDx(-0.75*player.getDx());
                player.reducehealthfromside();
                lastCollision = System.currentTimeMillis();
            }
        }
    }
    public void trafficObjectCollisions(Traffic A, Traffic B){
        if(A.getLane()==B.getLane()) {
            if (Math.abs(A.rectangle.bottom - B.rectangle.top) < HEIGHT / 30) {
                if (!(B.getDy() == 0)) {
                    B.setDy(A.getDy());
                }
            } else if (Math.abs(B.rectangle.bottom - A.rectangle.top) < HEIGHT / 30) {
                if (!(A.getDy() == 0)) {
                    A.setDy(B.getDy());
                }
            }
        }
    }
    public void trafficObjectSideCollisions(Traffic A,Traffic B){
        if (A.isInSideCollision() == true){
            if(A.rectangle.intersect(B.rectangle)) {
                if (A.getDx() > 0) {
                    B.setX(A.rectangle.right);
                }else{B.setX(A.rectangle.left - trafficIm1.getWidth());}
                B.setDy(20);
            }
        } else if(B.isInSideCollision()==true){
            if(B.rectangle.intersect(A.rectangle)) {
                if (B.getDx() > 0) {
                    A.setX(B.rectangle.right);
                }else{A.setX(B.rectangle.left - trafficIm1.getWidth());}
                A.setDy(20);
            }
        }
    }

    public Bitmap TrafficImage(int num){
        switch(num){
            case 0:
                return trafficIm1;
            case 1:
                return trafficIm2;
            case 2:
                return trafficIm3;
            case 3:
                return trafficIm4;
            case 4:
                return trafficIm5;
        }
        return trafficIm1;
    }
    public void newTrafficGroup1(int layout){
        switch(layout){
            case 0:
                traffic1A = new Traffic(45- random.nextInt(2), 1, -HEIGHT*48/40, TrafficImage(random.nextInt(5)));
                traffic1B = new Traffic(45, 2, -HEIGHT*5/4,  TrafficImage(random.nextInt(5)));
                traffic1C = new Traffic(45, 2, -HEIGHT/2,  TrafficImage(random.nextInt(5)));
                traffic1D = new Traffic(46, 3, -HEIGHT/2,  TrafficImage(random.nextInt(5)));
                break;
            case 1:
                traffic1A = new Traffic(45 - random.nextInt(2), 1, -HEIGHT*48/40, TrafficImage(random.nextInt(5)));
                traffic1B = new Traffic(45, 3, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
                traffic1C = new Traffic(45, 2, -HEIGHT*3/4, TrafficImage(random.nextInt(5)));
                traffic1D = new Traffic(45, 2, -HEIGHT/2, TrafficImage(random.nextInt(5)));
                break;
            case 2:
                traffic1A = new Traffic(45, 1, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
                traffic1B = new Traffic(45 - random.nextInt(2), 2, -HEIGHT*49/40, TrafficImage(random.nextInt(5)));
                traffic1C = new Traffic(45, 1, -HEIGHT, TrafficImage(random.nextInt(5)));
                traffic1D = new Traffic(45, 3, -HEIGHT*3/4 ,TrafficImage(random.nextInt(5)));
                break;
            case 3:
                traffic1A = new Traffic(45, 3, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
                traffic1B = new Traffic(45 + random.nextInt(2), 1, -HEIGHT*32/40, TrafficImage(random.nextInt(5)));
                traffic1C = new Traffic(45, 2, -HEIGHT*3/4, TrafficImage(random.nextInt(5)));
                traffic1D = new Traffic(45, 1, -HEIGHT/2, TrafficImage(random.nextInt(5)));
                break;
            case 4:
                traffic1A = new Traffic(45, 2, -HEIGHT, TrafficImage(random.nextInt(5)));
                traffic1B = new Traffic(46 + random.nextInt(2), 3, -HEIGHT*34/40, TrafficImage(random.nextInt(5)));
                traffic1C = new Traffic(45, 1, -HEIGHT/2, TrafficImage(random.nextInt(5)));
                traffic1D = new Traffic(45+ random.nextInt(2), 3, -HEIGHT*9/20, TrafficImage(random.nextInt(5)));
                break;
        }
    }
    public void newTrafficGroup2(int layout){
        switch(layout){
            case 0:
                traffic2A = new Traffic(45- random.nextInt(2), 1, -HEIGHT*48/40, TrafficImage(random.nextInt(5)));
                traffic2B = new Traffic(45, 2, -HEIGHT*5/4,  TrafficImage(random.nextInt(5)));
                traffic2C = new Traffic(45, 2, -HEIGHT/2,  TrafficImage(random.nextInt(5)));
                traffic2D = new Traffic(46, 3, -HEIGHT/2,  TrafficImage(random.nextInt(5)));
                break;
            case 1:
                traffic2A = new Traffic(45 - random.nextInt(2), 1, -HEIGHT*48/40, TrafficImage(random.nextInt(5)));
                traffic2B = new Traffic(45, 3, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
                traffic2C = new Traffic(45, 2, -HEIGHT*3/4, TrafficImage(random.nextInt(5)));
                traffic2D = new Traffic(45, 2, -HEIGHT/2, TrafficImage(random.nextInt(5)));
                break;
            case 2:
                traffic2A = new Traffic(45, 1, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
                traffic2B = new Traffic(45 - random.nextInt(2), 2, -HEIGHT*49/40, TrafficImage(random.nextInt(5)));
                traffic2C = new Traffic(45, 1, -HEIGHT, TrafficImage(random.nextInt(5)));
                traffic2D = new Traffic(45, 3, -HEIGHT*3/4 ,TrafficImage(random.nextInt(5)));
                break;
            case 3:
                traffic2A = new Traffic(45, 3, -HEIGHT*5/4, TrafficImage(random.nextInt(5)));
                traffic2B = new Traffic(45 + random.nextInt(2), 1, -HEIGHT*32/40, TrafficImage(random.nextInt(5)));
                traffic2C = new Traffic(45, 2, -HEIGHT*3/4, TrafficImage(random.nextInt(5)));
                traffic2D = new Traffic(45, 1, -HEIGHT/2, TrafficImage(random.nextInt(5)));
                break;
            case 4:
                traffic2A = new Traffic(45, 2, -HEIGHT, TrafficImage(random.nextInt(5)));
                traffic2B = new Traffic(46 + random.nextInt(2), 3, -HEIGHT*34/40, TrafficImage(random.nextInt(5)));
                traffic2C = new Traffic(45, 1, -HEIGHT/2, TrafficImage(random.nextInt(5)));
                traffic2D = new Traffic(45+ random.nextInt(2), 3, -HEIGHT*9/20, TrafficImage(random.nextInt(5)));
                break;
        }
    }

    public void movePolice(){
        policeY -= 15;
        if (policeY < HEIGHT - policeIm.getHeight()*1.25){
            policeThread.setRunning(false);
            game.openGameOver();
        }
    }
    public void setPolicePos(){
        boolean valueFound = false;
        Rect testRect = new Rect();
        testRect.set(player.getX()+policeIm.getWidth(),(int)(HEIGHT-policeIm.getHeight()*1.25),player.getX()+policeIm.getWidth()*2,HEIGHT);
        if (!testRectIntersectsTraffic(testRect)){
            policeX = testRect.left;
            valueFound = true;
        }
        if(!valueFound) {
            testRect.left = player.getX() - policeIm.getWidth();
            testRect.right = player.getX();
            if (!testRectIntersectsTraffic(testRect)){
                policeX = testRect.left;
                valueFound = true;
            }
        }
        if(!valueFound&&player.getX()>WIDTH/2){
            policeX = (int) (WIDTH/4.8 -policeIm.getWidth());
            valueFound = true;
        }
        if(!valueFound){policeX = (int) (WIDTH/1.23);}
    }
    public boolean testRectIntersectsTraffic(Rect testRect){
        if(testRect.intersect(traffic1A.rectangle)){return true;}
        if(testRect.intersect(traffic1B.rectangle)){return true;}
        if(testRect.intersect(traffic1C.rectangle)){return true;}
        if(testRect.intersect(traffic1D.rectangle)){return true;}
        if(testRect.intersect(traffic2A.rectangle)){return true;}
        if(testRect.intersect(traffic2B.rectangle)){return true;}
        if(testRect.intersect(traffic2C.rectangle)){return true;}
        if(testRect.intersect(traffic2D.rectangle)){return true;}
        if(testRect.left<0){return true;}
        if(testRect.right>WIDTH - policeIm.getWidth()){return true;}
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = (float) getWidth() / WIDTH;
        final float scaleFactorY = (float) getHeight() / HEIGHT;
        final int savedState = canvas.save();
        canvas.scale(scaleFactorX, scaleFactorY);
        if(canvas != null) {
            super.draw(canvas);
            bg.draw(canvas);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(50);
            DecimalFormat df1 = new DecimalFormat("#.#");
            DecimalFormat df2 = new DecimalFormat("#.###");
            player.draw(canvas);
            canvas.drawText("Speed", 3, 50, paint);
            canvas.drawText(df1.format(player.getDy()) + "mph", 3, 100, paint);
            canvas.drawText("Distance", 3, 170, paint);
            canvas.drawText(df2.format(player.getDistance()) + "mi", 3, 220, paint);
            //canvas.drawText(df2.format(yRotation),3,550,paint);
                //canvas.drawText(df1.format(player.getDxa()),3,600,paint);
                //canvas.drawText(df1.format(player.getDx()),3,650,paint);
                //canvas.drawText(df1.format(player.getX()),3,700,paint);
            traffic1A.draw(canvas);
            traffic1B.draw(canvas);
            traffic1C.draw(canvas);
            traffic1D.draw(canvas);
            traffic2A.draw(canvas);
            traffic2B.draw(canvas);
            traffic2C.draw(canvas);
            traffic2D.draw(canvas);
            //traffic1.draw(canvas);
                //traffic2.draw(canvas);
                //traffic3.draw(canvas);
                //traffic4.draw(canvas);
                //traffic5.draw(canvas);
            canvas.drawText("Lead", 3, 300, paint);
            canvas.drawText(df2.format(player.getDistance() - player.getEnemydistance()), 3, 350, paint);
            if (gamemode == 2) {
                Bitmap uparrow = BitmapFactory.decodeResource(getResources(), R.drawable.uparrow);
                canvas.drawBitmap(uparrow, WIDTH * 5 / 6, 50, null);
                canvas.drawText("C.Speed", WIDTH * 5 / 6, 110 + uparrow.getHeight(), paint);
                canvas.drawText(Double.toString(player.getMaxSpeed()) + "mph", WIDTH * 5 / 6, 160 + uparrow.getHeight(), paint);
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.downarrow), WIDTH * 5 / 6, 170 + uparrow.getHeight(), null);
                cspeedup.set((int) ((WIDTH * 5 / 6) * scaleFactorX), (int) (50 * scaleFactorY), (int) ((WIDTH * 5 / 6 + uparrow.getWidth()) * scaleFactorX), (int) ((50 + uparrow.getHeight()) * scaleFactorY));
                cspeeddown.set((int) ((WIDTH * 5 / 6) * scaleFactorX), (int) ((170 + uparrow.getHeight()) * scaleFactorY), (int) ((WIDTH * 5 / 6 + uparrow.getWidth()) * scaleFactorX), (int) ((170 + 2 * uparrow.getHeight()) * scaleFactorY));
            }
            canvas.drawText("Health", WIDTH * 5 / 6, 500, paint);
            if (player.getHealth() < 40) {
                    paint.setColor(Color.RED);
                }
            double healthBarTop = 510 + ((double) HEIGHT / 4) * (1 - (double) player.getHealth() / 100);
            canvas.drawRect(WIDTH * 21 / 24, (int) healthBarTop, WIDTH * 23 / 24, 510 + HEIGHT / 4, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            canvas.drawRect(WIDTH * 21 / 24, 510, WIDTH * 23 / 24, 510 + HEIGHT / 4, paint);
            if(gameOver) {
                canvas.drawBitmap(policeIm, policeX, policeY, null);
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gameoverpopup), 0, getHeight() / 5, null);
            }

        }
        canvas.restoreToCount(savedState);
    }
}
