package com.example.hyperion.spacecombatsimulation;

class PhysicsObject {

    double velX = 0, velY = 0, velocity = 0;
    float posX, posY, angle, turn = 0;

    float getX() {
        return posX;
    }
    float getY() {
        return posY;
    }
    float getAngle() {
        return angle;
    }
    double getVelX() {
        return velX;
    }
    double getVelY() {
        return velY;
    }
    double getVelocity() {
        return velocity;
    }
    double getTurn() {
        return turn;
    }

    void setPosX(float posX) {
        this.posX = posX;
    }
    void setPosY(float posY) {
        this.posY = posY;
    }
    void setVelX(double velX) {
        this.velX = velX;
    }
    void setVelY(double velY) {
        this.velY = velY;
    }
    void setAngle(float angle) {
        this.angle = angle;
    }

    void move(float accelerationLongitudinal, float accelerationLateral, float accelerationAngular) {

        double angleRad = angle * Math.PI / 180;
        this.velX +=  Math.cos(angleRad) * accelerationLongitudinal - Math.sin(angleRad) * accelerationLateral;
        this.velY +=  Math.sin(angleRad) * accelerationLongitudinal + Math.cos(angleRad) * accelerationLateral;
        this.turn += accelerationAngular;

        posX += velX;
        posY += velY;
        angle += turn;
        angle = (angle + 360) % 360;
    }
}
