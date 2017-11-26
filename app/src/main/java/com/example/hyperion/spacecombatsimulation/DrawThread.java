package com.example.hyperion.spacecombatsimulation;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private MenuBackground surfaceView;
    private boolean running = false;
    private int counter = 0;


    DrawThread(SurfaceHolder surfaceHolder, MenuBackground panel) {
        this.surfaceHolder = surfaceHolder;
        surfaceView = panel;
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {

        long lastTime = System.currentTimeMillis();

        while (running) {

            long currentTime = System.currentTimeMillis();
            double dt = (currentTime - lastTime) * 0.001;
            lastTime = currentTime;
            counter += dt * 1000;

            if (counter >= 50) {
                counter = 0;

                Canvas c = null;

                try {
                    c = surfaceHolder.lockCanvas();
                    if (c != null)
                        surfaceView.doDraw(c);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null)
                        surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }
}

