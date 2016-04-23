package com.thommil.libgdx.runtime.actor.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.animation.ColorAnimation;
import com.thommil.libgdx.runtime.graphics.animation.ScaleAnimation;
import com.thommil.libgdx.runtime.graphics.animation.TranslateAnimation;
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
     * Play a given translate animation in current actor actor at specified state time
     *
     * @param animation The translate animation to use
     * @param stateTime The state time in seconds
     */
    public BitmapFontActor playAnimation(final TranslateAnimation animation, final float stateTime){
        final TranslateAnimation.KeyFrame translation = animation.getKeyFrame(stateTime);
        this.x += translation.x;
        this.y += translation.y;
        return this;
    }

    /**
     * Play a given scale animation in current actor actor at specified state time
     *
     * @param animation The translate animation to use
     * @param stateTime The state time in seconds
     */
    public BitmapFontActor playAnimation(final ScaleAnimation animation, final float stateTime){
        final ScaleAnimation.KeyFrame scale = animation.getKeyFrame(stateTime);
        this.bitmapFont.getData().scale(Math.max(scale.x, scale.y));
        return this;
    }

    /**
     * Play a given color animation in current actor actor at specified state time
     *
     * @param animation The translate animation to use
     * @param stateTime The state time in seconds
     */
    public BitmapFontActor playAnimation(final ColorAnimation animation, final float stateTime){
        this.bitmapFont.setColor(animation.getKeyFrame(stateTime));
        return this;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP
    }
}
