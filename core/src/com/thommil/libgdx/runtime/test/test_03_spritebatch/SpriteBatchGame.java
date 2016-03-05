
package com.thommil.libgdx.runtime.test.test_03_spritebatch;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_03_spritebatch.level.SpriteLevel;

/**
 * Sprite batch TEST
 *
 * @author  thommil on 3/4/16.
 */
public class SpriteBatchGame extends Game{

    SpriteLevel batchLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 40;
        settings.viewport.height = 40;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        batchLevel = new SpriteLevel();
        this.showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowScreen(final Screen screen) {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onDispose() {

    }
}
