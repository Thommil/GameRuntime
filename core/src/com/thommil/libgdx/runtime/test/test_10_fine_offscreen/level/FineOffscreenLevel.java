package com.thommil.libgdx.runtime.test.test_10_fine_offscreen.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.buffer.OffScreenRenderer;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.OffScreenLayer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * Demonstrates how to optimize multipass rendering using singlePass/no continuous offscreen renderer
 *
 * @author  Thommil on 04/03/16.
 */
public class FineOffscreenLevel implements InputProcessor, Disposable {

    TextureSet textureSet;
    SpriteBatchLayer spriteBatchLayer;
    NormalSpriteRenderer normalSpriteRenderer;
    OffScreenRenderer offScreenRenderer;

    public FineOffscreenLevel() {
        CacheLayer.setGlobalSize(1);

        normalSpriteRenderer = new NormalSpriteRenderer(1);
        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(), 1, normalSpriteRenderer);

        textureSet = new TextureSet(new Texture(Gdx.files.internal("static/weave_diffuse.png"))
                                    ,new Texture(Gdx.files.internal("static/weave_normal.png")));
        textureSet.setWrapAll(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        spriteBatchLayer.addActor(new WallActor(0,textureSet,-50,0,50,50,0,1,1,0, Color.WHITE.toFloatBits()));
        spriteBatchLayer.addActor(new WallActor(1,textureSet,0,0,50,50,0,1,1,0, Color.WHITE.toFloatBits()));
        spriteBatchLayer.addActor(new WallActor(2,textureSet,-50,-50,50,50,0,1,1,0, Color.WHITE.toFloatBits()));
        spriteBatchLayer.addActor(new WallActor(3,textureSet,0,-50,50,50,0,1,1,0, Color.WHITE.toFloatBits()));

        offScreenRenderer = new OffScreenRenderer(Runtime.getInstance().getViewport(), Pixmap.Format.RGB565, true, false);
        Runtime.getInstance().addLayer(new OffScreenLayer<SpriteBatchLayer>(Runtime.getInstance().getViewport(),spriteBatchLayer,offScreenRenderer));

        Gdx.input.setInputProcessor(this);
        RuntimeProfiler.profile();
    }

    public void onResize(int width, int height){
        normalSpriteRenderer.setScreenSize(width, height);
        for(Actor actor : this.spriteBatchLayer.listActors()){
            final WallActor wallActor = (WallActor)actor;
            final Vector2 lightCoords = Runtime.getInstance().getViewport().project(new Vector2(wallActor.x + 25, wallActor.y + 25));
            wallActor.setLightPos((int)lightCoords.x, (int)lightCoords.y);
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        spriteBatchLayer.dispose();
        textureSet.dispose();
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }



    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 worldCoord = Runtime.getInstance().getViewport().unproject(new Vector2(screenX, screenY));
        if(worldCoord.x < 0){
            if(worldCoord.y > 0 ) ((WallActor)this.spriteBatchLayer.getActor(0)).switchLight();
            else ((WallActor)this.spriteBatchLayer.getActor(2)).switchLight();
        }
        else{
            if(worldCoord.y > 0 ) ((WallActor)this.spriteBatchLayer.getActor(1)).switchLight();
            else ((WallActor)this.spriteBatchLayer.getActor(3)).switchLight();
        }
        this.offScreenRenderer.invalidate();
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
