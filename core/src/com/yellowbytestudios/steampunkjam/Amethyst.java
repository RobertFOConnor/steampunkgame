package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.steampunkjam.spriter.MySpriterAnimationListener;

/**
 * Created by Robert on 07-Jun-16.
 * <p>
 * Our main protagonist/hero character.
 */
public class Amethyst {

    //Movement variables.
    private float ACCELERATION;
    private float SPEED;
    private float posX, posY;
    private float velX, velY;
    private boolean running = false;
    private boolean jumped = false;
    private boolean doubleJumped = false;
    private boolean jumping = false;
    private boolean attacking = false;
    private boolean swinging = false;

    //Contact variables;
    private int footContacts;

    private Body body;

    private Player spriter;
    private GrappleGun grappleGun;

    public Amethyst(Body body) {
        this.body = body;
        footContacts = 0;

        spriter = MainGame.spriterManager.getSpiter("player", "idle", 0.65f);
    }

    public void update() {
        assignVariables();
        updateSpriterImages();
    }

    public void render(SpriteBatch sb) {
        MainGame.spriterManager.draw(spriter);

        sb.end();
        if (swinging) {
            grappleGun.render();
        }
        sb.begin();
    }

    private void updateSpriterImages() {
        spriter.setPosition((int) (posX * 100f), (int) (posY * 100f) - 60);
        spriter.update();
    }

    public void moveLeft() {
        if (velX > -SPEED) {
            body.applyForce(-ACCELERATION, 0, posX, posY, true);
        } else {
            if (isOnGround()) {
                body.setLinearVelocity(-SPEED, velY);
            }
        }

        if (!facingLeft()) {
            flipSprite();
        }

        if (!jumping) {
            spriter.setAnimation("walking");
        }
    }

    public void moveRight() {
        if (velX < SPEED) {
            body.applyForce(ACCELERATION, 0, posX, posY, true);
        } else {
            if (isOnGround()) {
                body.setLinearVelocity(SPEED, velY);
            }
        }

        if (facingLeft()) {
            flipSprite();
        }

        if (!jumping) {
            spriter.setAnimation("walking");
        }
    }

    public void jump() {
        if (!swinging) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyForce(0, 1500, body.getPosition().x, body.getPosition().y, true);
            if (jumped) {
                Player.PlayerListener myListener = new MySpriterAnimationListener() {
                    @Override
                    public void animationFinished(Animation animation) {
                        spriter.setAnimation("air");
                    }
                };
                spriter.setAnimation("jump");
                spriter.addListener(myListener);
            } else {
                spriter.setAnimation("air");
            }
            jumping = true;
        }
    }

    public void stop() {
        body.setLinearVelocity(0, body.getLinearVelocity().y);
        if (!jumping) {
            spriter.setAnimation("idle");
        }
    }

    public void rotate(float angle) {
        body.setTransform(body.getWorldCenter(), angle);
    }

    public void addNumFootContacts(int num) {
        footContacts += num;

        if (footContacts > 0) {
            jumped = false;
            doubleJumped = false;
            jumping = false;
        }
    }

    public boolean isOnGround() {
        return footContacts > 0;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean hasDoubleJumped() {
        return doubleJumped;
    }

    public void setDoubleJumped(boolean doubleJumped) {
        this.doubleJumped = doubleJumped;
    }

    public boolean hasJumped() {
        return jumped;
    }

    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    public void rewind(RewindPoint rewindPoint) {
        body.setTransform(rewindPoint.getPosition(), body.getAngle());
        body.setLinearVelocity(rewindPoint.getVelocity());
    }

    private void assignVariables() {

        ACCELERATION = Gdx.graphics.getDeltaTime() * 2000f;
        if (running) {
            SPEED = Gdx.graphics.getDeltaTime() * 700f;
        } else {
            SPEED = Gdx.graphics.getDeltaTime() * 400f;
        }

        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean facingLeft() {
        return spriter.flippedX() == 1;
    }

    private void flipSprite() {
        spriter.flip(true, false);
    }

    public Body getBody() {
        return body;
    }

    public boolean isSwinging() {
        return swinging;
    }

    public void setSwinging(boolean swinging) {
        this.swinging = swinging;
    }

    public GrappleGun getGrappleGun() {
        return grappleGun;
    }

    public void setGrappleGun(GrappleGun grappleGun) {
        this.grappleGun = grappleGun;
    }
}
