package com.thommil.libgdx.runtime.test.test_16_blob.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.physics.ParticleSystemActor;
import com.thommil.libgdx.runtime.actor.physics.StaticBodyActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.particles.ColoredParticlesBatchRenderer;
import com.thommil.libgdx.runtime.graphics.renderer.particles.TexturedParticlesBatchRenderer;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.OffScreenLayer;
import com.thommil.libgdx.runtime.layer.ParticlesBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;
import finnstr.libgdx.liquidfun.*;

/**
 * @author  Thommil on 04/03/16.
 */
public class BlobLevel implements InputProcessor, Disposable {

    TextureSet groundTextureSet;
    CacheLayer cacheLayer;

    BlobActor blobActor;
    ParticlesBatchLayer particlesBatchLayer;
    OffScreenLayer<ParticlesBatchLayer> offScreenLayer;

    public BlobLevel() {
        CacheLayer.setGlobalSize(4);
        groundTextureSet = new TextureSet(new Texture("static/metal.png"));
        groundTextureSet.setWrapAll(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        cacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 4);
        cacheLayer.addActor(new GroundActor(0, groundTextureSet,-20f,-20f,1f,40f,0f,20f,1f,0f, Color.WHITE.toFloatBits()));
        cacheLayer.addActor(new GroundActor(1, groundTextureSet,-20f,-20f,40f,1f,0f,1f,20f,0f, Color.WHITE.toFloatBits()));
        cacheLayer.addActor(new GroundActor(2, groundTextureSet,19f,-20f,1f,40f,0f,20f,1f,0f, Color.WHITE.toFloatBits()));
        cacheLayer.addActor(new GroundActor(3, groundTextureSet,-20f,19f,40f,1f,0f,1f,20f,0f, Color.WHITE.toFloatBits()));
        Runtime.getInstance().addLayer(cacheLayer);

        TexturedParticlesBatchRenderer texturedParticlesBatchRenderer = new TexturedParticlesBatchRenderer(2000);
        particlesBatchLayer = new ParticlesBatchLayer(Runtime.getInstance().getViewport(),2000, texturedParticlesBatchRenderer);
        particlesBatchLayer.setScaleFactor(4);
        blobActor = new BlobActor(0);
        particlesBatchLayer.addActor(blobActor);

        BlobRenderer blobRenderer = new BlobRenderer(Runtime.getInstance().getViewport());
        offScreenLayer = new OffScreenLayer<ParticlesBatchLayer>(Runtime.getInstance().getViewport(), particlesBatchLayer, blobRenderer);
        Runtime.getInstance().addLayer(offScreenLayer);

        RuntimeProfiler.profile();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.cacheLayer.dispose();
        this.offScreenLayer.dispose();
        this.particlesBatchLayer.dispose(true);
        this.groundTextureSet.dispose();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 force = Runtime.getInstance().getViewport().unproject(new Vector2(screenX,screenY));
        this.blobActor.push(force.x * 50000, 0);
        return false;
    }



    public static class GroundActor extends StaticBodyActor{

        public GroundActor(int id, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
            super(id, textureSet, x, y, width, height, u, v, u2, v2, color);
        }

        /**
         * Gets the fixtures definition of the Collidable
         */
        @Override
        public Array<FixtureDef> getFixturesDefinition() {
            Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
            PolygonShape groundBodyShape = new PolygonShape();
            groundBodyShape.setAsBox(this.width/2,this.height/2, new Vector2(this.width/2,this.height/2),0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = groundBodyShape;
            fixtureDefs.add(fixtureDef);
            return fixtureDefs;
        }
    }

    public static class BlobActor extends ParticleSystemActor {

        private ParticleGroup particleGroup;

        public BlobActor(int id) {
            super(id, 0.15f, new TextureSet(new Texture(Gdx.files.internal("effects/particle-blob.png"))));
        }

        /**
         * Gets the definition of Collidable.
         *
         * @return definition The collidable definition (settings)
         */
        @Override
        public ParticleSystemDef getDefinition() {
            final ParticleSystemDef particleSystemDef = super.getDefinition();
            particleSystemDef.strictContactCheck = false;
            particleSystemDef.density = 0.5F;
            particleSystemDef.gravityScale = 1.0F;
            particleSystemDef.pressureStrength = 0.05F;
            particleSystemDef.dampingStrength = 1.0F;
            particleSystemDef.elasticStrength = 0.25F;
            particleSystemDef.springStrength = 0.25F;
            particleSystemDef.viscousStrength = 0.2F;
            particleSystemDef.surfaceTensionPressureStrength = 0.2F;
            particleSystemDef.surfaceTensionNormalStrength = 0.2F;
            particleSystemDef.repulsiveStrength = 0.01F;
            particleSystemDef.powderStrength = 0.5F;
            particleSystemDef.ejectionStrength = 0.01F;
            particleSystemDef.staticPressureStrength = 0.2F;
            particleSystemDef.staticPressureRelaxation = 0.2F;
            particleSystemDef.staticPressureIterations = 10;
            particleSystemDef.colorMixingStrength = 0.5F;
            particleSystemDef.destroyByAge = true;
            particleSystemDef.lifetimeGranularity = 0.016666668F;
            return particleSystemDef;
        }

        /**
         * Set body instance of the Collidable
         *
         * @param particleSystem
         */
        @Override
        public void setBody(ParticleSystem particleSystem) {
            super.setBody(particleSystem);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(5,5);

            ParticleGroupDef particleGroupDef = new ParticleGroupDef();
            particleGroupDef.shape = shape;
            particleGroupDef.position.set(0,0);
            particleGroupDef.flags.add(ParticleDef.ParticleType.b2_waterParticle);
            particleGroupDef.flags.add(ParticleDef.ParticleType.b2_tensileParticle);
            particleGroupDef.flags.add(ParticleDef.ParticleType.b2_viscousParticle);
            this.particleGroup = particleSystem.createParticleGroup(particleGroupDef);

            particleGroupDef.shape.dispose();
        }

        public void push(final float x, final float y){
            this.particleGroup.applyForce(new Vector2(x,y));
        }

        @Override
        public void dispose() {
            this.textureSet.dispose();
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
