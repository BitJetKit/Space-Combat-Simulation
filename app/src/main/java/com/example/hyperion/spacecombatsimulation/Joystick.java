package com.example.hyperion.spacecombatsimulation;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private int radius, hatRadius, rimWidth, centerY;
    private float posX, posY, joyX, joyY;
    private Paint paint = new Paint();
    private int colorAccent;
    private Game context;
    private Vibrator vibrator;
    private boolean centered = true;


    public Joystick(Context context) { super(context); init(context); }
    public Joystick(Context context, AttributeSet attrs) { super(context, attrs); init(context); }
    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(context); }

    private void init(Context context) {
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.context = (Game) context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_UP) {
            posX = event.getX(); posY = event.getY();

            float displacement = (float) Math.sqrt((Math.pow(posX - radius, 2)) + Math.pow(posY - centerY, 2));
            if (displacement >= radius - hatRadius - rimWidth) {
                float ratio = (radius - hatRadius - rimWidth) / displacement;
                posX = radius + (posX - radius) * ratio;
                posY = centerY + (posY - centerY) * ratio;
            }
            //Log.d("Joystick", "posX: " + Math.round(posX) + " posY: " + Math.round(posY));

        } else {
            //Log.d("Joystick", "Center");
            posX = radius; posY = centerY;
        }

        joyX = (posX - radius) / (radius - hatRadius - rimWidth);
        joyY = (posY - centerY) / (radius - hatRadius - rimWidth);

        float deadzone = (float) 0.1;
        if (joyX > 0 && joyX < deadzone || joyX < 0 && joyX > -deadzone) joyX = 0;
        if (joyY > 0 && joyY < deadzone || joyY < 0 && joyY > -deadzone) joyY = 0;

        //Log.d("Joystick", "joyX: " + joyX + " joyY: " + joyY);

        if (joyX == 0 && joyY == 0) {
            if (!centered) {
                centered = true;
                vibrator.vibrate(20);
            }
        } else if (centered)
            centered = false;

        context.setJoyX(joyX); context.setJoyY(joyY);
        invalidate();
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        Log.d("Joystick", "Width: " + w);
        radius = w / 2;
        centerY = h / 2;
        hatRadius = radius / 4;
        rimWidth = radius / 20;
        posX = radius; posY = centerY;
        paint.setAntiAlias(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d("Joystick", "surfaceCreated");
        setZOrderOnTop(true);

        if (android.os.Build.VERSION.SDK_INT >= 23)
            colorAccent = getResources().getColor(R.color.colorAccent, null);
        else
            colorAccent = getResources().getColor(R.color.colorAccent);
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
            canvas.drawCircle(radius, centerY, radius, paint);

            // Deadzone lines
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.DKGRAY);
            if (joyX == 0) // Vertical
                canvas.drawLine(radius, centerY - radius + rimWidth, radius, centerY + radius - rimWidth, paint);
            if (joyY == 0) // Horizontal
                canvas.drawLine(rimWidth, centerY, radius * 2 - rimWidth, centerY, paint);

            // Rim
            paint.setStrokeWidth(rimWidth);
            paint.setColor(colorAccent);
            canvas.drawCircle(radius, centerY, (float)(radius * 0.975), paint);

            // Hat
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(posX, posY, hatRadius, paint);

            // Turn arc
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(colorAccent);
            float turn = (float) context.getPlayerShip().getTurn() / context.getPlayerShip().getShipClass().maxTurn * 56;
            canvas.drawArc(-15, centerY - radius - 15, radius * 2 + 15, centerY + radius + 15, -90, turn, false, paint);

            // Yaw indicator
            float yaw = context.getYaw();
            if (yaw != 0 && yaw != 1 && yaw != -1) paint.setColor(Color.DKGRAY);
            yaw = -yaw + (float) Math.PI / 2;
            canvas.drawLine(radius + (radius + 25) * (float) Math.cos(yaw), centerY - (radius + 25) * (float) Math.sin(yaw),
                            radius + (radius + 5) * (float) Math.cos(yaw),  centerY - (radius + 5) * (float) Math.sin(yaw), paint);

            // Turn indicator
            //canvas.drawLine(radius + (radius + 15) * (float) Math.cos(turn), centerY - (radius + 15) * (float) Math.sin(turn),
            //                radius + (radius + 5) * (float) Math.cos(turn),  centerY - (radius + 5) * (float) Math.sin(turn), paint);
            Log.d("turn", turn+"");
            //canvas.drawOval(new RectF(radius, 50, 150, 200), paint);
        }
    }
}
