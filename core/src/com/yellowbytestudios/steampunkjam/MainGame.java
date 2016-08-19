package com.yellowbytestudios.steampunkjam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yellowbytestudios.steampunkjam.screens.GameScreen;
import com.yellowbytestudios.steampunkjam.screens.ScreenManager;
import com.yellowbytestudios.steampunkjam.spriter.SpriterManager;

public class MainGame extends ApplicationAdapter {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;

    //Frame-rate variables.
    public static final float STEP = 1 / 60f;
    private FPSLogger fpsLogger;

    private SpriteBatch sb;
    public static SpriterManager spriterManager;

    //Controller support variables.
    public static boolean hasControllers = false;
    private boolean fullscreen = false;

    @Override
    public void create() {
        sb = new SpriteBatch();
        fpsLogger = new FPSLogger();
        spriterManager = new SpriterManager(sb);

        checkForController();
        ScreenManager.setScreen(new GameScreen());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenManager.getCurrentScreen().update(STEP);
        ScreenManager.getCurrentScreen().render(sb);
        fpsLogger.log();

        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {

                if (!fullscreen) {
                    Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(mode);
                } else {
                    Gdx.graphics.setWindowedMode(1280, 720);
                }
                fullscreen = !fullscreen;
            }
        }
    }

    private void checkForController() {
        if (Controllers.getControllers().size != 0) {
            hasControllers = true;
        }
    }
}
