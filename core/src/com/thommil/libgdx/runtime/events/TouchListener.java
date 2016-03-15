package com.thommil.libgdx.runtime.events;

import com.badlogic.gdx.math.Rectangle;

/**
 * Allows to bound a graphical element to touch events applied on it.
  *
 * @author  Thommil on 3/15/16.
 */
public interface TouchListener {

    /**
     * Gets the bounding rectangle of this element for touch detection
     *
     * @return The bounding Rectangle
     */
    Rectangle getBoundingRectangle();

    /**
     * Called when the element is touched or clicked down
     *
     * @param worldX The X coordinate of the event
     * @param worldY The Y coordinate of the event
     * @param button See com.badlogic.gdx.Input.Buttons
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onTouchDown(float worldX, float worldY, int button);

    /**
     * Called when the element is untouched or clicked up
     *
     * @param worldX The X coordinate of the event
     * @param worldY The Y coordinate of the event
     * @param button See com.badlogic.gdx.Input.Buttons
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onTouchUp(float worldX, float worldY, int button);

    /**
     * Called when the element receives mouse (down) or touch move event
     *
     * @param worldX The X coordinate of the event
     * @param worldY The Y coordinate of the event
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onTouchMove(float worldX, float worldY);
}
