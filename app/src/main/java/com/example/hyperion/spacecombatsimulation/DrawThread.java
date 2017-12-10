package com.example.hyperion.spacecombatsimulation;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private MenuBackground menuView;
    private GameView gameView;
    private boolean running = false;
    private boolean game = false;
    private int counter = 0;


    DrawThread(SurfaceHolder surfaceHolder, MenuBackground panel) {
        this.surfaceHolder = surfaceHolder;
        menuView = panel;
    }

    DrawThread(SurfaceHolder surfaceHolder, GameView panel) {
        this.surfaceHolder = surfaceHolder;
        gameView = panel;
        gameView.setZOrderOnTop(true);
        game = true;
    }

    void setRunning (boolean running) {
        this.running = running;
    }

    @Override
    public void run() {

        long lastTime = System.currentTimeMillis();
        int delay = 40;

        while (running) {

            long currentTime = System.currentTimeMillis();
            double dt = (currentTime - lastTime) * 0.001;
            lastTime = currentTime;
            counter += dt * 1000;

            if (counter >= delay) {
                counter = 0;

                Canvas c = null;

                try {
                    c = surfaceHolder.lockCanvas();
                    if (c != null & game)
                        gameView.doDraw();
                    else if (c != null)
                        menuView.doDraw();

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

