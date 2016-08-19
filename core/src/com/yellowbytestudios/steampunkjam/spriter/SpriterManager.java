package com.yellowbytestudios.steampunkjam.spriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Drawer;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.SCMLReader;

public class SpriterManager {

    private Data data;
    private Drawer<Sprite> drawer;

    public SpriterManager(SpriteBatch sb) {
        ShapeRenderer renderer = new ShapeRenderer();
        FileHandle handle = Gdx.files.internal("amy/amy.scml");
        data = new SCMLReader(handle.read()).getData();

        LibGdxLoader loader = new LibGdxLoader(data);
        loader.load(handle.file());

        drawer = new LibGdxDrawer(loader, sb, renderer);
    }

    public void draw(Player p) {
        drawer.draw(p);
    }


    public Player getSpiter(String entityName, String animationName, float scale) {
        Player player = new Player(data.getEntity(entityName));
        player.setScale(scale);
        player.setAnimation(animationName);
        return player;
    }
}
