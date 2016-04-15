package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * Specific layer for ParticlesEffect to allow correct ADDITIVE blending.
 *
 * @author Thommil on 3/4/16.
 */
public class ParticlesEffectBatchLayer extends SpriteBatchLayer {

    private boolean additive = true;

    /**
     * Default constructor using the default shared renderer
     *
     * @param viewport The viewport used arounf layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public ParticlesEffectBatchLayer(final Viewport viewport, int initialCapacity) {
        super(viewport, initialCapacity);
    }

    /**
     * Constructor with custom renderer
     * @param viewport The viewport used arounf layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     * @param customRenderer  The custom renderer to use
     */
    public ParticlesEffectBatchLayer(final Viewport viewport, int initialCapacity, SpriteBatchRenderer customRenderer) {
        super(viewport, initialCapacity, customRenderer);
    }

    /**
     * Indicates if current layer forces using additive blending
     *
     * @return True is additive blending is enabled
     */
    public boolean isAdditive() {
        return additive;
    }

    /**
     * Allows to force additive blending
     *
     *  @param additive true to force additive blending
     */
    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(this.additive) {
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            super.render(deltaTime);
            Gdx.gl.glBlendFunc(Runtime.getInstance().getSettings().graphics.blendSrcFunc,
                    Runtime.getInstance().getSettings().graphics.blendDstFunc);
        }
        else{
            super.render(deltaTime);
        }
    }
}
