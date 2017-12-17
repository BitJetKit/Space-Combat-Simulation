package com.example.hyperion.spacecombatsimulation;

import android.util.Log;
import java.lang.reflect.Field;

class Ship extends PhysicsObject {

    public enum ShipClass {

        Starfighter ("Starfighter", "starfighter", 200, 200,
                30, 4, 0.11f,         // maxVel, maxTurn, turnRate
                0.22f, 0.12f, 0.15f, // maxThrust, strafe, reverse
                100, 1, 100,        // shield, shieldRate, hull
                10, 10),           // damage, energy

        Shuttle ("Shuttle", "shuttle", 130, 200,
                50, 2, 0.03f,
                0.16f, 0.05f, 0.1f,
                80, 1, 80,
                9, 12);

        protected final String name;
        protected final int image, width, height;
        protected final float maxVelocity, maxTurn, turnRate, maxThrust, strafe, reverse, shield, shieldRate, hull, damage, energy;

        ShipClass (String name, String image, int width, int height, float maxVel, float maxTurn, float turnRate,
                   float maxThr, float strafe, float reverse, float shield, float shieldRate, float hull, float dmg, float energy) {
            this.name = name;
            this.width = width; this.height = height;
            this.maxVelocity = maxVel; this.maxTurn = maxTurn; this.turnRate = turnRate; this.maxThrust = maxThr; this.strafe = strafe;
            this.reverse = reverse; this.shield = shield; this.shieldRate = shieldRate; this.hull = hull; this.damage = dmg; this.energy = energy;

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

    private final ShipClass shipClass;
    private float angleRad;


    Ship(ShipClass shipClass, float posX, float posY, float angle) {
        this.shipClass = shipClass;
        this.posX = posX; this.posY = posY; this.angle = angle;
    }

    void move(float accelerationLongitudinal, float accelerationLateral, float accelerationAngular) {

        angleRad = getAngleRad();

        // Movement
        if (accelerationLongitudinal > 0) {
            this.velX += Math.cos(angleRad) * accelerationLateral * shipClass.strafe + Math.sin(angleRad) * accelerationLongitudinal * shipClass.maxThrust;
            this.velY += Math.sin(angleRad) * accelerationLateral * shipClass.strafe - Math.cos(angleRad) * accelerationLongitudinal * shipClass.maxThrust;
        } else {
            this.velX += Math.cos(angleRad) * accelerationLateral * shipClass.strafe + Math.sin(angleRad) * accelerationLongitudinal * shipClass.reverse;
            this.velY += Math.sin(angleRad) * accelerationLateral * shipClass.strafe - Math.cos(angleRad) * accelerationLongitudinal * shipClass.reverse;
        }

        // Turning
        if (accelerationAngular == 0) {
            if (turn > 0) {
                turn -= shipClass.turnRate;
                if (turn < 0)
                    turn = 0;
            } else if (turn < 0) {
                turn += shipClass.turnRate;
                if (turn > 0)
                    turn = 0;
            }
        } else if (turn < shipClass.maxTurn * accelerationAngular) {
            turn += shipClass.turnRate;
            if (turn > shipClass.maxTurn)
                turn = shipClass.maxTurn;
        } else if (turn > shipClass.maxTurn * accelerationAngular) {
            turn -= shipClass.turnRate;
            if (turn < -shipClass.maxTurn)
                turn = -shipClass.maxTurn;
        }

        // Speed limit
        velocity = Math.sqrt(Math.pow(Math.abs(velX), 2) + Math.pow(Math.abs(velY), 2));
        if (velocity > shipClass.maxVelocity) {
            velX = velX * shipClass.maxVelocity / velocity;
            velY = velY * shipClass.maxVelocity / velocity;
            velocity = shipClass.maxVelocity;
        }

        // Automatic stop
        if (velocity < 0.5 && accelerationLongitudinal == 0 && accelerationLateral == 0 && velocity != 0) {
            if (velX != 0) {
                velX -= velX / 20;
                if (velX > 0 && velX < 0.05 || velX < 0 && velX > -0.05) velX = 0;
            }
            if (velY != 0) {
                velY -= velY / 20;
                if (velY > 0 && velY < 0.05 || velY < 0 && velY > -0.05) velY = 0;
            }
        }

        // Clamp turn
        turn = Math.max(-shipClass.maxTurn, Math.min(turn, shipClass.maxTurn));

        posX += velX;
        posY += velY;
        angle += turn;
        angle = (angle + 360) % 360;
    }

    boolean fire() {
        //move(-1,0,0);
        return true;
    }

    void hit(Projectile projectile) {
        Log.d("debug", "hit!");
    }

    float getBarrelX() {
        return posX + (float) Math.sin(angleRad) * 100;
    }
    float getBarrelY() {
        return posY - (float) Math.cos(angleRad) * 100;
    }
    int getShieldRadius() {
        return shipClass.width > shipClass.height ? shipClass.width / 2: shipClass.height / 2;
    }
    ShipClass getShipClass() {
        return shipClass;
    }
}
