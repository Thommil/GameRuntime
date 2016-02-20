package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.graphics.Color;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.layer.SoftBodyBatchLayer;

/**
 * Created by tomtom on 20/02/16.
 */
public class ColoredSoftBodyLayer extends SoftBodyBatchLayer {

    public ColoredSoftBodyLayer(int maxParticles) {
        super(maxParticles, new ColoredSoftBodyBatch(maxParticles));
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    @Override
    public void add(Renderable renderable) {
        super.add(renderable);
        ((ColoredSoftBodyBatch)this.renderer).addAttributeColors(((SoftbodyRenderActor)renderable).getColors());
    }
}
