package com.yellowbytestudios.steampunkjam.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;

/**
 * Created by BobbyBoy on 09-Jan-16.
 */
public class XBoxController implements BasicController {

    // These button codes were taken from http://www.java-gaming.org/index.php?topic=29223.0 (with thanks!).
    public static final int BUTTON_X = 2;
    public static final int BUTTON_Y = 3;
    public static final int BUTTON_A = 0;
    public static final int BUTTON_B = 1;
    public static final int BUTTON_BACK = 6;
    public static final int BUTTON_START = 7;
    public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
    public static final int BUTTON_LB = 4;
    public static final int BUTTON_L3 = 8;
    public static final int BUTTON_RB = 5;
    public static final int BUTTON_R3 = 9;
    public static final int AXIS_LEFT_X = 0; // -1 is left | +1 is right
    public static final int AXIS_LEFT_Y = 1; // -1 is up | +1 is down
    public static final int AXIS_LEFT_TRIGGER = 4; // value 0 to 1f
    public static final int AXIS_RIGHT_X = 3; // -1 is left | +1 is right
    public static final int AXIS_RIGHT_Y = 2; // -1 is up | +1 is down
    public static final int AXIS_RIGHT_TRIGGER = 2; // value 0 to -1f

    private Controller controller;
    private boolean rightTriggerJustPressed, jumpJustPressed, pauseJustPressed = false;

    public XBoxController(int controllerNumber) {
        controller = Controllers.getControllers().get(controllerNumber);
    }

    @Override
    public boolean leftPressed() {
        return controller.getAxis(AXIS_LEFT_X) < -0.2f;
    }

    @Override
    public boolean rightPressed() {
        return controller.getAxis(AXIS_LEFT_X) > 0.2f;
    }

    @Override
    public boolean jumpPressed() {
        if (controller.getButton(BUTTON_A)) {
            if (!jumpJustPressed) {
                jumpJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            jumpJustPressed = false;
            return false;
        }
    }

    @Override
    public boolean runPressed() {
        return controller.getAxis(AXIS_RIGHT_TRIGGER) < -0.5f;
    }

    @Override
    public boolean downPressed() {
        return false;
    }

    @Override
    public boolean attackPressed() {
        if (controller.getAxis(AXIS_RIGHT_TRIGGER) < -0.5f) {
            if (!rightTriggerJustPressed) {
                rightTriggerJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            rightTriggerJustPressed = false;
            return false;
        }
    }

    @Override
    public boolean pausePressed() {
        if (controller.getButton(BUTTON_START)) {
            if (!pauseJustPressed) {
                pauseJustPressed = true;
                return true;
            } else {
                return false;
            }
        } else {
            pauseJustPressed = false;
            return false;
        }
    }

    @Override
    public boolean grapplePressed() {
        return controller.getButton(BUTTON_X);
    }
}
