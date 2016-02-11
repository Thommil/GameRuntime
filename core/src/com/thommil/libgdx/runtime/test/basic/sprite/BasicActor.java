package com.thommil.libgdx.runtime.test.basic.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.thommil.libgdx.runtime.scene.actor.SpriteActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class BasicActor extends SpriteActor {

    public BasicActor() {
        super(new Texture(Gdx.files.internal("curiosity.png")));
        this.setLayer(0);
        this.setSize(2.6f,2.3f);
        this.setCenter(0f,0f);
        this.setOriginCenter();
    }

    @Override
    public void render(float deltaTime, Batch renderer) {
        this.rotate(2);
        super.render(deltaTime,renderer);
    }
}
