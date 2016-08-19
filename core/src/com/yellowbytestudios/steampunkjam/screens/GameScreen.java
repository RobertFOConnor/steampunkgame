package com.yellowbytestudios.steampunkjam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.brashmonkey.spriter.Player;
import com.yellowbytestudios.steampunkjam.Amethyst;
import com.yellowbytestudios.steampunkjam.GrappleGun;
import com.yellowbytestudios.steampunkjam.MainGame;
import com.yellowbytestudios.steampunkjam.MyContactListener;
import com.yellowbytestudios.steampunkjam.PipeTeethBoss;
import com.yellowbytestudios.steampunkjam.RewindPoint;
import com.yellowbytestudios.steampunkjam.cameras.BoundedCamera;
import com.yellowbytestudios.steampunkjam.controllers.BasicController;
import com.yellowbytestudios.steampunkjam.controllers.KeyboardController;
import com.yellowbytestudios.steampunkjam.controllers.XBoxController;

/**
 * Created by Robert on 07-Jun-16.
 * <p/>
 * The game screen which will display the level and players.
 */
public class GameScreen implements Screen {

    private World world; //World variables.
    private BoundedCamera cam, b2dCam;
    private Box2DDebugRenderer b2dr;
    private MyContactListener contactListener;
    private Amethyst amy; //Player object.
    private BasicController controller;
    private PipeTeethBoss boss; //Boss object.
    private Array<RewindPoint> rewindArray; //Rewind Array.
    private long startTurnTime; //Time variables for rewind feature.
    public static long duration;
    private float lastPoint;
    public static boolean shakeScreen = false; //Screen shaking variables.
    private int shakeCount = 0;
    private Texture bg; //Textures.
    private Player backgroundSpriter;
    private boolean showBounds = true; //Testing variables.
    private boolean bossLevel = false;
    private Body ceiling; //The ceiling body needed for grapple hook.

    @Override
    public void create() {

        //Setup camera variables.
        b2dCam = new BoundedCamera();
        cam = new BoundedCamera();

        cam.setToOrtho(false, MainGame.WIDTH, MainGame.HEIGHT);
        cam.setBounds(-200, MainGame.WIDTH + 200, -200, MainGame.HEIGHT + 200);
        b2dCam.setToOrtho(false, MainGame.WIDTH / 100, MainGame.HEIGHT / 100);
        b2dCam.setBounds(-20f, MainGame.WIDTH / 100f + 20f, -20f, MainGame.HEIGHT / 100f + 20f);
        cam.setPosition(MainGame.WIDTH / 2, MainGame.HEIGHT / 2);
        b2dCam.setPosition((MainGame.WIDTH / 2f / 100f), (MainGame.HEIGHT / 2f / 100f));

        //Setup world and contact listener.
        world = new World(new Vector2(0, -98), true);
        b2dr = new Box2DDebugRenderer();
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        //Identify which controller the player will use to play.
        if (MainGame.hasControllers) {
            controller = new XBoxController(0);
        } else {
            controller = new KeyboardController();
        }

        //Add the walls to the Box2D world.
        addWalls(world);
        amy = createPlayer(world);

        //Setup the grapple gun and give it to the player.
        GrappleGun grappleGun = new GrappleGun(world, cam, amy, ceiling);
        amy.setGrappleGun(grappleGun);

        //Setup the boss object.
        if (bossLevel) {
            boss = new PipeTeethBoss(world, amy);
        }

        //Setup the rewind array which stores rewind points.
        rewindArray = new Array<RewindPoint>();

        //Setup the timer for rewinding time.
        duration = 30000;
        startTurnTime = System.nanoTime();
        lastPoint = 0;

        //Finally setup the textures and sprites.
        bg = new Texture(Gdx.files.internal("bg.png"));
        backgroundSpriter = MainGame.spriterManager.getSpiter("background", "background", 1f);
    }

    @Override
    public void update(float step) {

        backgroundSpriter.setPosition(0, 0);
        backgroundSpriter.update();

        updateCameras();

        world.step(step, 8, 3);
        amy.update();

        if (bossLevel) {
            boss.update();
            if (contactListener.hasHitWeakspot()) {
                boss.setHealth(boss.getHealth() - 1);
                contactListener.setHitWeakspot(false);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F8)) {
            showBounds = !showBounds;
        }

        if (controller.grapplePressed()) {
            if (!amy.isSwinging()) {
                amy.getGrappleGun().shoot();
                amy.setSwinging(true);
                amy.setDoubleJumped(false);
            }
        } else {
            if (amy.isSwinging()) {
                amy.getGrappleGun().release();
                amy.setSwinging(false);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            if (rewindArray.size > 0) {
                amy.rewind(rewindArray.get(rewindArray.size - 1));
                rewindArray.removeIndex(rewindArray.size - 1);
            }

        } else {

            if (controller.leftPressed()) {
                amy.setRunning(controller.runPressed());
                amy.moveLeft();
            } else if (controller.rightPressed()) {
                amy.setRunning(controller.runPressed());
                amy.moveRight();
            } else if (amy.isOnGround()) {
                amy.stop();
            }

            if (controller.jumpPressed()) {
                if (!amy.hasJumped()) {
                    amy.jump();
                    amy.setJumped(true);
                } else if (!amy.hasDoubleJumped()) {
                    amy.jump();
                    amy.setDoubleJumped(true);
                }
            }

            if (controller.attackPressed()) {
                amy.setAttacking(true);
            } else {
                amy.setAttacking(false);
            }

            long time = ((System.nanoTime() - startTurnTime) / 10000000);
            lastPoint += time;
            if (lastPoint > 300) {
                rewindArray.add(new RewindPoint(amy.getPosition().cpy(), amy.getVelocity().cpy()));

                if (rewindArray.size > 5000) {
                    rewindArray.removeIndex(0);
                }
                lastPoint = 0;
            }
        }
    }

    private void updateCameras() {
        if (shakeScreen) {
            shakeTheScreen();
        }
        cam.update();
        b2dCam.update();
    }

    private void shakeTheScreen() {
        if (shakeCount < 20) {
            float xAdjustment = MathUtils.random(-0.2f, 0.2f); // get random number between -0.2 and 0.2
            float yAdjustment = MathUtils.random(-0.2f, 0.2f); // get random number between -0.2 and 0.2

            cam.setPosition((MainGame.WIDTH / 2 + (xAdjustment * 100)), (MainGame.HEIGHT / 2 + (yAdjustment * 100)));
            b2dCam.setPosition(MainGame.WIDTH / 2f / 100f + xAdjustment, MainGame.HEIGHT / 2f / 100f + yAdjustment);

            shakeCount++;

        } else {
            cam.setPosition(MainGame.WIDTH / 2, MainGame.HEIGHT / 2);
            b2dCam.setPosition(MainGame.WIDTH / 2f / 100f, MainGame.HEIGHT / 2f / 100f);
            shakeScreen = false;
            shakeCount = 0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        MainGame.spriterManager.draw(backgroundSpriter);
        sb.draw(bg, 0, 0);
        if (bossLevel) {
            boss.drawHead();

            boss.drawFists(sb);
        }
        amy.render(sb);
        sb.end();

        if (showBounds) {
            b2dr.render(world, b2dCam.combined);
        }
    }

    public void addWalls(World world) {
        float width = MainGame.WIDTH / 2f / 100f;
        float height = 0.5f;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(MainGame.WIDTH / 2f / 100f, 1.5f);
        Body body = world.createBody(bodyDef);
        shape.setAsBox(width, height);
        fixDef.shape = shape;
        body.createFixture(fixDef).setUserData("wall");

        bodyDef.position.set(MainGame.WIDTH / 2f / 100f, MainGame.HEIGHT / 100f - 1f);
        body = world.createBody(bodyDef);
        shape.setAsBox(width, height);
        fixDef.shape = shape;
        body.createFixture(fixDef).setUserData("wall");
        ceiling = body;

        bodyDef.position.set(0.5f, MainGame.HEIGHT / 2f / 100f);
        body = world.createBody(bodyDef);
        shape.setAsBox(height, width);
        fixDef.shape = shape;
        body.createFixture(fixDef).setUserData("wall");

        bodyDef.position.set(MainGame.WIDTH / 100f - 0.5f, MainGame.HEIGHT / 2f / 100f);
        body = world.createBody(bodyDef);
        shape.setAsBox(height, width);
        fixDef.shape = shape;
        body.createFixture(fixDef).setUserData("wall");

        shape.dispose();
    }

    public Amethyst createPlayer(World world) {
        float width = 0.38f;
        float height = 0.38f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(MainGame.WIDTH / 2f / 100f, 3f);

        Body body = world.createBody(bodyDef);

        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        fixDef.shape = shape;
        fixDef.friction = 0;

        body.createFixture(fixDef).setUserData("amy");

        // Create box for players foot.
        shape = new PolygonShape();
        shape.setAsBox(0.36f, 0.02f, new Vector2(0, -0.38f), 0);

        // Create Fixture Definition for foot collision box.
        fixDef.shape = shape;
        fixDef.isSensor = true;

        // create player foot fixture
        body.createFixture(fixDef).setUserData("foot");
        shape.dispose();

        Amethyst amethyst = new Amethyst(body);
        body.setUserData(amethyst);

        return amethyst;
    }


    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void goBack() {

    }
}
