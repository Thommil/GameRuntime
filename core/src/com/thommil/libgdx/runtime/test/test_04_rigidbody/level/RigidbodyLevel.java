package com.thommil.libgdx.runtime.test.test_04_rigidbody.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.actor.physics.SpriteBodyActor;
import com.thommil.libgdx.runtime.actor.physics.StaticBodyActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Thommil on 04/03/16.
 */
public class RigidbodyLevel implements InputProcessor, Disposable {

    TextureSet groundTextureSet;
    CacheLayer cacheLayer;

    TextureSet curiosityTextureSet;
    SpriteBatchLayer curiosityLayer;

    List<Actor> actors = new ArrayList<Actor>();

    public RigidbodyLevel() {
        CacheLayer.setGlobalSize(1);
        groundTextureSet = new TextureSet(new Texture("static/metal.png"));
        groundTextureSet.setWrapAll(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        cacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 1);
        cacheLayer.addActor(new GroundActor(0, groundTextureSet,-100f,-50f,200f,10f,0f,1f,20f,0f, Color.WHITE.toFloatBits()));
        Runtime.getInstance().addLayer(cacheLayer);

        SpriteBatchLayer.setGlobalSize(1000);
        curiosityTextureSet = new TextureSet(new Texture("sprites/curiosity.png"));
        curiosityLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(), 100);
        Runtime.getInstance().addLayer(curiosityLayer);

        RuntimeProfiler.profile();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.cacheLayer.dispose();
        this.curiosityLayer.dispose();
        this.groundTextureSet.dispose();
        this.curiosityTextureSet.dispose();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for(int i =0; i<10; i++){
            final CuriosityActor curiosityActor = new CuriosityActor(this.curiosityTextureSet);
            this.actors.add(curiosityActor);
            this.curiosityLayer.addActor(curiosityActor);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        final Actor actor = this.actors.remove(0);
        this.curiosityLayer.removeActor(actor);
        return false;
    }


    public static class GroundActor extends StaticBodyActor{

        public GroundActor(int id, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
            super(id, textureSet, x, y, width, height, u, v, u2, v2, color);
        }

        @Override
        public Array<FixtureDef> getFixturesDefinition() {
            Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
            PolygonShape groundBodyShape = new PolygonShape();
            groundBodyShape.setAsBox(this.width/2,this.height/2, new Vector2(this.width/2,this.height/2),0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1.0f;
            fixtureDef.shape = groundBodyShape;
            fixtureDefs.add(fixtureDef);
            return fixtureDefs;
        }

    }

    public static class CuriosityActor extends SpriteBodyActor {

        public CuriosityActor(TextureSet textureSet) {
            super(MathUtils.random(0x7ffffffe), textureSet);
            this.setSize(2.6f,2.3f);
            this.setOriginCenter();
            this.setPosition(MathUtils.random(-20f,20f),MathUtils.random(100,150f));
            this.setRotation(MathUtils.random(0f,90f));
        }

        @Override
        public Array<FixtureDef> getFixturesDefinition() {
            Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
            PolygonShape dynamicPolygonShape = new PolygonShape();
            dynamicPolygonShape.setAsBox(1f,1f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1.0f;
            fixtureDef.shape = dynamicPolygonShape;
            fixtureDefs.add(fixtureDef);
            return fixtureDefs;
        }
    }


    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */


    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
