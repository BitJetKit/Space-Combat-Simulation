package com.example.hyperion.spacecombatsimulation;

import android.util.Log;

import java.lang.reflect.Field;

class Projectile extends PhysicsObject {

    public enum ProjectileType {

        Type1("type1", "Type 1", 10, 15,
                10, 10, 0,     // initVel, maxVel, acceleration
                0, 10, 1000), // mass, damage, range
        Type2("type2", "Type 2", 10, 15,
                10, 10, 0,
                10, 5, 5000);

        protected final String name;
        protected final int image, width, height, range;
        protected final float initVel, maxVelocity, acceleration, mass, damage;

        ProjectileType (String image, String name, int width, int height, float initVel, float maxVelocity, float acceleration, float mass, float damage, int range) {

            this.name = name; this.width = width; this.height = height; this.initVel = initVel; this.maxVelocity = maxVelocity; this.acceleration = acceleration;
            this.mass = mass; this.damage = damage; this.range = range;

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
    private int rangeFlown;


    Projectile (ProjectileType type, float posX, float posY, float angle) {
        this.type = type;
        this.posX = posX; this.posY = posY; this.angle = angle;

        float angleRad = (float) (angle * Math.PI / 180);
        this.velX =  Math.sin(angleRad) * getType().initVel;
        this.velY = -Math.cos(angleRad) * getType().initVel;
    }

    void move() {

        float angleRad = (float) (angle * Math.PI / 180);

        // Acceleration
        this.velX +=  Math.sin(angleRad) * getType().acceleration;
        this.velY += -Math.cos(angleRad) * getType().acceleration;

        // Speed limit
        velocity = Math.sqrt(Math.pow(Math.abs(velX), 2) + Math.pow(Math.abs(velY), 2));
        if (velocity > getType().maxVelocity) {
            velX = velX * getType().maxVelocity / velocity;
            velY = velY * getType().maxVelocity / velocity;
            velocity = getType().maxVelocity;
        }

        posX += velX;
        posY += velY;
        angle += turn;
        angle = (angle + 360) % 360;
        rangeFlown += velocity;
    }

    boolean canRemove() {
        return rangeFlown >= this.type.range;
    }

    ProjectileType getType() {
        return type;
    }
}
