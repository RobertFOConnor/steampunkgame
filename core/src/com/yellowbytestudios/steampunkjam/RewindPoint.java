package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Robert on 08-Jun-16.
 */
public class RewindPoint {

    private Vector2 position;
    private Vector2 velocity;

    public RewindPoint(Vector2 position, Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
