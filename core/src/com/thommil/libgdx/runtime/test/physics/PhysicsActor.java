package com.thommil.libgdx.runtime.test.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Created by tomtom on 03/02/16.
 */
public class PhysicsActor extends Actor implements Renderable {

    Sprite sprite;
    Texture texture;

    public PhysicsActor() {
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
        this.sprite.rotate(2);
        this.sprite.draw(batch);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
