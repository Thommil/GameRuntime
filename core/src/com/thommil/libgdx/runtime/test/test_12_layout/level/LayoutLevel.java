package com.thommil.libgdx.runtime.test.test_12_layout.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.ViewportLayout;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;

/**
 * @author  Thommil on 04/03/16.
 */
public class LayoutLevel implements Disposable {

    final TextureSet layoutTextureSet;
    final SpriteBatchLayer spriteBatchLayer;
    final ViewportLayout viewportLayout;

    public LayoutLevel() {
        this.layoutTextureSet = new TextureSet(new Texture("static/layout.png"));
        this.spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        this.viewportLayout = new ViewportLayout(Runtime.getInstance().getViewport());
        this.doLayout();
        Runtime.getInstance().addLayer(this.spriteBatchLayer);
    }

    public void doLayout(){

    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.layoutTextureSet.dispose();
        this.spriteBatchLayer.dispose();
    }
}
