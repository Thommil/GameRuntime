package com.thommil.libgdx.runtime.test.test_03_spritebatch.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Thommil on 04/03/16.
 */
public class SpriteLevel implements InputProcessor, Disposable {

    TextureSet textureSet;
    SpriteBatchLayer shipsLayer;
    List<Actor> actors = new ArrayList<Actor>();

    public SpriteLevel() {
        SpriteBatchLayer.setGlobalSize(1000);
        shipsLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(), 100);
        textureSet = new TextureSet(new Texture("sprites/ship.png"));
        Runtime.getInstance().addLayer(shipsLayer);
        RuntimeProfiler.profile();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.shipsLayer.dispose();
        textureSet.dispose();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(int i =0; i<10; i++){
            final ShipActor shipActor = new ShipActor(textureSet, Runtime.getInstance().getViewport().getWorldWidth() / 2);
            this.actors.add(shipActor);
            shipsLayer.addActor(shipActor);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        final Actor actor = this.actors.remove(0);
        this.shipsLayer.removeActor(actor);
        return false;
    }


    public static class ShipActor extends SpriteActor{

        private float maxPosition;

        public ShipActor(TextureSet textureSet, float maxPosition) {
            super(MathUtils.random(0x7ffffffe), textureSet, 2.6f,2.3f);
            this.maxPosition = maxPosition;
            this.setCenter(MathUtils.random(-maxPosition, maxPosition),MathUtils.random(-maxPosition, maxPosition));
        }

        /**
         * Render the element on current viewport (do access physics world here !)
         *
         * @param deltaTime The delta time since last call
         * @param renderer  The renderer to use in current layer
         */
        @Override
        public void render(float deltaTime, SpriteBatchRenderer renderer) {
            this.rotate(2);
            super.render(deltaTime, renderer);
        }
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
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
