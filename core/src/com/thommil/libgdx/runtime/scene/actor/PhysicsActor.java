package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Defines an SpriteActor actor which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public abstract class PhysicsActor extends SpriteActor {

    /**
     * The bound body
     */
    public Body body;

    /**
     * Creates a sprite with width, height, and texture region equal to the size of the texture.
     *
     * @param texture
     */
    public PhysicsActor(Texture texture) {
        super(texture);
    }

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     *
     * @param texture
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public PhysicsActor(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
    }

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size.
     *
     * @param texture
     * @param srcX
     * @param srcY
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public PhysicsActor(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

    /**
     * Creates a sprite based on a specific TextureRegion, the new sprite's region is a copy of the parameter region - altering one
     * does not affect the other
     *
     * @param region
     */
    public PhysicsActor(TextureRegion region) {
        super(region);
    }

    /**
     * Creates a sprite with width, height, and texture region equal to the specified size, relative to specified sprite's texture
     * region.
     *
     * @param region
     * @param srcX
     * @param srcY
     * @param srcWidth  The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn.
     */
    public PhysicsActor(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
    }

    /**
     * System method used by Scene.
     */
    public void _init(final World world){
        this.body = this.buildBody(world);
        this.body.setUserData(this);
    }

    /**
     * Subclasses must implement this method to build the body
     * of this instance. The body is automatically associated to
     * the actor in the _init() method and can't be overriden.
     */
    public abstract Body buildBody(final World world);

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    public void step(long lastStepDuration){
        this.setCenter(this.body.getPosition().x,this.body.getPosition().y);
        this.setRotation(this.body.getAngle()*57.2957795f);
    }
}
