package com.thommil.libgdx.runtime.runtime.actor.physics;

import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.particles.ParticlesBatchRenderer;
import com.thommil.libgdx.runtime.runtime.actor.graphics.Renderable;
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
    protected final TextureSet textureSet;

    protected int layer = 0;

    /**
     * Default constructor without color or texture support
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     * @param particlesRadius The particles radius
     */
    public ParticleSystemActor(final int id, final int layer, final float particlesRadius) {
        this(id, layer, particlesRadius, false);
    }

    /**
     * Constructor with color support enabled (no texture)
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
        this.textureSet = null;
    }

    /**
     * Constructor with texture support enabled (no color)
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     * @param particlesRadius The particles radius
     * @param textureSet The texture set used for rendering
     */
    public ParticleSystemActor(final int id, final int layer, final float particlesRadius, final TextureSet textureSet) {
        super(id);
        this.layer = layer;
        this.particlesRadius = particlesRadius;
        this.colored = false;
        this.textureSet = textureSet;
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
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, ParticlesBatchRenderer renderer) {
        renderer.setParticlesRadius(this.getParticlesRadius());
        final float[]vertices;
        if(this.colored) {
            vertices = this.particleSystem.getParticlePositionAndColorBufferArray(true);
        }
        else{
            vertices = this.particleSystem.getParticlePositionBufferArray(true);
        }
        renderer.draw(this.textureSet, vertices, 0, vertices.length);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.particleSystem.destroyParticleSystem();
    }
}
