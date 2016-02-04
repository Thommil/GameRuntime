package com.thommil.libgdx.runtime.test.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.graphics.Renderable;

/**
 * Created by tomtom on 03/02/16.
 */
public class TextureActor extends Actor implements Renderable {


    TextureRegion textureRegion;

    float[] center={1.3f/2,1.15f/2};

    public TextureActor(Affine2 affine) {
        super(affine);
        Texture texture = new Texture(Gdx.files.internal("curiosity.png"));
        textureRegion = new TextureRegion(texture);
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, Batch batch) {
        this.readAffine.translate(center[0],center[1]);
        this.readAffine.rotate(1);
        this.readAffine.scale(1.001f,1.001f);
        this.readAffine.translate(-center[0],-center[1]);
        batch.draw(textureRegion,1.3f,1.15f, this.readAffine);
    }
}
