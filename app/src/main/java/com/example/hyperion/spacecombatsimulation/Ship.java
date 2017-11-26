package com.example.hyperion.spacecombatsimulation;

import android.graphics.Color;

public class Ship {

    private double posX, posY, angle;
    private double maxVelocity = 100, maxTurn = 100, shield = 100, shieldRate = 1, hull = 100, damage = 10;
    private double velX = 0, velY = 0, turn = 0, thrust = 0;
    private int color1 = Color.WHITE, color2 = Color.GRAY;

    public Ship(double posX, double posY, double angle) {
        this.posX = posX; this.posY = posY; this.angle = angle;
    };
}
