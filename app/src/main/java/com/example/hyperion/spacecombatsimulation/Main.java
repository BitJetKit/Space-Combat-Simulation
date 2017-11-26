package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

    private Button butSP, butMP, butSettings, butExit;
    private TextView title;

    static MediaPlayer mediaPlayer;
    static int height;
    static boolean started = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("POWER", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        butSP = findViewById(R.id.butSP);
        butMP = findViewById(R.id.butMP);
        butSettings = findViewById(R.id.butSettings);
        butExit = findViewById(R.id.butExit);
        title = findViewById(R.id.title);

        SharedPreferences mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        if (!started) {
            int resID = getResources().getIdentifier("menu", "raw", getPackageName());
            int musicVolume = mSettings.getInt("Music volume", 50);
            mediaPlayer = MediaPlayer.create(this, resID);
            mediaPlayer.setLooping(true);

            if (mSettings.getBoolean("Music", true)) {
                mediaPlayer.setVolume((float) musicVolume / 100, (float) musicVolume / 100);
                mediaPlayer.start();
            } else {
                mediaPlayer.start();
                mediaPlayer.pause();
            }
        }

        started = true;
    }

    protected void onPause() {
        Log.d("POWER","Pause");
        super.onPause();
    }

    protected void onStart() {
        super.onStart();
        Log.d("POWER","Start");
    }

    protected void onResume() {
        super.onResume();
        Log.d("POWER","Resume");

        if (Settings.active) {
            butSP.setAlpha(0); butMP.setAlpha(0); butSettings.setAlpha(0); butExit.setAlpha(0);
            title.setY(-height / 3);
            Log.d("Height",""+height);
            Log.d("POWER","Resume");
        } else {
            butSP.animate().alpha(1).setDuration(200).start();
            butMP.animate().alpha(1).setDuration(400).start();
            butSettings.animate().alpha(1).setDuration(600).start();
            butExit.animate().alpha(1).setDuration(800).start();
            title.animate().translationY(0).setDuration(600).start();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("POWER","Destroy");
    }

    public void button_click(View v) {

        switch (v.getId()) {

            case R.id.butSP:

                break;

            case R.id.butMP:

                break;

            case R.id.butSettings:
                startActivity(new Intent(this, Settings.class));
                butSP.animate().alpha(0).setDuration(200).start();
                butMP.animate().alpha(0).setDuration(400).start();
                butSettings.animate().alpha(0).setDuration(600).start();
                butExit.animate().alpha(0).setDuration(800).start();
                title.animate().translationY(-butSP.getY() / 1.2f).setDuration(600).start();
                break;

            case R.id.butExit:
                finish();
                System.exit(0);
                break;
        }
    }
}
