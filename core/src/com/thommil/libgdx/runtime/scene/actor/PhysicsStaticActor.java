package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Collidable;

/**
 * Created by tomtom on 12/02/16.
 */
public abstract class PhysicsStaticActor extends StaticActor implements Collidable{

    /**
     * The bound body
     */
    public Body body;

    public PhysicsStaticActor(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        super(texture, x, y, width, height, u, v, u2, v2);
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
        //NOP
    }

    @Override
    public void dispose() {
        super.dispose();
        this.body.getWorld().destroyBody(this.body);
    }
}
