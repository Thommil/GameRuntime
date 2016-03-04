package com.thommil.libgdx.runtime.test.test_02_spritecache.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * Created by tomtom on 04/03/16.
 */
public class CacheLevel implements Disposable {

    CacheLayer planetCacheLayer;

    public CacheLevel() {
        CacheLayer.setGlobalSize(60);
        planetCacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 60);
        TextureSet textureSet = new TextureSet(new Texture("sprites/planet.png"));
        for(int i = 0; i < 10; i++) {
            planetCacheLayer.addActor(new PlanetActor(textureSet, MathUtils.random(0.1f, 2f), 0));
            planetCacheLayer.addActor(new PlanetActor(textureSet, MathUtils.random(0.1f, 2f), 1));
            planetCacheLayer.addActor(new PlanetActor(textureSet, MathUtils.random(0.1f, 2f), 2));
            planetCacheLayer.addActor(new PlanetActor(textureSet, MathUtils.random(0.1f, 2f), 3));
            planetCacheLayer.addActor(new PlanetActor(textureSet, MathUtils.random(0.1f, 2f), 4));
            planetCacheLayer.addActor(new PlanetActor(textureSet, MathUtils.random(0.1f, 2f), 5));
        }
        Runtime.getInstance().addLayer(planetCacheLayer);
        RuntimeProfiler.profile();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.planetCacheLayer.dispose();
    }

    private static class PlanetActor extends StaticActor {

        private final static float xOffset = 1/6f;

        public PlanetActor(final TextureSet textureSet, final float radius, int offset) {
            super(MathUtils.random(0x7ffffffe), 0, textureSet, MathUtils.random(-4.5f,3.5f),MathUtils.random(-4.5f,3.5f),radius,radius,xOffset*offset,1f,(1+offset)*xOffset,0f, new Color(1,1,1,0.8f).toFloatBits());
        }

    }
}
