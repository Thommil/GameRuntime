package com.thommil.libgdx.runtime.runtime.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchAdapter;
import com.thommil.libgdx.runtime.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.runtime.layer.Layer;
import com.thommil.libgdx.runtime.runtime.layer.SpriteBatchLayer;

/**
 * Created by tomtom on 29/02/16.
 */
public class DefaultLoadingScreen extends LoadingScreen {

    private final Layer layer;
    private final LoadingMessage loadingMessage;

    public DefaultLoadingScreen(Viewport viewport, AssetManager assetManager) {
        super(viewport, assetManager);
        this.layer = new SpriteBatchLayer(10, Runtime.getInstance().getSpriteBatchAdapter());
        this.loadingMessage = new LoadingMessage();
        this.layer.add(this.loadingMessage);
    }

    @Override
    public void render(float delta, float progress) {
        this.layer.render(delta);
    }

    private class LoadingMessage implements Renderable<SpriteBatchAdapter>{

        /**
         * Render the element on current viewport (do access physics world here !)
         *
         * @param deltaTime The delta time since last call
         * @param renderer  The renderer to use in current layer
         */
        @Override
        public void render(float deltaTime, SpriteBatchAdapter renderer) {

        }
    }
}
