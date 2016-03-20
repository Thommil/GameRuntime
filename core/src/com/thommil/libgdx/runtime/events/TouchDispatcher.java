package com.thommil.libgdx.runtime.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Settings;

/**
 * Event dispatcher for touch events, this class is accessed as a Singleton across application.
 *
 * @author Thommil on 3/15/16.
 */
public class TouchDispatcher implements InputProcessor{

    private Viewport viewport;

    private InputProcessor decoratedInputProcessor;

    private final Array<TouchListener> listeners;

    private final Vector2 screenVec = new Vector2();

    /**
     * Full constructor with decorator pattern on InputProcessor
     *
     * @param viewport The viewport owning the listeners
     * @param inputProcessor The decorated InputProcessor
     */
    public TouchDispatcher(final Viewport viewport){
        this.viewport = viewport;
        this.listeners = new Array<TouchListener>(false,64);
        this.bind();
    }

    /**
     * Binds the TouchDispatcher to the core InputProcessor
     */
    public void bind(){
        if(Gdx.input.getInputProcessor() != this) {
            this.decoratedInputProcessor = Gdx.input.getInputProcessor();
        }
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Add a touch listener to the dispatcher
     *
     * @param touchListener The TouchListener to add
     */
    public void addListener(final TouchListener touchListener){
        this.listeners.add(touchListener);
    }

    /**
     * Clear all listeners from the dispatcher
     */
    public void clear(){
        this.listeners.clear();
    }

    /**
     * Remove a listener from the dispatcher
     *
     * @param touchListener The TouchListener to remove
     */
    public void removeListener(final TouchListener touchListener){
        this.listeners.removeValue(touchListener, false);
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.keyDown(keycode) : false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.keyUp(keycode) : false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.keyTyped(character) : false;
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
        screenVec.set(screenX, screenY);
        final Vector2 worldVec = viewport.unproject(screenVec);
        for(final TouchListener touchListener : this.listeners){
            if(touchListener.getBoundingRectangle().contains(worldVec)){
                if(touchListener.onTouchDown(worldVec.x, worldVec.y, button)){
                    return true;
                }
            }
        }
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.touchDown(screenX,screenY,pointer,button) : false;
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
        this.screenVec.set(screenX, screenY);
        final Vector2 worldVec = this.viewport.unproject(this.screenVec);
        for(final TouchListener touchListener : this.listeners){
            if(touchListener.getBoundingRectangle().contains(worldVec)){
                if(touchListener.onTouchUp(worldVec.x, worldVec.y, button)){
                    return true;
                }
            }
        }
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.touchDown(screenX,screenY,pointer,button) : false;
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
        this.screenVec.set(screenX, screenY);
        final Vector2 worldVec = this.viewport.unproject(this.screenVec);
        for(final TouchListener touchListener : this.listeners){
            if(touchListener.getBoundingRectangle().contains(worldVec)){
                if(touchListener.onTouchMove(worldVec.x, worldVec.y)){
                    return true;
                }
            }
        }
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.touchDragged(screenX, screenY, pointer) : false;
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
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.mouseMoved(screenX, screenY) : false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return (this.decoratedInputProcessor != null) ? this.decoratedInputProcessor.scrolled(amount) : false;
    }
}
