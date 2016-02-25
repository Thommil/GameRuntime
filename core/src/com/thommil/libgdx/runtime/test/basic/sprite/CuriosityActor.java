package com.thommil.libgdx.runtime.test.basic.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.thommil.libgdx.runtime.graphics.renderer.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class CuriosityActor extends SpriteActor {

    public CuriosityActor() {
        super(0, 0, new TextureSet(new Texture(Gdx.files.internal("curiosity.png"))));
        this.setSize(2.6f,2.3f);
        this.setCenter(0f,0f);
        this.setOriginCenter();
    }

    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        this.rotate(2);
        super.render(deltaTime,renderer);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.textureSet.dispose();
    }
}
