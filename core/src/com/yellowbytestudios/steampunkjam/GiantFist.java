package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by Robert on 09-Jun-16.
 */
public class GiantFist {

    private Body body;
    private float ACCELERATION;
    private float SPEED;
    private float posX, posY;
    private float velX, velY;
    private float startX, startY;

    float width = 1.3f;
    float height = 2f;

    private TextureRegion texture;
    private boolean smashing = false;
    private boolean left;

    public GiantFist(Body body, boolean isLeft) {
        this.body = body;
        startX = body.getPosition().x;
        startY = body.getPosition().y;

        left = isLeft;
        Texture t = new Texture(Gdx.files.internal("fist.png"));
        texture = new TextureRegion(t);

        if (left) {
            texture.flip(true, false);
        }
    }

    public void update(Amethyst amy) {
        assignVariables();

        if (amy.getPosition().x > posX && amy.getPosition().x < startX + 5f) {
            moveRight();
        } else if (amy.getPosition().x < posX && amy.getPosition().x > startX - 5f) {
            moveLeft();
        } else {
            moveToCenter();
        }

        if (!smashing && posY < 7) {
            body.setTransform(new Vector2(posX, posY + 0.1f), body.getAngle());
            body.setType(BodyDef.BodyType.StaticBody);

        } else if (amy.getPosition().x < posX + 1 && amy.getPosition().x > posX - 1) {
            smashing = true;
            body.setType(BodyDef.BodyType.DynamicBody);
        }

    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, (posX - (width * 1.2f)) * 100, (posY - (height)) * 100);
    }

    public void moveLeft() {
        body.setTransform(body.getPosition().add(-SPEED / 200, 0), body.getAngle());
    }

    public void moveRight() {
        body.setTransform(body.getPosition().add(SPEED / 200, 0), body.getAngle());
    }

    public void moveToCenter() {
        if (startX+1 < posX) {
            moveLeft();
        } else if (startX-1 > posX) {
            moveRight();
        }
    }

    private void assignVariables() {

        ACCELERATION = Gdx.graphics.getDeltaTime() * 2000f;
        SPEED = Gdx.graphics.getDeltaTime() * 400f;

        velX = body.getLinearVelocity().x;
        velY = body.getLinearVelocity().y;

        posX = body.getPosition().x;
        posY = body.getPosition().y;
    }

    public void reset() {
        smashing = false;
    }

    public boolean isSmashing() {
        return smashing;
    }
}
