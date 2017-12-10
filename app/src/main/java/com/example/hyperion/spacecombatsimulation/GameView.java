package com.example.hyperion.spacecombatsimulation;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread thread;
    private Paint paint = new Paint();
    private int[] dustSize, dustColor;
    private int[] dustX, dustY, dustZ;
    private int dustCount, sectorColor, dustDepth = 10;
    private Game context;
    public static int width, height;
    private Map<String, Bitmap> bmp;
    private Matrix shipMatrix;
    private List<Ship> ships;
    private float camX, camY;
    private int shipWidth = 100, shipHeight = 100;

    private int random(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max); }


    public GameView(Context context) { super(context); getHolder().addCallback(this); this.context = (Game) context; }
    public GameView(Context context, AttributeSet attrs) { super(context, attrs); getHolder().addCallback(this); this.context = (Game) context; }
    public GameView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); getHolder().addCallback(this); this.context = (Game) context; }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        width = w; height = h;
        Main.height = h;
        Log.d("Call game","surfaceChanged");

        dustCount = context.getSector().dustCount;
        sectorColor = context.getSector().sectorColor;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        dustSize = new int[dustCount]; dustColor = new int[dustCount];
        dustX = new int[dustCount]; dustY = new int[dustCount]; dustZ = new int[dustCount];

        for (int i = 0; i < dustCount; i++) {
            dustX[i] = random(1, width * dustDepth); dustY[i] = random(1, height * dustDepth);
            dustZ[i] = random(1, dustDepth * 2);
            dustSize[i] = random(1, 3);
            dustColor[i] = Color.argb(random(1, Color.alpha(sectorColor)), Color.red(sectorColor), Color.green(sectorColor), Color.blue(sectorColor));
        }

        ships = context.getShips();

        bmp = new HashMap<>();
        for (Ship ship : ships) {
            Bitmap newBmp;
            newBmp = BitmapFactory.decodeResource(getResources(), ship.getShipClass().image).copy(Bitmap.Config.ARGB_8888, true);
            bmp.put(ship.getShipClass().name, Bitmap.createScaledBitmap(newBmp, ship.getShipClass().width, ship.getShipClass().height, false));
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setWillNotDraw(true);
        thread = new DrawThread(holder, this);
        thread.setRunning(true);
        thread.start();
        Log.d("Thread game view", "Started");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        try {
            thread.setRunning(false);
            thread.join();
            Log.d("Thread game view", "Joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {

        for (int i = 0; i < dustCount; i++) {
            paint.setColor(dustColor[i]);
            canvas.drawCircle((float) dustX[i] / dustDepth, (float) dustY[i] / dustDepth, dustSize[i], paint);
        }

        for (Ship ship : ships) {
            if (ship.getX() > camX - width / 2 - ship.getShipClass().width / 2  && ship.getX() < camX + width / 2 + ship.getShipClass().width / 2
             && ship.getY() > camY - height / 2 - ship.getShipClass().height / 2 && ship.getY() < camY + height / 2 + ship.getShipClass().height / 2) {
                canvas.drawBitmap(bmp.get(ship.getShipClass().name), shipMatrix, null);
            }
        }
    }

    protected void doDraw() {

        postInvalidate();

        float oldCamX = camX, oldCamY = camY, camDX, camDY;
        camX = context.getCamX();
        camY = context.getCamY();
        camDX = camX - oldCamX;
        camDY = camY - oldCamY;

        for (int i = 0; i < dustCount; i++) {
            if (dustY[i] < -dustDepth || dustY[i] > height * dustDepth + dustDepth ||
                dustX[i] < -dustDepth || dustX[i] > width * dustDepth + dustDepth) {

                if (dustY[i] < -dustDepth) {
                    dustX[i] = random(1, width * dustDepth);
                    dustY[i] = height * dustDepth + dustDepth;
                } else if (dustY[i] > height * dustDepth + dustDepth) {
                    dustX[i] = random(1, width * dustDepth);
                    dustY[i] = -dustDepth;
                } else if (dustX[i] < -dustDepth) {
                    dustX[i] = width * dustDepth + dustDepth;
                    dustY[i] = random(1, height * dustDepth);
                } else {
                    dustX[i] = -dustDepth;
                    dustY[i] = random(1, height * dustDepth);
                }

/*
                if (dustY[i] < -dustDepth) {
                    dustX[i] = random(1, width * dustDepth);
                    dustY[i] += dustY[i];
                } else if (dustY[i] > height * dustDepth + dustDepth) {
                    dustX[i] = random(1, width * dustDepth);
                    dustY[i] -= dustY[i];
                } else if (dustX[i] < -dustDepth) {
                    dustX[i] += dustX[i];
                    dustY[i] = random(1, height * dustDepth);
                } else {
                    dustX[i] -= dustX[i];
                    dustY[i] = random(1, height * dustDepth);
                }*/

                dustZ[i] = Math.max(1, Math.min(dustDepth * 2, dustZ[i] + random(-3, 3)));
                dustSize[i] = random(1, 3);
                dustColor[i] = Color.argb(random(1, Color.alpha(sectorColor)), Color.red(sectorColor), Color.green(sectorColor), Color.blue(sectorColor));
            } else {
                dustX[i] -= dustZ[i] * camDX;
                dustY[i] -= dustZ[i] * camDY;
            }
        }

        for (Ship ship : ships) {
            shipMatrix = new Matrix();
            shipMatrix.setRotate(ship.getAngle(), ship.getShipClass().width / 2, ship.getShipClass().height / 2);
            shipMatrix.postTranslate(width / 2 - ship.getShipClass().width / 2 + ship.getX() - camX, height / 2 - ship.getShipClass().height / 2 + ship.getY() - camY);
        }
    }
}
