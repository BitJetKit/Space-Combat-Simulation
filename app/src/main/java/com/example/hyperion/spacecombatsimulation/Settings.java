package com.example.hyperion.spacecombatsimulation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends Activity {

    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        active = true;
    }

    public void button_click(View v) {
        switch (v.getId()) {

            case R.id.butBack:
                active = false;
                finish();
                break;
        }
    }
}
