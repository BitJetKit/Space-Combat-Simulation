package com.example.hyperion.spacecombatsimulation;

import java.lang.reflect.Field;

class Projectile extends PhysicsObject {

    public enum ProjectileType {

        Type1("type1", 0, 10, 1000), // mass, damage, range
        Type2("type2", 10, 5, 5000);

        private final int image, range;
        private final float mass, damage;

        ProjectileType (String image, float mass, float damage, int range) {
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


    Projectile (ProjectileType type, float posX, float posY, float angle) {
        this.type = type;
        this.posX = posX; this.posY = posY; this.angle = angle;
    }

    ProjectileType getType() {
        return type;
    }
}
