package com.thommil.libgdx.runtime.test.test_15_animation.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;
import com.thommil.libgdx.runtime.tools.SceneLoader;

public class AnimationLevel implements Disposable {

    final SpriteBatchLayer spriteBatchLayer;

    final Texture animationTexture;
    final SpriteActor idleActor;

    public AnimationLevel() {
        final SceneLoader sceneLoader = new SceneLoader();
        sceneLoader.parse(Gdx.files.internal("animation/animation.json"));
        final SceneLoader.ImageDef imageDef = sceneLoader.getImageDefinition("averagemale");
        animationTexture = new Texture(Gdx.files.internal(imageDef.path));

        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);

        //IDLE
        idleActor = new SpriteActor(0, new TextureSet(animationTexture), 5f, 5f);

        spriteBatchLayer.addActor(idleActor);


        Runtime.getInstance().addLayer(spriteBatchLayer);

        RuntimeProfiler.profile();
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        spriteBatchLayer.dispose();
        animationTexture.dispose();
    }

}
