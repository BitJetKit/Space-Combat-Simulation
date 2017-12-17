package com.example.hyperion.spacecombatsimulation;

import android.util.Log;

import java.lang.reflect.Field;

class Projectile extends PhysicsObject {

    public enum ProjectileType {

        Type1 ("type1", "Type 1", 10, 15,
                20, 20, 0,     // initVel, maxVel, acceleration
                0, 10, 200),  // mass, damage, lifetime
        Type2 ("type2", "Type 2", 10, 15,
                10, 10, 0,
                10, 5, 500);

        protected final String name;
        protected final int image, width, height, lifetime;
        protected final float initVelocity, maxVelocity, acceleration, mass, damage;

        ProjectileType (String image, String name, int width, int height, float initVelocity, float maxVelocity, float acceleration, float mass, float damage, int lifetime) {

            this.name = name; this.width = width; this.height = height; this.initVelocity = initVelocity; this.maxVelocity = maxVelocity; this.acceleration = acceleration;
            this.mass = mass; this.damage = damage; this.lifetime = lifetime;

            try {
                Class res = R.drawable.class;
                Field field = res.getField(image);
                this.image = field.getInt(null);
            } catch (Exception e) {
                throw new RuntimeException("Error getting resource ID", e);
            }
        }
    }

    private final ProjectileType type;
    private final float maxVelocity;
    private int timeFlown;


    Projectile (ProjectileType type, Ship ship) {
        this.type = type; this.posX = ship.getBarrelX(); this.posY = ship.getBarrelY(); this.angle = ship.getAngle();

        float angleRad = (float) (angle * Math.PI / 180);
        velX =  Math.sin(angleRad) * getType().initVelocity + ship.getVelX();
        velY = -Math.cos(angleRad) * getType().initVelocity + ship.getVelY();

        velocity = Math.sqrt(Math.pow(Math.abs(velX), 2) + Math.pow(Math.abs(velY), 2));
        maxVelocity = (float) velocity + getType().maxVelocity;
    }

    void move() {

        float angleRad = (float) (angle * Math.PI / 180);

        // Acceleration
        velX +=  Math.sin(angleRad) * getType().acceleration;
        velY += -Math.cos(angleRad) * getType().acceleration;

        // Speed limit
        velocity = Math.sqrt(Math.pow(Math.abs(velX), 2) + Math.pow(Math.abs(velY), 2));
        if (velocity > maxVelocity) {
            velX = velX * maxVelocity / velocity;
            velY = velY * maxVelocity / velocity;
            velocity = maxVelocity;
        }

        posX += velX;
        posY += velY;
        angle += turn;
        angle = (angle + 360) % 360;
        timeFlown++;
    }

    boolean inBounds(Ship ship) {
        return Math.pow(posX - ship.getX(), 2) + Math.pow(posY - ship.getY(), 2) <= Math.pow(ship.getShieldRadius(), 2);
    }

    boolean canRemove() {
        return timeFlown >= type.lifetime;
    }

    ProjectileType getType() {
        return type;
    }
}
