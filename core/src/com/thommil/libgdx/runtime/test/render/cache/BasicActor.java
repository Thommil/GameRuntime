package com.thommil.libgdx.runtime.test.render.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.thommil.libgdx.runtime.graphics.Renderable;

/**
 * Created by tomtom on 03/02/16.
 */
public class BasicActor implements Renderable {

    Sprite sprite;
    Texture texture;

    public BasicActor() {
        this.texture = new Texture(Gdx.files.internal("curiosity.png"));
        this.sprite = new Sprite(texture);
        this.sprite.setSize(2.6f,2.3f);
        this.sprite.setCenter(0f,0f);
        this.sprite.setOriginCenter();
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, Batch batch) {
        //Gdx.app.debug("SpriteActor","render()");
        this.sprite.rotate(2);
        this.sprite.draw(batch);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
