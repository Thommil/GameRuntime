package com.thommil.libgdx.runtime.test.test_10_fine_offscreen.level;

import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * Created by thommil on 3/14/16.
 */
public class WallActor extends StaticActor{

    private boolean previousLightOn = false;
    private boolean currentLightOn = true;
    private boolean mustRedraw = true;

    private int[] lightPos = new int[2];

    public WallActor(int id, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
        super(id, textureSet, x, y, width, height, u, v, u2, v2, color);
    }

    public void switchLight(){
        this.currentLightOn = !this.currentLightOn;
    }

    public void setLightPos(final int x, final int y){
        mustRedraw = true;
        this.lightPos[0] = x;
        this.lightPos[1] = y;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        if(mustRedraw || currentLightOn != previousLightOn) {
            ((NormalSpriteRenderer)renderer).setLightPosition(this.lightPos[0],this.lightPos[1]);
            ((NormalSpriteRenderer)renderer).switchLight(this.currentLightOn);
            super.render(deltaTime, renderer);
            renderer.flush();
            this.previousLightOn = this.currentLightOn;
            mustRedraw = false;
        }
    }
}
