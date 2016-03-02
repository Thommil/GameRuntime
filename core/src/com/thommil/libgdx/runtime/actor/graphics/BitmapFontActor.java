package com.thommil.libgdx.runtime.actor.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * BitmapFont Actor
 *
 * @author Thommil on 02/03/16.
 */
public class BitmapFontActor extends Actor implements Renderable<SpriteBatchRenderer> {

    protected final BitmapFont bitmapFont;
    public float x, y;
    public String text;

    /**
     * Default constructor using predefined BitmapFont
     *
     * @param id The ID of the Actor
     * @param bitmapFont The BitmapFont to use
     */
    public BitmapFontActor(int id, final BitmapFont bitmapFont) {
        super(id);
        this.bitmapFont = bitmapFont;
    }

    /**
     * Sets the position of the text
     */
    public void setPosition(final float x, final float y){
        this.x = x;
        this.y = y;
    }

    /**
     * The the current text
     */
    public void setText(final String text){
        this.text = text;
    }


    /**
     * Render the element on current viewport
     *
     * @param deltaTime The delta time since last call in seconds
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        this.bitmapFont.draw(renderer, this.text, x, y);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP
    }
}
