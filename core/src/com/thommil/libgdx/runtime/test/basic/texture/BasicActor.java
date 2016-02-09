package com.thommil.libgdx.runtime.test.basic.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thommil.libgdx.runtime.graphics.Renderable;

/**
 * Created by tomtom on 03/02/16.
 */
public class BasicActor implements Renderable<SpriteBatch> {

    Texture texture;

    public BasicActor() {
        this.texture = new Texture(Gdx.files.internal("test.png"));
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);

    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        batch.draw(texture,-5,-2,10,4);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
