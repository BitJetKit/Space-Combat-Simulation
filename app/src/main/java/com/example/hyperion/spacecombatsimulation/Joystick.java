package com.example.hyperion.spacecombatsimulation;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private int radius, hatRadius, rimWidth;
    private float posX, posY, joyX, joyY;
    private Paint paint = new Paint();
    private Game context;


    public Joystick(Context context) { super(context); init(context); }
    public Joystick(Context context, AttributeSet attrs) { super(context, attrs); init(context); }
    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(context); }

    private void init(Context context) {
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.context = (Game) context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_UP) {
            posX = event.getX(); posY = event.getY();

            float displacement = (float) Math.sqrt((Math.pow(posX - radius, 2)) + Math.pow(posY - radius, 2));
            if (displacement >= radius - hatRadius - rimWidth) {
                float ratio = (radius - hatRadius - rimWidth) / displacement;
                posX = radius + (posX - radius) * ratio;
                posY = radius + (posY - radius) * ratio;
            }
            //Log.d("Joystick", "posX: " + Math.round(posX) + " posY: " + Math.round(posY));

        } else {
            //Log.d("Joystick", "Center");
            posX = radius; posY = radius;
        }

        joyX = (posX - radius) / (radius - hatRadius - rimWidth);
        joyY = (posY - radius) / (radius - hatRadius - rimWidth);

        float deadzone = (float) 0.1;
        if (joyX > 0 && joyX < deadzone || joyX < 0 && joyX > -deadzone) joyX = 0;
        if (joyY > 0 && joyY < deadzone || joyY < 0 && joyY > -deadzone) joyY = 0;

        //Log.d("Joystick", "joyX: " + joyX + " joyY: " + joyY);

        context.setJoyX(joyX); context.setJoyY(joyY);

        invalidate();

        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Log.d("Joystick", "Width: " + w);
        radius = w / 2;
        hatRadius = radius / 4;
        rimWidth = radius / 20;
        posX = radius; posY = radius;
        paint.setAntiAlias(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d("Joystick", "surfaceCreated");
        setZOrderOnTop(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onDraw(Canvas canvas) {

        if (getHolder().getSurface().isValid()) {

            // Base
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(80, 60, 60, 60));
            canvas.drawCircle(radius, radius, radius, paint);

            // Deadzone lines
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.DKGRAY);
            if (joyX == 0)
                canvas.drawLine(radius, rimWidth, radius, radius * 2 - rimWidth, paint);
            if (joyY == 0)
                canvas.drawLine(rimWidth, radius, radius * 2- rimWidth, radius, paint);

            // Rim
            paint.setStrokeWidth(rimWidth);
            if (android.os.Build.VERSION.SDK_INT >= 23)
                paint.setColor(getResources().getColor(R.color.colorAccent, null));
            else
                paint.setColor(getResources().getColor(R.color.colorAccent));
            canvas.drawCircle(radius, radius, (float)(radius * 0.975), paint);

            // Hat
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(10,0,0, Color.BLUE);
            canvas.drawCircle(posX, posY, hatRadius, paint);
        }
    }
}
