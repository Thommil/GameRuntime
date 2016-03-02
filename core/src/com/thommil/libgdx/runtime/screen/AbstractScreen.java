package com.thommil.libgdx.runtime.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;

/**
 * Created by tomtom on 29/02/16.
 */
public abstract class AbstractScreen implements Screen {

    /**
     * Viewport of the screen
     */
    protected Viewport viewport;

    /**
     * Default constructor
     *
     * @param viewport reference to the game viewport to use
     */
    public AbstractScreen(final Viewport viewport){
        this.viewport = viewport;
    }

}
