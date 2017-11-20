package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity /*implements View.OnClickListener*/ {

    private Button butSP, butMP, butSettings, butExit;
    private TextView title;
    static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("POWER", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        butSP = (Button)findViewById(R.id.butSP);
        butMP = (Button)findViewById(R.id.butMP);
        butSettings = (Button)findViewById(R.id.butSettings);
        butExit = (Button)findViewById(R.id.butExit);
        title = (TextView)findViewById(R.id.title);

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
