
package com.thommil.libgdx.runtime.test.test_08_liquid;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_03_spritebatch.level.SpriteLevel;
import com.thommil.libgdx.runtime.test.test_08_liquid.level.LiquidLevel;

/**
 * Liquid TEST
 *
 * @author  thommil on 3/4/16.
 */
public class LiquidGame extends Game{

    LiquidLevel liquidLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 10;
        settings.viewport.height = 10;
        //settings.physics.debug = true;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        liquidLevel = new LiquidLevel();
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
