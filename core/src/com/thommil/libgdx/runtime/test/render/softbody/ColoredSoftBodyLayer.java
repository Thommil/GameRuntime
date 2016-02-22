package com.thommil.libgdx.runtime.test.render.softbody;

import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.layer.ParticlesBatchLayer;

/**
 * Created by tomtom on 20/02/16.
 */
public class ColoredSoftBodyLayer extends ParticlesBatchLayer {

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
        ((ColoredSoftBodyBatch)this.renderer).addAttributeColors(((ColoredSoftbodyActor)renderable).getColors());
    }
}
