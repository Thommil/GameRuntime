package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.batch.ParticleSystemBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.SoftBody;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particle system ator container
 *
 * Created by thommil on 14/02/16.
 */
public abstract class SoftBodyActor implements SoftBody, Renderable<ParticleSystemBatch> {

    public ParticleSystem particleSystem;

    protected int layer = 0;

    protected final int id;

    protected final Texture texture;

    public SoftBodyActor(Texture texture) {
        this(MathUtils.random(0x7ffffffe),texture);
    }

    public SoftBodyActor(final int id, Texture texture) {
        this.id = id;
        this.texture = texture;
    }

    public SoftBodyActor(TextureRegion region) {
        this(MathUtils.random(0x7ffffffe),region);
    }

    public SoftBodyActor(final int id, TextureRegion region) {
        this.id = id;
        this.texture = region.getTexture();
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    @Override
    public int getLayer() {
        return this.layer;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, ParticleSystemBatch renderer) {
        this.particleSystem.getParticlePositionBuffer();
        //renderer.draw(this.texture, getVertices(), 0, SPRITE_SIZE);
    }

    /**
     * Set body instance of the Collidable
     *
     * @param particleSystem
     */
    @Override
    public void setBody(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    @Override
    public void step(long lastStepDuration) {
        //TODO Align vertices
    }

    /**
     * Gets the definition of SoftBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public abstract ParticleSystemDef getDefinition();

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.particleSystem.destroyParticleSystem();
    }
}
