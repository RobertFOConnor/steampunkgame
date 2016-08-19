package com.yellowbytestudios.steampunkjam.controllers;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public interface BasicController {

    boolean leftPressed();
    boolean rightPressed();
    boolean jumpPressed();
    boolean runPressed();
    boolean downPressed();
    boolean attackPressed();
    boolean pausePressed();
    boolean grapplePressed();
}
