package com.marcelk.evadegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class GameOver extends AppCompatActivity implements View.OnClickListener{

    Button replayBtn, menuBtn;
    TextView distanceTxt, bestDistanceTxt;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);

        replayBtn = (Button) findViewById(R.id.replaybtn);
        menuBtn = (Button) findViewById(R.id.menubtn);
        distanceTxt = (TextView) findViewById(R.id.distance);
        bestDistanceTxt = (TextView) findViewById(R.id.bestDistance);

        replayBtn.setOnClickListener(this);
        menuBtn.setOnClickListener(this);

        if (GamePanel.distance > MainActivity.highScore){
            MainActivity.highScore = GamePanel.distance;
            try {
                MainActivity.highScore = GamePanel.distance;
                File path = getApplicationContext().getFilesDir();
                File file = new File(path, "highScore.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream stream = new FileOutputStream(file);
                try {
                    stream.write(Double.toString(MainActivity.highScore).getBytes());
                } finally {
                    stream.close();
                }
            }catch (IOException e){

            }

        }

        DecimalFormat df1 = new DecimalFormat("#.##");
        distanceTxt.setText("Distance: "+df1.format(GamePanel.distance)+"mi");
        bestDistanceTxt.setText("Best Distance: "+df1.format(MainActivity.highScore)+"mi");

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        showInterstitial();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.replaybtn:
                startActivity(new Intent(GameOver.this, Game.class));
                break;
            case R.id.menubtn:
                startActivity(new Intent(GameOver.this, MainActivity.class));
                break;
        }
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        System.out.println("Tried to load ad");
        
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
        System.out.println("Advert Loaded");
    }

}
