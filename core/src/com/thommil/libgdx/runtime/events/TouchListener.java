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
     * @param button See com.badlogic.gdx.Input.Buttons
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onTouchDown(int button);

    /**
     * Called when the element is untouched or clicked up
     *
     * @param button See com.badlogic.gdx.Input.Buttons
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onTouchUp(int button);

    /**
     * Called when the mouse pointer or the finger enters in element bounding area
     *
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onMouseEnter();

    /**
     * Called when the mouse pointer or the finger leaves in element bounding area
     *
     * @return True if the event is considered as treated and stop propagation
     */
    boolean onMouseLeave();
}
