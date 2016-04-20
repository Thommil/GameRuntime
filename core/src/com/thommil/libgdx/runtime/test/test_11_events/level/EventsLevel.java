package com.thommil.libgdx.runtime.test.test_11_events.level;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.events.TouchDispatcher;
import com.thommil.libgdx.runtime.events.TouchListener;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * @author  Thommil on 04/03/16.
 */
public class EventsLevel implements InputProcessor,Disposable {

    final TextureSet textureSet;
    final SpriteBatchLayer spriteBatchLayer;
    final TouchDispatcher touchDispatcher;

    public EventsLevel() {
        textureSet = new TextureSet(new Texture("sprites/planet.png"));
        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        touchDispatcher = new TouchDispatcher(Runtime.getInstance().getViewport());

        spriteBatchLayer.addActor(new PlanetActor(0, textureSet));
        spriteBatchLayer.addActor(new PlanetActor(1, textureSet));
        spriteBatchLayer.addActor(new PlanetActor(2, textureSet));
        spriteBatchLayer.addActor(new PlanetActor(3, textureSet));
        spriteBatchLayer.addActor(new PlanetActor(4, textureSet));
        spriteBatchLayer.addActor(new PlanetActor(5, textureSet));

        ((PlanetActor)spriteBatchLayer.getActor(0)).setCenter(-40,25);
        touchDispatcher.addListener((PlanetActor)spriteBatchLayer.getActor(0));
        ((PlanetActor)spriteBatchLayer.getActor(1)).setCenter(0,25);
        touchDispatcher.addListener((PlanetActor)spriteBatchLayer.getActor(1));
        ((PlanetActor)spriteBatchLayer.getActor(2)).setCenter(40,25);
        touchDispatcher.addListener((PlanetActor)spriteBatchLayer.getActor(2));
        ((PlanetActor)spriteBatchLayer.getActor(3)).setCenter(-40,-25);
        touchDispatcher.addListener((PlanetActor)spriteBatchLayer.getActor(3));
        ((PlanetActor)spriteBatchLayer.getActor(4)).setCenter(0,-25);
        touchDispatcher.addListener((PlanetActor)spriteBatchLayer.getActor(4));
        ((PlanetActor)spriteBatchLayer.getActor(5)).setCenter(40,-25);
        touchDispatcher.addListener((PlanetActor)spriteBatchLayer.getActor(5));

        Runtime.getInstance().addLayer(spriteBatchLayer);

        RuntimeProfiler.profile();
    }


    public static class PlanetActor extends SpriteActor implements TouchListener {

        static int dragging = -1;

        public PlanetActor(int id, TextureSet textureSet) {
            super(id, textureSet, 0, 0, 150, 150, 20f,20f);
        }

        /**
         * Called when the element is touched or clicked down
         *
         * @param worldX The X coordinate of the event
         * @param worldY The Y coordinate of the event
         * @param button See com.badlogic.gdx.Input.Buttons
         * @return True if the event is considered as treated and stop propagation
         */
        @Override
        public boolean onTouchDown(float worldX, float worldY, int button) {
            if(this.u < 5/6f) {
                this.setRegion(this.u + 1/6f,1,this.u2 + 1/6f,0);
            }
            else{
                this.setRegion(0,1,1/6f,0);
            }
            return false;
        }


        /**
         * Called when the element receives mouse (down) or touch move event
         *
         * @param worldX The X coordinate of the event
         * @param worldY The Y coordinate of the event
         * @return True if the event is considered as treated and stop propagation
         */
        @Override
        public boolean onTouchMove(float worldX, float worldY) {
            if(dragging < 0 || dragging == this.getId()) {
                this.setCenter(worldX, worldY);
                dragging = this.getId();
            }
            return false;
        }

        /**
         * Called when the element is untouched or clicked up
         *
         * @param worldX The X coordinate of the event
         * @param worldY The Y coordinate of the event
         * @param button See com.badlogic.gdx.Input.Buttons
         * @return True if the event is considered as treated and stop propagation
         */
        @Override
        public boolean onTouchUp(float worldX, float worldY, int button) {
            dragging=-1;
            return false;
        }
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
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
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

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

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        textureSet.dispose();
        spriteBatchLayer.dispose();
    }
}
