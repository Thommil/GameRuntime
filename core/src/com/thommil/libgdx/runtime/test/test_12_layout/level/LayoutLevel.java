package com.thommil.libgdx.runtime.test.test_12_layout.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.ViewportLayout;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * @author  Thommil on 04/03/16.
 */
public class LayoutLevel implements Disposable, InputProcessor {

    final TextureSet layoutTextureSet;
    final SpriteBatchLayer spriteBatchLayer;
    final ViewportLayout viewportLayout;

    int currentLayout = 0;

    public LayoutLevel() {
        this.layoutTextureSet = new TextureSet(new Texture("static/layout.png"));
        this.spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        this.viewportLayout = new ViewportLayout(Runtime.getInstance().getViewport());

        //Top-left
        spriteBatchLayer.addActor(new SpriteActor(0,layoutTextureSet,0,0,52,52));
        //Top-Right
        spriteBatchLayer.addActor(new SpriteActor(1,layoutTextureSet,188,0,52,52));

        //Bottom-left
        spriteBatchLayer.addActor(new SpriteActor(2,layoutTextureSet,0,118,52,52));
        //Bottom-Right
        spriteBatchLayer.addActor(new SpriteActor(3,layoutTextureSet,188,118,52,52));

        //Top-Center
        spriteBatchLayer.addActor(new SpriteActor(4,layoutTextureSet,52,0,136,52));
        //Bottom-Center
        spriteBatchLayer.addActor(new SpriteActor(5,layoutTextureSet,52,118,136,52));

        //Center-Left
        spriteBatchLayer.addActor(new SpriteActor(6,layoutTextureSet,0,52,52,66));
        //Center-Right
        spriteBatchLayer.addActor(new SpriteActor(7,layoutTextureSet,188,52,52,66));

        //Middle
        spriteBatchLayer.addActor(new SpriteActor(8,layoutTextureSet,52,52,136,66));

        Runtime.getInstance().addLayer(this.spriteBatchLayer);

        Gdx.input.setInputProcessor(this);

        RuntimeProfiler.profile();
    }

    public void layout(){
        switch (this.currentLayout){
            case 0 :
                this.doLayoutNoFillNoMargin();
                break;
            case 1 :
                this.doLayoutNoFillMargin();
                break;
            case 2 :
                this.doLayoutFillNoMargin();
                break;
            case 3 :
                this.doLayoutFillMargin();
                break;
        }
    }

    public void doLayoutNoFillNoMargin(){
        SpriteActor spriteActor;
        Rectangle rectangle;

        //Top-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(0);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(1);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(2);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(3);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(4);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(5);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(6);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.CENTER);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(7);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.CENTER);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Middle
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(8);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.CENTER);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);
    }

    public void doLayoutNoFillMargin(){
        SpriteActor spriteActor;
        Rectangle rectangle;

        //Top-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(0);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.TOP,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(1);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.TOP,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(2);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.BOTTOM,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(3);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.BOTTOM,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(4);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.TOP,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(5);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.BOTTOM,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Left-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(6);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.CENTER,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(7);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.CENTER,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Middle
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(8);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.CENTER,0.25f,0.25f);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);
    }

    public void doLayoutFillNoMargin(){
        SpriteActor spriteActor;
        Rectangle rectangle;

        //Top-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(0);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(1);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(2);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(3);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(4);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.TOP, true, false);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(5);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.BOTTOM, true, false);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(6);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.CENTER, false, true);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(7);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.CENTER, false, true);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Middle
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(8);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.CENTER);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);
    }

    public void doLayoutFillMargin(){
        SpriteActor spriteActor;
        Rectangle rectangle;

        //Top-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(0);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(1);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.TOP);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(2);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(3);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.BOTTOM);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Top-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(4);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.TOP, 2, 0, true, false);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Bottom-Center
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(5);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,2);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.BOTTOM, 2, 0, true, false);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Left
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(6);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.LEFT, ViewportLayout.Align.CENTER, 0, 2, false, true);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Center-Right
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(7);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,2,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.RIGHT, ViewportLayout.Align.CENTER, 0, 2, false, true);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);

        //Middle
        spriteActor = (SpriteActor)spriteBatchLayer.getActor(8);
        rectangle = spriteActor.getBoundingRectangle();
        rectangle.set(0,0,4,4);
        this.viewportLayout.layout(rectangle, ViewportLayout.Align.CENTER, ViewportLayout.Align.CENTER, 2, 2, true, true);
        spriteActor.setSize(rectangle.width, rectangle.height);
        spriteActor.setPosition(rectangle.x, rectangle.y);
    }

    public void resize(int width, int height){
        this.viewportLayout.update(width, height);
        this.layout();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.currentLayout = (this.currentLayout + 1) % 4;
        layout();
        return false;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.layoutTextureSet.dispose();
        this.spriteBatchLayer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
