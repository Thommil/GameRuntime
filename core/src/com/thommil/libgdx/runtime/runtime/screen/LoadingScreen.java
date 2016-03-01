package com.thommil.libgdx.runtime.runtime.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by tomtom on 29/02/16.
 */
public abstract class LoadingScreen extends AbstractScreen {

    protected final AssetManager assetManager;

    public LoadingScreen(final Viewport viewport, final AssetManager assetManager) {
        super(viewport);
        this.assetManager = assetManager;
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public final void render(float delta) {
        this.render(delta, this.assetManager.getProgress());
    }

    public abstract void render(float delta, float progress);
}
