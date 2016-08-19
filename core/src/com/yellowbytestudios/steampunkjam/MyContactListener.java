package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.yellowbytestudios.steampunkjam.screens.GameScreen;

/**
 * Created by Robert on 07-Jun-16.
 */

public class MyContactListener implements ContactListener {

    public MyContactListener() {
        super();
    }

    private boolean hitWeakspot = false;

    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            if (fb.getUserData() != null && fb.getUserData().equals("wall")) {
                ((Amethyst) fa.getBody().getUserData()).addNumFootContacts(1);
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            if (fa.getUserData() != null && fa.getUserData().equals("wall")) {
                ((Amethyst) fb.getBody().getUserData()).addNumFootContacts(1);
            }
        }

        if (fa.getUserData() != null && fa.getUserData().equals("fist")) {
            if (fb.getUserData() != null && fb.getUserData().equals("amy")) {
                if (((Amethyst) fb.getBody().getUserData()).isOnGround()) {
                    System.out.println("dead");
                }
            } else {
                ((GiantFist) fa.getBody().getUserData()).reset();
                GameScreen.shakeScreen = true;
            }
        }

        if (fb.getUserData() != null && fb.getUserData().equals("fist")) {
            if (fa.getUserData() != null && fa.getUserData().equals("amy")) {
                if (((Amethyst) fa.getBody().getUserData()).isOnGround()) {
                    System.out.println("dead");
                }
            } else if (fa.getUserData() != null && fa.getUserData().equals("wall")) {
                ((GiantFist) fb.getBody().getUserData()).reset();
                GameScreen.shakeScreen = true;
            }
        }


        if (fa.getUserData() != null && fa.getUserData().equals("weakspot")) {
            if (fb.getUserData() != null && fb.getUserData().equals("amy")) {
                System.out.println("hit");
                hitWeakspot = true;
            }
        }

        if (fb.getUserData() != null && fb.getUserData().equals("weakspot")) {
            if (fa.getUserData() != null && fa.getUserData().equals("amy")) {
                System.out.println("hit");
                hitWeakspot = true;
            }
        }
    }


    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            if (fb.getUserData() != null && fb.getUserData().equals("wall")) {
                ((Amethyst) fa.getBody().getUserData()).addNumFootContacts(-1);
            }
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            if (fa.getUserData() != null && fa.getUserData().equals("wall")) {
                ((Amethyst) fb.getBody().getUserData()).addNumFootContacts(-1);
            }
        }
    }

    public boolean hasHitWeakspot() {
        return hitWeakspot;
    }

    public void setHitWeakspot(boolean hitWeakspot) {
        this.hitWeakspot = hitWeakspot;
    }

    public void preSolve(Contact c, Manifold m) {
    }

    public void postSolve(Contact c, ContactImpulse ci) {
    }
}