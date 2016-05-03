

package com.thommil.libgdx.runtime.test.test_16_blob;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_05_softbody.level.SoftbodyLevel;
import com.thommil.libgdx.runtime.test.test_16_blob.level.BlobLevel;

/**
 * Softbody TEST
 *
 * @author  thommil on 3/4/16.
 */
public class BlobGame extends Game{

    BlobLevel blobLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 40;
        settings.viewport.height = 40;
        settings.physics.particleIterations = 4;
        settings.physics.enabled = true;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        blobLevel = new BlobLevel();
        this.showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowRuntime() {

    }

    @Override
    protected void onHideRuntime() {

    }

    @Override
    protected void onResize(int width, int height) {

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
