package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.*;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Helper class used to lay out components on the Viewport
 *
 * @author Thommil on 3/17/16.
 */
public class ViewportLayout {

    private final Viewport viewport;

    public float width, height;
    public float top , right, bottom, left;

    /**
     * Alignement definition
     */
    public enum Align{
        TOP,
        RIGHT,
        BOTTOM,
        LEFT,
        CENTER,
        NONE
    }

    /**
     * Default constructor
     *
     * @param viewport The viewport to use for layout
     */
    public ViewportLayout(final Viewport viewport){
        this.viewport = viewport;
    }

    /**
     * Call this method when viewport changes, should be called at each resize()
     *
     * @param width The screen width (not the viewport one)
     * @param height The screen height (not the viewport one)
     */
    public void update(final int width, final int height){
        if(this.viewport instanceof FillViewport){
            if(this.viewport.getScreenX() < 0){
                this.width = width * this.viewport.getWorldWidth() / this.viewport.getScreenWidth();
                this.height = viewport.getWorldHeight();
            }
            else if(viewport.getScreenY() < 0){
                this.width = viewport.getWorldWidth();
                this.height = height* this.viewport.getWorldHeight() / this.viewport.getScreenHeight();
            }
            else{
                this.width = this.viewport.getWorldWidth();
                this.height = this.viewport.getWorldHeight();
            }
        }
        else if(this.viewport instanceof FitViewport){
            if(this.viewport.getScreenX() > 0){
                this.width = this.viewport.getScreenWidth() * this.viewport.getWorldWidth() / width;
                this.height = this.viewport.getWorldHeight();
            }
            else if(this.viewport.getScreenY() > 0){
                this.width = this.viewport.getWorldWidth();
                this.height = this.viewport.getScreenHeight() * this.viewport.getWorldHeight() / height;
            }
            else{
                this.width = this.viewport.getWorldWidth();
                this.height = this.viewport.getWorldHeight();
            }
        }
        else if (this.viewport instanceof StretchViewport){
            this.width = this.viewport.getWorldWidth();
            this.height = this.viewport.getWorldHeight();
        }
        else if(this.viewport instanceof ScreenViewport){
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
        this.layout(rectangle, hAlign, vAlign, 0, 0, false, false);
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
        this.layout(rectangle, hAlign, vAlign, xMargin, yMargin, false, false);
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
        this.layout(rectangle, hAlign, vAlign, 0, 0, xFill, yFill);
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
            }
        }
    }

    /**
     * Modifies the point in parameter from World coordinates to scaled
     * ones adapted to the current screen and viewport. This method has sens only
     * if the world size is limited by the viewport size c.
     *
     * @param point IN/OUT, point to transform
     */
    public void adapt(final Vector2 point){
        point.x = point.x * this.width / this.viewport.getWorldWidth();
        point.y = point.y * this.height / this.viewport.getWorldHeight();
    }

    /**
     * Modifies the list of points in parameter from World coordinates to scaled
     * ones adapted to the current screen and viewport. This method has sens only
     * if the world size is limited by the viewport size (FILL).
     *
     * @param points IN/OUT, list of points to transform
     */
    public void adapt(final Vector2[] points){
        final float worldHeight = this.viewport.getWorldHeight();
        final float worldWidth = this.viewport.getWorldWidth();
        for(final Vector2 point : points){
            point.x = point.x * this.width / worldWidth;
            point.y = point.y * this.height / worldHeight;
        }
    }

    /**
     * Modifies the list of points in parameter from World coordinates to scaled
     * ones adapted to the current screen and viewport. This method has sens only
     * if the world size is limited by the viewport size (FILL).
     *
     * @param points IN/OUT, list of points to transform [x,y,x1,Y1...]
     */
    public void adapt(final float[] points){
        final float worldHeight = this.viewport.getWorldHeight();
        final float worldWidth = this.viewport.getWorldWidth();
        for(int index=0; index < points.length; index+=2){
            points[index] = points[index] * this.width / worldWidth;
            points[index+1] = points[index+1] * this.height / worldHeight;
        }
    }

    /**
     * Adapt a point from a reference screen (DEV) to current screen
     *
     * @param referenceScreen The reference screen width and height
     * @param point The point to adapt
     */
    public static void adaptToScreen(final Vector2 referenceScreen, final Vector2 point){
        point.x = point.x * Gdx.graphics.getWidth() / referenceScreen.x;
        point.y = point.y * Gdx.graphics.getHeight() / referenceScreen.y;
    }

}
