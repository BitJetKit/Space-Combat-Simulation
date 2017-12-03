package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class Game extends Activity {

    private Space.Sector selectedMap = StartGame.selectedMap;
    private Ship.ShipClass playerShip = StartGame.selectedShip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Log.d("selectedMap name", selectedMap.name);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Main.active = true;
            StartGame.selectedMap = null;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

