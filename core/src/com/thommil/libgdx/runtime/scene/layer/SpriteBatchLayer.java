package com.thommil.libgdx.runtime.scene.layer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderer;

/**
 * Basic Sprite layer using SpriteBatchRenderer or Batch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class SpriteBatchLayer extends Layer{

    final protected Renderer renderer;

    /**
     * Default constructor
     *
     * @param initialCapacity The initial capacity of the layer
     */
    public SpriteBatchLayer(final int initialCapacity) {
        super(initialCapacity);
        this.renderer = new SpriteBatchRenderer(initialCapacity);
    }

    /**
     * Constructor with custom renderer
     *
     * @param initialCapacity The initial capacity of the layer
     * @param customRenderer The custom renderer to use
     */
    public SpriteBatchLayer(final int initialCapacity, final Renderer customRenderer) {
        super(initialCapacity);
        this.renderer = customRenderer;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(this.renderer instanceof SpriteBatchRenderer) {
            ((SpriteBatchRenderer)renderer).setCombinedMatrix(this.camera.combined);
        }
        else if(this.renderer instanceof Batch) {
            ((Batch)renderer).setProjectionMatrix(this.camera.projection);
            ((Batch)renderer).setTransformMatrix(this.camera.view);
        }
        this.renderer.begin();
        for(Renderable renderable : this.renderables){
            renderable.render(deltaTime,this.renderer);
        }
        this.renderer.end();
    }

    /**
     * Called when layer is showed for subclasses
     */
    @Override
    protected void onShow() {
        //NOP
    }

    /**
     * Called when layer is hidden
     */
    @Override
    protected void onHide() {
        //NOP
    }

    /**
     * Called when layer is resized
     *
     * @param width
     * @param height
     */
    @Override
    protected void onResize(int width, int height) {
        //NOP
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        renderer.dispose();
    }
}
