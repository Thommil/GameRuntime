package com.thommil.libgdx.runtime.test.test_06_input.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.physics.SpriteBodyActor;
import com.thommil.libgdx.runtime.actor.physics.StaticBodyActor;
import com.thommil.libgdx.runtime.actor.physics.Stepable;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Thommil on 04/03/16.
 */
public class InputLevel implements InputProcessor, Disposable {

    TextureSet groundTextureSet;
    TextureSet planetTextureSet;
    TextureSet shipActorTextureSet;

    SpriteBatchLayer spriteBatchLayer;
    CacheLayer cacheLayer;

    public InputLevel() {
        CacheLayer.setGlobalSize(10);
        cacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 10);

        planetTextureSet = new TextureSet(new Texture("sprites/planet.png"));
        for(int i=0; i < 6; i++) {
            cacheLayer.addActor(new PlanetActor(i, planetTextureSet, i%6, MathUtils.random(-15f, 15f), MathUtils.random(-15f, 15f), MathUtils.random(1f, 5f)));
        }

        groundTextureSet = new TextureSet(new Texture("static/metal.png"));
        groundTextureSet.setWrapAll(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        cacheLayer.addActor(new GroundActor(6, groundTextureSet,-20f,-20f,1f,40f,0f,20f,1f,0f, Color.WHITE.toFloatBits()));
        cacheLayer.addActor(new GroundActor(7, groundTextureSet,-20f,-20f,40f,1f,0f,1f,20f,0f, Color.WHITE.toFloatBits()));
        cacheLayer.addActor(new GroundActor(8, groundTextureSet,19f,-20f,1f,40f,0f,20f,1f,0f, Color.WHITE.toFloatBits()));
        cacheLayer.addActor(new GroundActor(9, groundTextureSet,-20f,19f,40f,1f,0f,1f,20f,0f, Color.WHITE.toFloatBits()));
        Runtime.getInstance().addLayer(cacheLayer);

        SpriteBatchLayer.setGlobalSize(1);
        shipActorTextureSet = new TextureSet(new Texture("sprites/ship.png"));
        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),1);
        spriteBatchLayer.addActor(new ShipActor(shipActorTextureSet));
        Runtime.getInstance().addLayer(spriteBatchLayer);

        RuntimeProfiler.profile();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.cacheLayer.dispose();
        this.spriteBatchLayer.dispose();
        this.groundTextureSet.dispose();
        this.planetTextureSet.dispose();
        this.shipActorTextureSet.dispose();
    }



    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 target = Runtime.getInstance().getViewport().unproject(new Vector2(screenX,screenY));
        ShipActor shipActor = ((ShipActor)this.spriteBatchLayer.getActor(0));
        shipActor.setTargetPosition(target.x, target.y);
        shipActor.setMoving(true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        ShipActor shipActor = ((ShipActor)this.spriteBatchLayer.getActor(0));
        shipActor.setMoving(false);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 target = Runtime.getInstance().getViewport().unproject(new Vector2(screenX,screenY));
        ShipActor shipActor = ((ShipActor)this.spriteBatchLayer.getActor(0));
        shipActor.setTargetPosition(target.x, target.y);
        shipActor.setMoving(true);
        return false;
    }

    public static class ShipActor extends SpriteBodyActor implements Stepable{

        private final Vector2 tmpVec = new Vector2();
        private final Vector2 moveVec = new Vector2();
        private boolean moving = false;

        public ShipActor(TextureSet textureSet) {
            super(0, textureSet);
            this.setSize(2.6f,2.3f);
            this.setOriginCenter();
        }

        @Override
        public List<Shape> getShapes() {
            List<Shape> shapes = new ArrayList<Shape>();
            CircleShape shape = new CircleShape();
            shape.setRadius(0.9f);
            shapes.add(shape);
            return shapes;
        }

        public void setTargetPosition(final float x, final float y){
            this.tmpVec.set(x, y);
            this.moveVec.set(this.tmpVec.sub(this.x, this.y)).nor();

        }

        public void setMoving(final boolean moving){
            this.moving = moving;
        }

        @Override
        public void step(float deltaTime) {
            if(moving) {
                this.body.applyLinearImpulse(moveVec , this.body.getWorldCenter(), true);
            }
        }
    }


    public static class GroundActor extends StaticBodyActor{

        public GroundActor(int id, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
            super(id, textureSet, x, y, width, height, u, v, u2, v2, color);
        }

        @Override
        public List<Shape> getShapes() {
            List<Shape> shapes = new ArrayList<Shape>();
            PolygonShape groundBodyShape = new PolygonShape();
            groundBodyShape.setAsBox(this.width/2,this.height/2, new Vector2(this.width/2,this.height/2),0);
            shapes.add(groundBodyShape);
            return shapes;
        }
    }

    public static class PlanetActor extends StaticBodyActor{

        private float radius;
        private static float textW = 1/6f;

        public PlanetActor(int id, TextureSet textureSet, int type,  float x, float y, float radius) {
            super(id, textureSet, x, y, radius*2, radius*2, type*textW, 1, (type+1)*textW, 0, Color.WHITE.toFloatBits());
            this.radius = radius;
        }

        @Override
        public List<Shape> getShapes() {
            List<Shape> shapes = new ArrayList<Shape>();
            CircleShape planetShape = new CircleShape();
            planetShape.setRadius(radius);
            shapes.add(planetShape);
            return shapes;
        }

        /**
         * Gets the definition of Collidable.
         *
         * @return definition The collidable definition (settings)
         */
        @Override
        public BodyDef getDefinition() {
            final BodyDef bodyDef = super.getDefinition();
            bodyDef.position.set(this.x + this.radius, this.y + radius);
            return bodyDef;
        }
    }



    @Override
    public boolean keyDown(int keycode) {

        return false;
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
