package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.*;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Helper class used to lay out components on the Viewport
 *
 * @author Thommil on 3/17/16.
 */
public class ViewportLayout {

    private final Viewport viewport;

    public float height , width;
    public float top , right, bottom, left;

    /**
     * Alignement definition
     */
    public enum Align{
        TOP,
        RIGHT,
        BOTTOM,
        LEFT,
        CENTER
    }

    /**
     * Default constructor
     *
     * @param viewport The viewport to use for layout
     */
    public ViewportLayout(final Viewport viewport){
        this.viewport = viewport;
        this.update();
    }

    /**
     * Call this method when viewport changes
     */
    public void update(){
        if(this.viewport instanceof ScreenViewport || this.viewport instanceof StretchViewport){
            this.width = viewport.getWorldWidth();
            this.height = viewport.getWorldHeight();
        }
        else if(this.viewport instanceof FillViewport){

        }
        else if(this.viewport instanceof FitViewport){

        }
        else throw new GameRuntimeException("Unsupported Viewport Class : "+this.viewport.getClass().getName());
        this.top = this.height/2;
        this.left = -this.width/2;
        this.right = -this.left;
        this.bottom = -this.top;
    }

    /**
     * Set a Rectangle instance to the right coordinates based on alignment (no margin)
     *
     * @param rectangle The rectangle to set (in/out parameter)
     * @param hAlign The horizontal alignment (LEFT, CENTER or RIGHT)
     * @param vAlign The vertical alignment (TOP, CENTER or BOTTOM)
     */
    public void layout(final Rectangle rectangle, final Align hAlign, final Align vAlign){
        layout(rectangle, hAlign, vAlign, 0, 0, false, false);
    }

    /**
     * Set a Rectangle instance to the right coordinates based on alignment and filling (no margin)
     *
     * @param rectangle The rectangle to set (in/out parameter)
     * @param hAlign The horizontal alignment (LEFT, CENTER or RIGHT)
     * @param vAlign The vertical alignment (TOP, CENTER or BOTTOM)
     * @param xFill If true, the rectangle will be stretched to fill the horizontal area
     * @param yFill If true, the rectangle will be stretched to fill the vertical area
     */
    public void layout(final Rectangle rectangle, final Align hAlign, final Align vAlign, final boolean xFill, final boolean yFill){
        layout(rectangle, hAlign, vAlign, 0, 0, xFill, yFill);
    }

    /**
     * Set a Rectangle instance to the right coordinates based on alignment, margins and filling
     *
     * @param rectangle The rectangle to set (in/out parameter)
     * @param hAlign The horizontal alignment (LEFT, CENTER or RIGHT)
     * @param vAlign The vertical alignment (TOP, CENTER or BOTTOM)
     * @param xMargin The margin to add on left and right
     * @param yMargin The margin to add on top and bottom
     * @param xFill If true, the rectangle will be stretched to fill the horizontal area
     * @param yFill If true, the rectangle will be stretched to fill the vertical area
     */
    public void layout(final Rectangle rectangle, final Align hAlign, final Align vAlign, final float xMargin, final float yMargin, final boolean xFill, final boolean yFill){

    }

}
