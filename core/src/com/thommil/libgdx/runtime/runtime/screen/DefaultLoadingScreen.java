package com.thommil.libgdx.runtime.runtime.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.runtime.layer.Layer;
import com.thommil.libgdx.runtime.runtime.layer.SpriteBatchLayer;

/**
 * Created by tomtom on 29/02/16.
 */
public class DefaultLoadingScreen extends LoadingScreen {

    public DefaultLoadingScreen(Viewport viewport, AssetManager assetManager) {
        super(viewport, assetManager);
    }

    @Override
    public void render(float delta, float progress) {

    }
}
