package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.math.Affine2;

/**
 * Base element of a Scene, represents any entity contained in Scene.
 *
 * Created by thommil on 01/02/16.
 */
public abstract class Actor {

    /**
     * Write transformation matrix used to modify the actor (physics only)
     */
    public Affine2 writeAffine = new Affine2();

    /**
     * Read transformation matrix used to render/test the actor (render only)
     */
    public Affine2 readAffine = new Affine2();

    /**
     * Inner transformation matrix used for switch
     */
    private Affine2 tmpAffine = new Affine2();

    /**
     * Default constructor using initial transformation matrix
     *
     * @param affine The initial transformation matrix
     */
    public Actor(final Affine2 affine){
        this.readAffine.set(affine);
        this.writeAffine.set(affine);
    }

    /**
     * Switch between write <-> read Affines
     * <br/><br/>Call this method before rendering so write affine becomes the
     * read affine. This technique allows to go on updating write affine while reading
     * previous update now on read Affine.
     */
    public void switchAffine(){
        this.tmpAffine = this.readAffine;
        this.readAffine = this.writeAffine;
        this.writeAffine = this.tmpAffine;
    }
}
