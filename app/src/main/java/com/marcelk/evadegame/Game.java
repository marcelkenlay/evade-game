package com.marcelk.evadegame;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

//public class Game extends Activity{
public class Game extends Activity implements SensorEventListener {
    Sensor gyroscope;
    SensorManager gm;
    public double yRotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gm=(SensorManager)getSystemService(SENSOR_SERVICE);
        gyroscope = gm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gm.registerListener(this,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);

        GamePanel.playing = false;
        GamePanel.gameOver = false;
        setContentView(new GamePanel(this,this));
    }

    @Override
    public void onSensorChanged(SensorEvent Event){
        GamePanel.setyRotation(Event.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}


    public void openGameOver(){
        startActivity(new Intent(Game.this, GameOver.class));
    }
}
