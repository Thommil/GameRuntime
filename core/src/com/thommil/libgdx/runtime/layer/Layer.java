package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.actor.physics.Collidable;
import com.thommil.libgdx.runtime.actor.physics.ParticlesBody;
import com.thommil.libgdx.runtime.actor.physics.RigidBody;
import com.thommil.libgdx.runtime.actor.physics.Stepable;
import com.thommil.libgdx.runtime.Runtime;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Represents a rendering layer (mainly for batch purpose)
 *
 * @author thommil on 03/02/16.
 */
public abstract class Layer implements Disposable {

    /**
     * Inner reference to the currently bound Runtime.
     * When null, the layer is not bound and can be rendered directly.
     */
    protected Runtime runtime;

    /**
     * Inner Actors list
     */
    protected final IntMap<Actor> actors;

    /**
     * Inner Renderable list
     */
    protected final Array<Renderable> renderables;

    /**
     * Inner Collidables list
     */
    protected final Array<Collidable> collidables;

    /**
     * Inner Stepable list
     */
    protected final Array<Stepable> stepables;

    /**
     * The viewport bound to the layer
     */
    protected final Viewport viewport;

    /**
     * The hidden state of the layer
     */
    protected boolean hidden = true;

    /**
     * Default constructor
     *
     * @param viewport The viewport bound to the layer
     * @param initialCapacity The initial capacity of the layer
     */
    public Layer(final Viewport viewport, final int initialCapacity) {
        this.runtime = null;
        this.viewport = viewport;
        this.actors = new IntMap<Actor>(initialCapacity);
        this.renderables = new Array<Renderable>(false, initialCapacity);
        this.collidables= new Array<Collidable>(false, initialCapacity);
        this.stepables = new Array<Stepable>(false, initialCapacity);
    }

    private void setCollidablesState(final Collidable collidable, final boolean active){
        if(collidable instanceof RigidBody){
            ((RigidBody)collidable).getBody().setActive(active);
        }
        else if(collidable instanceof ParticlesBody){
            ((ParticlesBody)collidable).getBody().setPaused(!active);
        }
    }

    private void bindCollidable(final Collidable collidable){
        if(collidable instanceof RigidBody){
            final RigidBody rigidBody = (RigidBody)collidable;
            final Body body = Layer.this.runtime.getPhysicsWorld().createBody(rigidBody.getDefinition());
            for(final Shape shape : rigidBody.getShapes()){
                body.createFixture(shape, rigidBody.getDensity());
                shape.dispose();
            }
            rigidBody.setBody(body);
        }
        else if(collidable instanceof ParticlesBody){
            final ParticlesBody particlesBody = (ParticlesBody)collidable;
            final ParticleSystem particleSystem = new ParticleSystem(Layer.this.runtime.getPhysicsWorld(),particlesBody.getDefinition());
            particlesBody.setBody(particleSystem);
        }
    }

    private void unbindCollidable(final Collidable collidable){
        if(collidable instanceof RigidBody){
            Layer.this.runtime.getPhysicsWorld().destroyBody(((RigidBody) collidable).getBody());
        }
        else if(collidable instanceof ParticlesBody){
            ((ParticlesBody)collidable).getBody().destroyParticleSystem();
        }
    }

    /**
     * Add an renderable to the layer
     *
     * @param renderable The renderable to add
     */
    public void addActor(final Actor actor){
        this.actors.put(actor.getId(), actor);
        //Adding when not bound
        if(this.runtime == null) {
            if (actor instanceof Renderable) {
                this.renderables.add((Renderable)actor);
            }
            if (actor instanceof Collidable) {
                this.collidables.add((Collidable)actor);
            }
            if (actor instanceof Stepable) {
                this.stepables.add((Stepable)actor);
            }
        }
        //Adding when bound
        else{
            if (actor instanceof Renderable) {
                this.runtime.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Layer.this.renderables.add((Renderable)actor);
                    }
                });
            }
            if (actor instanceof Collidable || actor instanceof Stepable) {
                this.runtime.runOnPhysicsThread(new Runnable() {
                    @Override
                    public void run() {
                        if (actor instanceof Collidable) {
                            bindCollidable((Collidable) actor);
                            Layer.this.collidables.add((Collidable) actor);
                        }
                        if (actor instanceof Stepable) {
                            Layer.this.stepables.add((Stepable) actor);
                        }
                    }
                });
            }
        }
    }

    /**
     * Gets an actor in the layer from its ID
     *
     * @param id The OD of the actor
     *
     * @return The Actor instance, null if not found
     */
    public Actor getActor(final int id){
        return this.actors.get(id);
    }

    /**
     * Gets the list of actors in the layer
     *
     * @return The list of actors in an Array
     */
    public Array<Actor> listActors(){
        return this.actors.values().toArray();
    }

    /**
     * Remove a renderable from the layer
     *
     * @param renderable The renderable to remove
     */
    public void removeActor(final Actor actor){
        this.actors.remove(actor.getId());
        //Removing when not bound
        if(this.runtime == null) {
            if (actor instanceof Renderable) {
                this.renderables.removeValue((Renderable)actor, true);
            }
            if (actor instanceof Collidable) {
                this.collidables.removeValue((Collidable)actor, true);
            }
            if (actor instanceof Stepable) {
                this.stepables.removeValue((Stepable)actor, true);
            }
        }
        //Removing when bound
        else{
            if (actor instanceof Renderable) {
                this.runtime.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Layer.this.renderables.removeValue((Renderable)actor, true);
                    }
                });
            }
            if (actor instanceof Collidable || actor instanceof Stepable) {
                this.runtime.runOnPhysicsThread(new Runnable() {
                    @Override
                    public void run() {
                        if (actor instanceof Collidable) {
                            unbindCollidable((Collidable) actor);
                            Layer.this.collidables.removeValue((Collidable)actor, true);
                        }
                        if (actor instanceof Stepable) {
                            Layer.this.stepables.removeValue((Stepable)actor, true);
                        }
                    }
                });
            }
        }
    }

    /**
     * Binds a layer to a Runtime (normaly called by the Runtime itself)
     */
    public void bind(final Runtime runtime){
        this.runtime = runtime;
        for(final Collidable collidable : this.collidables){
            bindCollidable(collidable);
        }
    }

    /**
     * Unbinds a layer from a Runtime (normaly called by the Runtime itself)
     */
    public void unbind(){
        for(final Collidable collidable : this.collidables){
            unbindCollidable(collidable);
        }
        this.runtime = null;
    }

    /**
     * Shows a layer
     */
    public final void show(){
        if(this.runtime != null && this.runtime.getSettings().physics.enabled) {
            this.runtime.runOnPhysicsThread(new Runnable() {
                @Override
                public void run() {
                    for (final Collidable collidable : collidables) {
                        setCollidablesState(collidable, true);
                    }
                    Layer.this.hidden = false;
                    onShow();
                }
            });
        }
        else {
            this.hidden = false;
            this.onShow();
        }
    }

    /**
     * Indicates if the layer is flagged as hidden
     *
     * @return Hidden state
     */
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * Hides a layer
     */
    public final void hide(){
        if(this.runtime != null && this.runtime.getSettings().physics.enabled) {
            this.runtime.runOnPhysicsThread(new Runnable() {
                @Override
                public void run() {
                    for (final Collidable collidable : Layer.this.collidables) {
                        setCollidablesState(collidable, false);
                    }
                    Layer.this.hidden = true;
                    onHide();
                }
            });
        }
        else {
            this.hidden = true;
            onHide();
        }
    }

    /**
     * Resize a layer
     */
    public final void resize(final int width, final int height){
        this.onResize(width, height);
    }

    /**
     * Step call
     *
     * @param deltaTime Time since last call since last call in seconds
     */
    public void step(float deltaTime){
        if(!this.isHidden()) {
            for (final Stepable stepable : this.stepables) {
                stepable.step(deltaTime);
            }
        }
    }

    /**
     * Called when layer is passing in visible mode.
     */
    protected abstract void onShow();

    /**
     * Called when layer is resized
     */
    protected abstract void onResize(int width, int height);

    /**
     * Called when layer is passing in hidden mode.
     */
    protected abstract void onHide();

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call in seconds
     */
    public abstract void render(float deltaTime);

}
