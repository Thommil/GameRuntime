package com.thommil.libgdx.runtime.actor.physics;

import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.particles.ParticlesBatchRenderer;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particles system actor container
 *
 * @author thommil on 03/02/16.
 */
public abstract class ParticleSystemActor extends Actor implements ParticlesBody, Renderable<ParticlesBatchRenderer> {

    public static final int VERTEX_SIZE = 2;
    public static final int COLORED_VERTEX_SIZE  = VERTEX_SIZE + 4;

    public ParticleSystem particleSystem;

    protected float density = 1f;
    protected final float particlesRadius;
    protected final boolean colored;
    protected final TextureSet textureSet;

    /**
     * Default constructor without color or texture support
     *
     * @param id The ID of the Actor in the scene
     * @param particlesRadius The particles radius
     */
    public ParticleSystemActor(final int id, final float particlesRadius) {
        this(id, particlesRadius, false, null);
    }

    /**
     * Constructor with color support (no texture)
     *
     * @param id The ID of the Actor in the scene
     * @param particlesRadius The particles radius
     * @param colored If true, the colored are sent to the renderer
     */
    public ParticleSystemActor(final int id, final float particlesRadius, boolean colored) {
        this(id, particlesRadius, colored, null);
    }

    /**
     * Constructor with texture support (no color)
     *
     * @param id The ID of the Actor in the scene
     * @param particlesRadius The particles radius
     * @param textureSet The texture set used for rendering
     */
    public ParticleSystemActor(final int id, final float particlesRadius, final TextureSet textureSet) {
        this(id, particlesRadius, false, textureSet);
    }

    /**
     * Full constructor with texture and color support
     *
     * @param id The ID of the Actor in the scene
     * @param particlesRadius The particles radius
     * @param textureSet The texture set used for rendering
     */
    public ParticleSystemActor(final int id, final float particlesRadius, boolean colored, final TextureSet textureSet) {
        super(id);
        this.particlesRadius = particlesRadius;
        this.colored = colored;
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
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    public ParticleSystemDef getDefinition() {
        final ParticleSystemDef particleSystemDef = new ParticleSystemDef();
        particleSystemDef.radius = this.getParticlesRadius();
        particleSystemDef.density = this.getDensity();
        return particleSystemDef;
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
     * Get the collidable body
     */
    @Override
    public ParticleSystem getBody() {
        return this.particleSystem;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, ParticlesBatchRenderer renderer) {
        if(this.particleSystem != null) {
            renderer.setParticlesRadius(this.getParticlesRadius());
            final float[] vertices;
            if (this.colored) {
                vertices = this.particleSystem.getParticlePositionAndColorBufferArray(true);
            } else {
                vertices = this.particleSystem.getParticlePositionBufferArray(true);
            }
            renderer.draw(this.textureSet, vertices, 0, vertices.length);
        }
    }
}
