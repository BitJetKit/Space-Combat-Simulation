package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.CursorJoiner;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Game extends Activity implements SensorEventListener {

    private Space.Sector sector;
    private GameThread thread;
    private List<Ship> ships = new ArrayList<>();
    private float camX, camY, joyX, joyY, yaw;
    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        SharedPreferences mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean vertical = mSettings.getBoolean("Vertical", false);
        if (!vertical)
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sector = StartGame.selectedMap;
        ships.add(new Ship(StartGame.selectedShip, 0, 0, 0));
        //ships.add(new Ship(Ship.ShipClass.Shuttle, 1000, 1000, 10));
        //ships.add(new Ship(Ship.ShipClass.Starfighter, 500, -1500, 48));
        //ships.add(new Ship(Ship.ShipClass.Shuttle, 200, 800, 190));
        camX = ships.get(0).getX();
        camY = ships.get(0).getY();

        ImageView background = findViewById(R.id.background);
        background.setImageResource(sector.background);

        sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

        thread = new GameThread(20, this);
        thread.setRunning(true);
        thread.start();
        Log.d("Thread game", "Started");
    }

    public void update() {
        boolean playerShip = true;
        for (Ship ship : ships) {
            if (playerShip) {
                ship.move(joyY, joyX, yaw);
                camX = ship.getX();
                camY = ship.getY();
                playerShip = false;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            yaw = event.values[1];

            // Limiter
            if (yaw < -5) yaw = -5; else if (yaw > 5) yaw = 5;
            yaw = yaw / 5;

            // Deadzone
            if (yaw < 0.1 && yaw > -0.1) yaw = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        super.onResume();
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    public List<Ship> getShips() {
        return ships;
    }
    public Space.Sector getSector() {
        return sector;
    }
    public float getCamX() {
        return camX;
    }
    public float getCamY() {
        return camY;
    }
    public float getJoyX() {
        return joyX;
    }
    public float getJoyY() {
        return joyY;
    }

    public void setCamX(float camX) {
        this.camX = camX;
    }
    public void setCamY(float camY) {
        this.camY = camY;
    }
    public void setJoyX(float joyX) {
        this.joyX = joyX;
    }
    public void setJoyY(float joyY) {
        this.joyY = joyY;
    }

    public void moveCamX(float changeX) {
        this.camX += changeX;
    }
    public void moveCamY(float changeY) {
        this.camY += changeY;
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            thread.setRunning(false);
            thread.join();
            Log.d("Thread game", "Joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Main.active = true;
            StartGame.selectedMap = null;
            StartGame.selectedShip = null;
            StartGame.ships.clear();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}

