package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.Gdx;
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
        this.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Call this method when viewport changes, should be called at each resize()
     *
     * @param width The screen width (not the viewport one)
     * @param height The screen height (not the viewport one)
     */
    public void update(final int width, final int height){
        if(this.viewport instanceof FillViewport){
            if(viewport.getScreenX() < 0){
                this.width = width * viewport.getWorldWidth() / viewport.getScreenWidth();
                this.height = viewport.getWorldHeight();
            }
            else if(viewport.getScreenY() < 0){
                this.width = viewport.getWorldWidth();
                this.height = height* viewport.getWorldHeight() / viewport.getScreenHeight();
            }
            else{
                this.width = viewport.getWorldWidth();
                this.height = viewport.getWorldHeight();
            }
        }
        else if(viewport instanceof FitViewport){
            if(viewport.getScreenX() > 0){
                this.width = viewport.getScreenWidth() * viewport.getWorldWidth() / width;
                this.height = viewport.getWorldHeight();
            }
            else if(viewport.getScreenY() > 0){
                this.width = viewport.getWorldWidth();
                this.height = viewport.getScreenHeight() * viewport.getWorldHeight() / height;
            }
            else{
                this.width = viewport.getWorldWidth();
                this.height = viewport.getWorldHeight();
            }
        }
        else if (viewport instanceof StretchViewport){
            this.width = viewport.getWorldWidth();
            this.height = viewport.getWorldHeight();
        }
        else if(viewport instanceof ScreenViewport){
            this.width = width;
            this.height = height;
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
     * Set a Rectangle instance to the right coordinates based on alignment with margin
     *
     * @param rectangle The rectangle to set (in/out parameter)
     * @param xMargin The margin to add on left and right
     * @param yMargin The margin to add on top and bottom
     * @param hAlign The horizontal alignment (LEFT, CENTER or RIGHT)
     * @param vAlign The vertical alignment (TOP, CENTER or BOTTOM)
     */
    public void layout(final Rectangle rectangle, final Align hAlign, final Align vAlign, final float xMargin, final float yMargin){
        layout(rectangle, hAlign, vAlign, xMargin, yMargin, false, false);
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
        if(xFill){
            rectangle.x = this.left + xMargin;
            rectangle.width = this.width - 2 * xMargin;
        }
        else {
            switch (hAlign) {
                case LEFT:
                    rectangle.x = this.left + xMargin;
                    break;
                case CENTER:
                    rectangle.x = -rectangle.width / 2;
                    break;
                case RIGHT:
                    rectangle.x = this.right - rectangle.width - xMargin;
                    break;
                default:
                    throw new GameRuntimeException("bad hAlign value : "+hAlign);
            }
        }

        if(yFill){
            rectangle.y = this.bottom + yMargin;
            rectangle.height = this.height- 2 * yMargin;
        }
        else {
            switch (vAlign) {
                case TOP:
                    rectangle.y = this.top - rectangle.height - yMargin;
                    break;
                case CENTER:
                    rectangle.y = -rectangle.height/ 2;
                    break;
                case BOTTOM:
                    rectangle.y = this.bottom + yMargin;
                    break;
                default:
                    throw new GameRuntimeException("bad vAlign value : "+vAlign);
            }
        }
    }

}
