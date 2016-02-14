package com.thommil.libgdx.runtime.test.basic.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Created by tomtom on 03/02/16.
 */
public class BasicActor implements Renderable<SpriteBatch> {

    Texture texture;

    public BasicActor() {
        this.texture = new Texture(Gdx.files.internal("test.png"));
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        //1/2x1/2
        //batch.draw(texture,-4f,-4f,8f,8f,0f,0.5f,0.5f,0f,Color.WHITE.toFloatBits());
        //1x1
        //batch.draw(texture,-4f,-4f,8f,8f,0f,1f,1f,0f,Color.WHITE.toFloatBits());
        //2x2
        //batch.draw(texture,-4f,-4f,8f,8f,0f,2f,2f,0f,Color.WHITE.toFloatBits());
        //4x4
        //batch.draw(texture,-4f,-4f,8f,8f,0f,4f,4f,0f,Color.WHITE.toFloatBits());
        //2x1
        //batch.draw(texture,-4f,-4f,8f,8f,0f,1f,2f,0f,Color.WHITE.toFloatBits());
        //1x2
        batch.draw(texture,-4f,-4f,8f,8f,0f,2f,1f,0f,Color.WHITE.toFloatBits());
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
