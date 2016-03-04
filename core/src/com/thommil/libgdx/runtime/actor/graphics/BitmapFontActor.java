package com.thommil.libgdx.runtime.actor.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * BitmapFont Actor
 *
 * @author Thommil on 02/03/16.
 */
public class BitmapFontActor extends Actor implements Renderable<SpriteBatchRenderer> {

    protected final BitmapFont bitmapFont;
    protected float x, y;
    protected String text;
    protected float targetWidth = 0;
    protected int halign = Align.left;
    protected boolean wrap = false;
    protected String  truncate = null;


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
     * Sets the position of the text (in pixels)
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
     * Sets container width in pixels (0 for no limit)
     */
    public void setTargetWidth(float targetWidth) {
        this.targetWidth = targetWidth;
    }

    /**
     * Sets alignment (see com.badlogic.gdx.utils.Align)
     */
    public void setHalign(int halign) {
        this.halign = halign;
    }

    /**
     * Set wrapping
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    /**
     * Sets truncate String
     */
    public void setTruncate(String truncate) {
        this.truncate = truncate;
    }

    /**
     * BitmapFont accessor
     */
    public BitmapFont getBitmapFont(){
        return this.bitmapFont;
    }

     /**
     * Render the element on current viewport
     *
     * @param deltaTime The delta time since last call in seconds
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        this.bitmapFont.draw(renderer, this.text, x, y, 0, this.text.length() , this.targetWidth, this.halign, this.wrap, this.truncate);
     }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP
    }
}
