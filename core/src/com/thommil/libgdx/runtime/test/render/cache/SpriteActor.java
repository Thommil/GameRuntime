package com.thommil.libgdx.runtime.test.render.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thommil.libgdx.runtime.graphics.Renderable;

/**
 * Created by tomtom on 03/02/16.
 */
public class SpriteActor implements Renderable<SpriteBatch> {

    Sprite sprite;
    Texture texture;

    public SpriteActor() {
        this.texture = new Texture(Gdx.files.internal("curiosity.png"));
        this.sprite = new Sprite(texture);
        this.sprite.setSize(5.2f,4.6f);
        this.sprite.setCenter(0f,0f);
        this.sprite.setOriginCenter();
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        //Gdx.app.debug("SpriteActor","render()");
        this.sprite.rotate(2);
        this.sprite.draw(batch);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
