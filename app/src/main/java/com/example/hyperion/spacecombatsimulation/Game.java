package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Game extends Activity implements SensorEventListener {

    private com.example.hyperion.spacecombatsimulation.GameView gameView;
    private com.example.hyperion.spacecombatsimulation.Joystick joystick;
    private Space.Sector sector;
    private GameThread thread;
    private List<Ship> ships = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private float camX, camY, joyX, joyY, yaw;
    private SensorManager sm;
    private TextView textVelocity;
    private boolean vertical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        SharedPreferences mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        vertical = mSettings.getBoolean("Vertical", false);
        if (!vertical)
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sector = StartGame.selectedMap;
        ships.add(new Ship(StartGame.selectedShip, 0, 0, 0));
        ships.add(new Ship(Ship.ShipClass.Shuttle, 100, 100, 10));
        ships.add(new Ship(Ship.ShipClass.Shuttle, -250, -20, 48));
        ships.add(new Ship(Ship.ShipClass.Starfighter, 200, 300, 190));
        //projectiles.add(new Projectile(Projectile.ProjectileType.Type1, 0, -100, 190));
        camX = ships.get(1).getX();
        camY = ships.get(1).getY();

        ImageView background = findViewById(R.id.background);
        background.setImageResource(sector.background);

        textVelocity = findViewById(R.id.textVelocity);

        sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);

        gameView = findViewById(R.id.gameView);
        joystick = findViewById(R.id.joystick);

        thread = new GameThread(20, this);
        thread.setRunning(true);
        thread.start();
        Log.d("Thread game", "Started");

        // Set velocity
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable(){
            public void run() {
                textVelocity.setText(String.format(Locale.getDefault(), "%.2f m/s", ships.get(0).getVelocity()));
                mainHandler.postDelayed(this, 50);
            }
        });
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

        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            try {
                Projectile projectile = projectileIterator.next();
                projectile.move();

                boolean hit = false;
                for (Ship ship : ships) {
                    if (projectile.inBounds(ship) && ship != ships.get(0)) {
                        ship.hit(projectile);
                        projectileIterator.remove();
                        hit = true;
                        break;
                    }
                }
                if(hit) break;

                if (projectile.canRemove())
                    projectileIterator.remove();
            } catch (ConcurrentModificationException e) {
                Log.e("Game", "ConcurrentModificationException!");
            }
        }
    }

    public void button_click(View v) {

        switch (v.getId()) {

            case R.id.butFire:
                if (ships.get(0).fire()) {
                    projectiles.add(new Projectile(Projectile.ProjectileType.Type1, ships.get(0)));
                    gameView.addObject(projectiles.get(projectiles.size() - 1));
                }
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (!vertical) {
                yaw = event.values[1];

                // Limiter
                if (yaw < -5) yaw = -5;
                else if (yaw > 5) yaw = 5;

                // Deadzone
                if (yaw < 0.5 && yaw > -0.5) yaw = 0;

            } else {
                yaw = -event.values[0];

                // Limiter
                if (yaw < -5) yaw = -5;
                else if (yaw > 5) yaw = 5;

                // Deadzone
                if (yaw < 0.5 && yaw > -0.5) yaw = 0;
            }
            yaw = yaw / 5;
            joystick.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        super.onResume();
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
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

    public Ship getPlayerShip() {
        return ships.get(0);
    }
    public List<Ship> getShips() {
        return ships;
    }
    public List<Projectile> getProjectiles() {
        return projectiles;
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
    public float getYaw() {
        return yaw;
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
            sm.unregisterListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

