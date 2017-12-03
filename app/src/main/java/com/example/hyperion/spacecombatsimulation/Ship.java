package com.example.hyperion.spacecombatsimulation;

import android.graphics.Color;
import android.util.Log;
import java.lang.reflect.Field;

class Ship {

    public enum ShipClass {

        Starfighter ("Starfighter", "starfighter", 100, 100, 10, 10, 10, 100, 1, 100, 10),
        Shuttle ("Shuttle", "shuttle", 120, 90, 15, 6, 8, 80, 1, 80, 9);

        protected final String name;
        protected final int image;
        protected final double maxVelocity, maxTurn, maxThrust, strafe, reverse, shield, shieldRate, hull, damage;

        ShipClass (String name, String image, double maxVel, double maxTurn, double maxThr, double strafe, double reverse,
               double shield, double shieldRate, double hull, double dmg) {
            this.name = name;
            this.maxVelocity = maxVel; this.maxTurn = maxTurn; this.maxThrust = maxThr; this.strafe = strafe;
            this.reverse = reverse; this.shield = shield; this.shieldRate = shieldRate; this.hull = hull; this.damage = dmg;

            try {
                Class res = R.drawable.class;
                Field field = res.getField(image);
                this.image = field.getInt(null);
            } catch (Exception e) {
                Log.e("ERROR", "Error getting resource ID: " + image);
                throw new RuntimeException("Error getting resource ID", e);
            }
        }
    }

    private double posX, posY, angle;
    private double velX = 0, velY = 0, turn = 0, thrust = 0;
    private int color1 = Color.WHITE, color2 = Color.GRAY;

    public Ship(ShipClass ship, double posX, double posY, double angle) {
        this.posX = posX; this.posY = posY; this.angle = angle;
    }
}
