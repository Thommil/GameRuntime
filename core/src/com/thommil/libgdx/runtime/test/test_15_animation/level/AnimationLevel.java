package com.thommil.libgdx.runtime.test.test_15_animation.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.animation.TextureRegionAnimation;
import com.thommil.libgdx.runtime.layer.Layer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;
import com.thommil.libgdx.runtime.tools.SceneLoader;

public class AnimationLevel implements Disposable {

    final AssetManager assetManager = new AssetManager();

    final SpriteBatchLayer spriteBatchLayer;

    final SpriteActor idleActor;

    float time =0;

    public AnimationLevel() {
        final SceneLoader sceneLoader = new SceneLoader();
        sceneLoader.parse(Gdx.files.internal("animation/animation.json"));
        final SceneLoader.ImageDef imageDef = sceneLoader.getImageDefinition("averagemale");
        assetManager.load(imageDef.path, Texture.class);
        assetManager.finishLoading();

        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);

        //IDLE
        final TextureRegionAnimation idleAnimation = (TextureRegionAnimation)sceneLoader.getAnimation("idle",assetManager);
        idleActor = new SpriteActor(0, new TextureSet(idleAnimation.getKeyFrame(0).getTexture())
                , idleAnimation.getKeyFrame(0).getRegionX(), idleAnimation.getKeyFrame(0).getRegionY()
                , idleAnimation.getKeyFrame(0).getRegionWidth(), idleAnimation.getKeyFrame(0).getRegionHeight()
                , imageDef.width, imageDef.height
        );
        idleActor.setPosition(-4,4);
        spriteBatchLayer.addActor(idleActor);

        Runtime.getInstance().addLayer(spriteBatchLayer);

        Runtime.getInstance().addLayer(new Layer(Runtime.getInstance().getViewport(), 1) {
            @Override
            protected void onShow() {

            }

            @Override
            protected void onResize(int width, int height) {

            }

            @Override
            protected void onHide() {

            }

            @Override
            public void render(float deltaTime) {
                time+=deltaTime;
                idleActor.playAnimation(idleAnimation,time);
            }
        });

        RuntimeProfiler.profile();
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        spriteBatchLayer.dispose();
        idleActor.dispose();
        assetManager.dispose();
    }

}
