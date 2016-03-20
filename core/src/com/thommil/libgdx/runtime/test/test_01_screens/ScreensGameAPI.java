package com.thommil.libgdx.runtime.test.test_01_screens;

import com.badlogic.gdx.assets.AssetManager;

public interface ScreensGameAPI {

    void load();

    float getLoadingProgress();

    void onLoad();
}
