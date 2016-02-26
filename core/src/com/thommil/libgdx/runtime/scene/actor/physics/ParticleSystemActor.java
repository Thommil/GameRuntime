package com.thommil.libgdx.runtime.scene.actor.physics;

import com.thommil.libgdx.runtime.graphics.renderer.particles.ParticlesBatchRenderer;
import com.thommil.libgdx.runtime.scene.ParticlesBody;
import com.thommil.libgdx.runtime.scene.Renderable;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particles system actor container
 *
 * @author thommil on 03/02/16.
 */
public abstract class ParticleSystemActor extends AbstractStepable implements ParticlesBody, Renderable<ParticlesBatchRenderer> {

    public static final int VERTEX_SIZE = 2;
    public static final int COLORED_VERTEX_SIZE  = VERTEX_SIZE + 4;

    public ParticleSystem particleSystem;

    protected float density;
    protected final float particlesRadius;
    protected final boolean colored;

    protected int layer = 0;

    /**
     * Default constructor without color support
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     * @param particlesRadius The particles radius
     */
    public ParticleSystemActor(final int id, final int layer, final float particlesRadius) {
        this(id, layer, particlesRadius, false);
    }

    /**
     * Full constructor
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     * @param particlesRadius The particles radius
     * @param colored If true, the colored are sent to the renderer
     */
    public ParticleSystemActor(final int id, final int layer, final float particlesRadius, boolean colored) {
        super(id);
        this.layer = layer;
        this.particlesRadius = particlesRadius;
        this.colored = colored;
    }

    /**
     * Gets teh radius of particles
     */
    @Override
    public float getParticlesRadius(){
        return this.particlesRadius;
    }

    /**
     * Gets the density to the particles
     */
    @Override
    public float getDensity() {
        return this.density;
    }

    /**
     * Sets the definition of Collidable, implementations
     * should configure Collidable settings in the passed
     * definition.
     *
     * @param particleSystemDef The collidable definition (settings)
     */
    @Override
    public void setDefinition(ParticleSystemDef particleSystemDef) {
        particleSystemDef.radius = this.getParticlesRadius();
        particleSystemDef.density = this.getDensity();
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
     * Sets the layer index
     *
     * @param layer The layer index
     */
    public void setLayer(final int layer){
        this.layer = layer;
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
    public void render(float deltaTime, ParticlesBatchRenderer renderer) {
        renderer.setParticlesRadius(this.getParticlesRadius());
        if(this.colored) {
            renderer.draw(this.particleSystem.getParticlePositionAndColorBufferArray(true));
        }
        else{
            renderer.draw(this.particleSystem.getParticlePositionBufferArray(true));
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.particleSystem.destroyParticleSystem();
    }
}
