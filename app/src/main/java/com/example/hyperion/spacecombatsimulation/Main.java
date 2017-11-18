package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Main extends Activity /*implements View.OnClickListener*/ {

    private Button butSP, butMP, butSettings, butExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("POWER", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        butSP = (Button)findViewById(R.id.butSP);
        butMP = (Button)findViewById(R.id.butMP);
        butSettings = (Button)findViewById(R.id.butSettings);
        butExit = (Button)findViewById(R.id.butExit);
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
        } else {
            butSP.animate().alpha(1).setDuration(200).start();
            butMP.animate().alpha(1).setDuration(400).start();
            butSettings.animate().alpha(1).setDuration(600).start();
            butExit.animate().alpha(1).setDuration(800).start();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("POWER","Destroy");
    }

    public void button_click(View v) {

        switch (v.getId()) {

            case R.id.butSettings:
                startActivity(new Intent(this, Settings.class));
                butSP.animate().alpha(0).setDuration(200).start();
                butMP.animate().alpha(0).setDuration(400).start();
                butSettings.animate().alpha(0).setDuration(600).start();
                butExit.animate().alpha(0).setDuration(800).start();
                break;

            case R.id.butExit:
                finish();
                System.exit(0);
                break;
        }
    }
}
