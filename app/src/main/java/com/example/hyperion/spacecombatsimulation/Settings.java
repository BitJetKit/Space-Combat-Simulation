package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.ToggleButton;

public class Settings extends Activity {

    private SharedPreferences mSettings;
    private SeekBar musicBar, soundBar;
    private Switch musicSwitch, soundSwitch;
    private CheckBox batteryCheck, clockCheck;
    private ToggleButton modeToggle;

    int musicVolume, soundVolume;
    public boolean music, sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        musicBar = findViewById(R.id.musicBar);
        soundBar = findViewById(R.id.soundBar);
        musicSwitch = findViewById(R.id.switchMusic);
        soundSwitch = findViewById(R.id.switchSounds);
        batteryCheck = findViewById(R.id.checkBattery);
        clockCheck = findViewById(R.id.checkClock);
        modeToggle = findViewById(R.id.toggleMode);

        musicBar.setProgress(mSettings.getInt("Music volume", 50));
        soundBar.setProgress(mSettings.getInt("Sound volume", 100));
        musicSwitch.setChecked(mSettings.getBoolean("Music", true));
        soundSwitch.setChecked(mSettings.getBoolean("Sounds", true));
        batteryCheck.setChecked(mSettings.getBoolean("Battery indicator", false));
        clockCheck.setChecked(mSettings.getBoolean("Clock indicator", false));
        modeToggle.setChecked(mSettings.getBoolean("Vertical", false));

        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                musicVolume = musicBar.getProgress();
                Main.mediaPlayer.setVolume((float) musicVolume / 100, (float) musicVolume / 100);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        soundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                soundVolume = soundBar.getProgress();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* Start playing random sounds */}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* Stop playing random sounds */ }
        });

        musicSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                music = musicSwitch.isChecked();
                if (!music)
                    Main.mediaPlayer.pause();
                else {
                    musicVolume = musicBar.getProgress();
                    Main.mediaPlayer.setVolume((float) musicVolume / 100, (float) musicVolume / 100);
                    Main.mediaPlayer.start(); Main.mediaPlayer.seekTo(0);
                }
            }
        });

        soundSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sound = soundSwitch.isChecked();
            }
        });
    }

    private void back() {

        musicVolume = mSettings.getInt("Music volume", 50);
        music = mSettings.getBoolean("Music", true);
        Main.mediaPlayer.setVolume((float) musicVolume / 100, (float) musicVolume / 100);
        if (music) {
            if (!Main.mediaPlayer.isPlaying()) {
                Main.mediaPlayer.start();
                Main.mediaPlayer.seekTo(0);
            }
        } else {
            Main.mediaPlayer.pause();
        }

        Main.active = true;
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("DEBUG","OnKeyDown");
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            back();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void button_click(View v) {

        switch (v.getId()) {

            case R.id.butBack:
                back();
                break;

            case R.id.butSave:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt("Music volume", musicBar.getProgress());
                editor.putInt("Sound volume", soundBar.getProgress());
                editor.putBoolean("Music", musicSwitch.isChecked());
                editor.putBoolean("Sounds", soundSwitch.isChecked());
                editor.putBoolean("Battery indicator", batteryCheck.isChecked());
                editor.putBoolean("Clock indicator", clockCheck.isChecked());
                editor.putBoolean("Vertical", modeToggle.isChecked());
                editor.apply();

                Main.active = true;
                finish();
                break;
        }
    }
}
