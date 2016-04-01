package com.thommil.libgdx.runtime.test.test_09_normalmap.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * @author  Thommil on 04/03/16.
 */
public class NormalMapLevel implements InputProcessor, Disposable {

    TextureSet textureSet;
    CacheLayer cacheLayer;
    NormalCacheRenderer normalCacheRenderer;

    private boolean hasInit = false;

    public NormalMapLevel() {
        CacheLayer.setGlobalSize(1);

        normalCacheRenderer = new NormalCacheRenderer(1);
        cacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 1, normalCacheRenderer);
        textureSet = new TextureSet(new Texture(Gdx.files.internal("static/weave_diffuse.png"))
                                    ,new Texture(Gdx.files.internal("static/weave_normal.png")));
        textureSet.setWrapAll(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        cacheLayer.addActor(new StaticActor(0,textureSet,-10f,-10f,20f,20f,0f,1f,1f,0f, Color.WHITE.toFloatBits()));
        Runtime.getInstance().addLayer(cacheLayer);

        Gdx.input.setInputProcessor(this);
        RuntimeProfiler.profile();
    }

    public void onResize(int width, int height){
        normalCacheRenderer.setScreenSize(width, height);
        if(!hasInit){
            normalCacheRenderer.setLightPosition(width/2, height/2);
            hasInit = true;
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        cacheLayer.dispose();
        textureSet.dispose();
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        normalCacheRenderer.setLightPosition(screenX, screenY);
        return false;
    }



    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        normalCacheRenderer.setLightPosition(screenX, screenY);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }


    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */


    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
