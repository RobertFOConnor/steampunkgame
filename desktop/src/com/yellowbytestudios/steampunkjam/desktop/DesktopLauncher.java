package com.yellowbytestudios.steampunkjam.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yellowbytestudios.steampunkjam.MainGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Steam Punk");
        config.setWindowedMode(1280, 720);
        config.setResizable(false);
        config.useVsync(true);
        config.disableAudio(false);

        new Lwjgl3Application(new MainGame(), config);
    }
}
