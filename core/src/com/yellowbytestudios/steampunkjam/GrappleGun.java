package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.yellowbytestudios.steampunkjam.cameras.BoundedCamera;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 04-Aug-16.
 */
public class GrappleGun {

    private ShapeRenderer shapeRenderer; //Shape renderer for drawing grapple rope.
    private List<Body> links;
    private List<Joint> joints;
    private Vector2 anchor;

    private World world;
    private BoundedCamera cam;
    private Amethyst amy;
    private Body ceiling;

    public GrappleGun(World world, BoundedCamera cam, Amethyst amy, Body ceiling) {
        this.world = world;
        this.cam = cam;
        this.amy = amy;
        this.ceiling = ceiling;
        shapeRenderer = new ShapeRenderer();
    }

    public void render() {
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(amy.getPosition().x * 100f, amy.getPosition().y * 100f, anchor.x * 100f, (anchor.y+0.4f) * 100f, 6);
        shapeRenderer.end();
    }

    public void shoot() {
        if (amy.facingLeft()) {
            anchor = new Vector2(amy.getPosition().x - 3f, ceiling.getPosition().y - 0.5f);
        } else {
            anchor = new Vector2(amy.getPosition().x + 3f, ceiling.getPosition().y - 0.5f);
        }

        // Create links.
        links = new ArrayList<Body>();
        joints = new ArrayList<Joint>();
        for (int i = 1; i < 3; i++) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(anchor);
            Body link = world.createBody(bodyDef);
            links.add(link);
        }

        // Connects links together. First link is connected directly to the "ceiling".
        Body lastLink = null;
        for (Body link : links) {
            DistanceJointDef distanceJointDef = new DistanceJointDef();
            if (lastLink == null) {
                distanceJointDef.initialize(link, ceiling, link.getPosition(), anchor);
            } else {
                distanceJointDef.initialize(link, amy.getBody(), link.getPosition(), amy.getBody().getPosition());
            }
            joints.add(world.createJoint(distanceJointDef));
            lastLink = link;
        }
    }

    public void release() {
        for (int i = 0; i < 2; i++) {
            world.destroyJoint(joints.get(i));
        }

        for (Body b : links) {
            world.destroyBody(b);
        }

        links = new ArrayList<Body>();
        joints = new ArrayList<Joint>();
    }

}
