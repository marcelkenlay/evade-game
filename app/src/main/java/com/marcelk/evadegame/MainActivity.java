package com.marcelk.evadegame;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {


    public static double highScore = 0f;
    Button start1Btn, start2Btn, start3Btn;
    TextView gameInfoTxt;
    int dataBlock = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        start1Btn = (Button) findViewById(R.id.start1btn);
        start1Btn.setOnClickListener(this);
        start1Btn.setOnLongClickListener(this);
        start2Btn = (Button) findViewById(R.id.start2btn);
        start2Btn.setOnClickListener(myhandler);
        start2Btn.setOnLongClickListener(this);
        start3Btn = (Button) findViewById(R.id.start3btn);
        start3Btn.setOnClickListener(myhandler);
        start3Btn.setOnLongClickListener(this);
        gameInfoTxt = (TextView) findViewById(R.id.gameInfoText);
        gameInfoTxt.setMovementMethod(new ScrollingMovementMethod());

        String line = null;
        try {
            File path = getApplicationContext().getFilesDir();
            File file = new File(path, "highScore.txt");
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            line = bufferedReader.readLine();
            fileInputStream.close();
            bufferedReader.close();
            highScore = Double.parseDouble(line);
        }
        catch(FileNotFoundException ex) {
        }
        catch(IOException ex) {
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start1btn:
                GamePanel.gamemode = 1;
                break;
            case R.id.start2btn:
                GamePanel.gamemode = 2;
                break;
            case R.id.start3btn:
                GamePanel.gamemode = 3;
                break;
        }
        startActivity(new Intent(MainActivity.this, Game.class));
    }

    @Override
    public boolean onLongClick(View v) {
        CharSequence instructions ="";
        CharSequence info ="";
        CharSequence tips ="Bully cars, colliding from side will make them brake and allow you to avoid fatal collisions.\nDriving on the grass will slow you down but allows you to avoid colliding.";
        switch (v.getId()) {
            case R.id.start1btn:
                info = "Just Metres Ahead\nOnly metres ahead so any head on crash will mean the end.";
                instructions = "Touch left half of screen to turn left\nTouch right half to turn right.";
                gameInfoTxt.setBackgroundColor(Color.parseColor("#696868"));
                gameInfoTxt.setText(info+"\n\nInstructions:\n"+instructions+"\n\nTips:\n"+tips);
                break;
            case R.id.start2btn:
                info = "Pick Your Pace: A mile head start, choose what speed to travel at, be cautious as police speed increases.";
                instructions = "Touch left half of screen to turn left\nTouch right half to turn right.\nArrows in top right of screen to alter your speed.";
                gameInfoTxt.setBackgroundColor(Color.parseColor("#232323"));
                gameInfoTxt.setText(info+"\n\nInstructions:\n"+instructions+"\n\nTips:\n"+tips);
                break;
            case R.id.start3btn:
                info = "Full Control: A mile head start, you accelerate, you decelerate, you turn.";
                instructions = "Touch left half of screen to accelerate\nTouch right half to decelerate.\nTilt phone left or right to turn in that direction.";
                gameInfoTxt.setBackgroundColor(Color.parseColor("#000000"));
                gameInfoTxt.setText(info+"\n\nInstructions:\n"+instructions+"\n\nTips:\n"+tips);
                break;
        }
        return true;
    }


    View.OnClickListener myhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start1btn:
                    GamePanel.gamemode = 1;
                    break;
                case R.id.start2btn:
                    GamePanel.gamemode = 2;
                    break;
                case R.id.start3btn:
                    GamePanel.gamemode = 3;
                    break;
            }
            startActivity(new Intent(MainActivity.this, Game.class));
        }
    };
}