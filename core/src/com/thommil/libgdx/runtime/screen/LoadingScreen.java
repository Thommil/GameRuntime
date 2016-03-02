package com.thommil.libgdx.runtime.screen;

import com.badlogic.gdx.assets.AssetErrorListener;

/**
 * Screens implementing this interface can listen for loading events
 *
 * @author  Thommil on 02/03/16.
 */
public interface LoadingScreen extends AssetErrorListener {

    /**
     * Indicates the loading progress (0.0 - 1.0)
     *
     * @param progress The loading progress
     */
    void onLoadProgress(final float progress);
}
