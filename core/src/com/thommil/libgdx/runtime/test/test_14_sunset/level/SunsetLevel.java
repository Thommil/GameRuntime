package com.thommil.libgdx.runtime.test.test_14_sunset.level;

import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * @author  Thommil on 04/03/16.
 */
public class SunsetLevel implements Disposable {

    final SkyLayer sunsetLayer;

    public SunsetLevel() {
        sunsetLayer = new SkyLayer(Runtime.getInstance().getViewport());
        Runtime.getInstance().addLayer(sunsetLayer);

        RuntimeProfiler.profile();
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        sunsetLayer.dispose();
    }

}
