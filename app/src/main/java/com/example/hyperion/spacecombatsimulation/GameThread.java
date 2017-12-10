package com.example.hyperion.spacecombatsimulation;


import android.content.Context;

class GameThread extends Thread {

    private boolean running = false;
    private int counter = 0, delay = 50;
    private Game context;


    GameThread(int delay, Context context) {
        this.delay = delay;
        this.context = (Game) context;
    }

    void setRunning (boolean running) {
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

            if (counter >= delay) {
                counter = 0;
                context.update();
            }
        }
    }

}
