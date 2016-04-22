package com.thommil.libgdx.runtime.test.test_15_animation.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.animation.*;
import com.thommil.libgdx.runtime.layer.Layer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;
import com.thommil.libgdx.runtime.tools.SceneLoader;

public class AnimationLevel implements Disposable {

    final AssetManager assetManager = new AssetManager();

    final SpriteBatchLayer spriteBatchLayer;

    final SpriteActor imageAnimationActor;
    final SpriteActor translateAnimationActor;
    final SpriteActor rotateAnimationActor;
    final SpriteActor scaleAnimationActor;
    final SpriteActor colorAnimationActor;

    float time =0;

    public AnimationLevel() {
        final SceneLoader sceneLoader = new SceneLoader();
        sceneLoader.parse(Gdx.files.internal("animation/animation.json"));
        final SceneLoader.ImageDef imageDef = sceneLoader.getImageDefinition("averagemale");
        assetManager.load(imageDef.path, Texture.class);
        assetManager.finishLoading();

        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);

        //IMAGE
        final ImageAnimation imageAnimation = (ImageAnimation)sceneLoader.getAnimation("image",assetManager);
        imageAnimationActor = new SpriteActor(0, new TextureSet(assetManager.get(imageDef.path, Texture.class))
                , imageDef.regionX, imageDef.regionY
                , imageDef.regionWidth, imageDef.regionHeight
                , imageDef.width, imageDef.height
        );
        imageAnimationActor.setPosition(-4,4);
        spriteBatchLayer.addActor(imageAnimationActor);

        //TRANSLATE
        final TranslateAnimation translateAnimation = (TranslateAnimation)sceneLoader.getAnimation("translate",assetManager);
        translateAnimationActor = new SpriteActor(1, new TextureSet(assetManager.get(imageDef.path, Texture.class))
                , imageDef.regionX, imageDef.regionY
                , imageDef.regionWidth, imageDef.regionHeight
                , imageDef.width, imageDef.height
        );
        translateAnimationActor.setPosition(-4,2);
        spriteBatchLayer.addActor(translateAnimationActor);

        //ROTATE
        final RotateAnimation rotateAnimation = (RotateAnimation)sceneLoader.getAnimation("rotate",assetManager);
        rotateAnimationActor = new SpriteActor(2, new TextureSet(assetManager.get(imageDef.path, Texture.class))
                , imageDef.regionX, imageDef.regionY
                , imageDef.regionWidth, imageDef.regionHeight
                , imageDef.width, imageDef.height
        );
        rotateAnimationActor.setPosition(-2,4);
        spriteBatchLayer.addActor(rotateAnimationActor);

        //SCALE
        final ScaleAnimation scaleAnimation = (ScaleAnimation)sceneLoader.getAnimation("scale",assetManager);
        scaleAnimationActor = new SpriteActor(3, new TextureSet(assetManager.get(imageDef.path, Texture.class))
                , imageDef.regionX, imageDef.regionY
                , imageDef.regionWidth, imageDef.regionHeight
                , imageDef.width, imageDef.height
        );
        scaleAnimationActor.setPosition(2,4);
        spriteBatchLayer.addActor(scaleAnimationActor);

        //COLOR
        final ColorAnimation colorAnimation = (ColorAnimation)sceneLoader.getAnimation("color",assetManager);
        colorAnimationActor = new SpriteActor(4, new TextureSet(assetManager.get(imageDef.path, Texture.class))
                , imageDef.regionX, imageDef.regionY
                , imageDef.regionWidth, imageDef.regionHeight
                , imageDef.width, imageDef.height
        );
        colorAnimationActor.setPosition(4,4);
        colorAnimationActor.setColor(0,0,0,0);
        spriteBatchLayer.addActor(colorAnimationActor);


        Runtime.getInstance().addLayer(spriteBatchLayer);

        Runtime.getInstance().addLayer(new Layer(Runtime.getInstance().getViewport(), 1) {
            @Override
            protected void onShow() {
                imageAnimationActor.playAnimation(imageAnimation,0);
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
                imageAnimationActor.playAnimation(imageAnimation,time);
                translateAnimationActor.playAnimation(translateAnimation, time);
                rotateAnimationActor.playAnimation(rotateAnimation, time);
                scaleAnimationActor.playAnimation(scaleAnimation, time);
                colorAnimationActor.playAnimation(colorAnimation, time);
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
        imageAnimationActor.dispose();
        translateAnimationActor.dispose();
        assetManager.dispose();
    }

}
