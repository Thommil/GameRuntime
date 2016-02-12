package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Collidable;

/**
 * Defines a dynamic SpriteActor actor which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public abstract class PhysicsSpriteActor extends SpriteActor implements Collidable{

    /**
     * The bound body
     */
    public Body body;

    /**
     * Creates a sprite with width, height, and texture region equal to the size of the texture.
     *
     * @param texture
     */
    public PhysicsSpriteActor(Texture texture) {
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
    public PhysicsSpriteActor(Texture texture, int srcWidth, int srcHeight) {
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
    public PhysicsSpriteActor(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

    /**
     * Creates a sprite based on a specific TextureRegion, the new sprite's region is a copy of the parameter region - altering one
     * does not affect the other
     *
     * @param region
     */
    public PhysicsSpriteActor(TextureRegion region) {
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
    public PhysicsSpriteActor(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
    }

    /**
     * Subclasses must implement this method to build the body
     * of this instance. The body is automatically associated to
     * the actor in the _init() method and can't be overriden.
     */
    @Override
    public abstract void buildBody(final World world);

    /**
     * @return The body of this instance
     */
    @Override
    public Body getBody() {
        return this.body;
    }

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    @Override
    public void step(long lastStepDuration){
        final float xAmount = this.body.getPosition().x - width / 2 - this.x;
        final float yAmount = this.body.getPosition().y - height / 2 - this.y;
        this.x += xAmount;
        this.y += yAmount;
        this.rotation = this.body.getAngle() * DEG_IN_RAD;
        dirty = true;

        vertices[X1] += xAmount;
        vertices[X2] += xAmount;
        vertices[X3] += xAmount;
        vertices[X4] += xAmount;
        vertices[Y1] += yAmount;
        vertices[Y2] += yAmount;
        vertices[Y3] += yAmount;
        vertices[Y4] += yAmount;
    }
}
