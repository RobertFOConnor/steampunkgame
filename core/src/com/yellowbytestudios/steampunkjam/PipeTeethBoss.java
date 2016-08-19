package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.brashmonkey.spriter.Player;

/**
 * Created by Robert on 02-Jul-16.
 */
public class PipeTeethBoss {

    private Amethyst amy;
    private Player headSpriter;
    private GiantFist lefty, righty;
    private int health = 100;

    public PipeTeethBoss(World world, Amethyst amy) {

        this.amy = amy;
        lefty = createFist(world, new Vector2(4, 7), true);
        righty = createFist(world, new Vector2(14, 7), false);
        headSpriter = MainGame.spriterManager.getSpiter("boss", "idle", 1f);
        createWeakSpot(world);
    }

    public void update() {
        headSpriter.setPosition(MainGame.WIDTH / 2 - 650 / 2, MainGame.HEIGHT / 2 - 840 / 2);
        headSpriter.update();

        lefty.update(amy);
        righty.update(amy);
    }

    public void drawHead() {
        MainGame.spriterManager.draw(headSpriter);
    }

    public void drawFists(SpriteBatch sb) {
        lefty.render(sb);
        righty.render(sb);
    }


    public static GiantFist createFist(World world, Vector2 pos, boolean left) {
        float width = 1.3f;
        float height = 2f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(MainGame.WIDTH / 2 / 100, MainGame.HEIGHT / 2 / 100);

        Body body = world.createBody(bodyDef);
        body.setGravityScale(0.5f);

        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        fixDef.shape = shape;
        fixDef.friction = 0;

        body.createFixture(fixDef).setUserData("enemy");

        // Create box for players foot.
        shape = new PolygonShape();
        shape.setAsBox(1.2f, 0.1f, new Vector2(0, -2f), 0);

        // Create Fixture Definition for foot collision box.
        fixDef.shape = shape;
        fixDef.isSensor = true;

        // create player foot fixture
        body.createFixture(fixDef).setUserData("fist");
        body.setTransform(pos.x, pos.y, body.getAngle());
        shape.dispose();

        GiantFist enemy = new GiantFist(body, left);
        body.setUserData(enemy);

        return enemy;
    }

    public void createWeakSpot(World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(MainGame.WIDTH / 2f / 100f, MainGame.HEIGHT / 2f / 100f+2.1f);

        Body body = world.createBody(bodyDef);

        FixtureDef fixDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);
        fixDef.isSensor = true;
        fixDef.shape = shape;
        fixDef.friction = 0;

        body.createFixture(fixDef).setUserData("weakspot");
        shape.dispose();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        System.out.println("BOSS HEALTH: " + health);
    }
}
