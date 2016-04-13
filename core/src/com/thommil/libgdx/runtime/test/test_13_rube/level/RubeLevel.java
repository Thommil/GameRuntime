package com.thommil.libgdx.runtime.test.test_13_rube.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.physics.*;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneLoader;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * @author  Thommil on 04/03/16.
 */
public class RubeLevel implements Disposable {

    final SpriteBatchLayer spriteBatchLayer;
    final SceneLoader rubeLoader;
    final AssetManager assetManager;

    public RubeLevel() {
        this.rubeLoader = new SceneLoader();
        this.rubeLoader.parse(Gdx.files.internal("scene/sample.json"));

        this.spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        this.assetManager = new AssetManager();
        assetManager.load("scene/crate.png",Texture.class);
        assetManager.load("scene/mcclory.png",Texture.class);
        assetManager.load("scene/tire.png",Texture.class);
        assetManager.load("scene/truck.png",Texture.class);
        assetManager.finishLoading();

        int i =0;
        for(final SceneLoader.BodyDef bodyDef : this.rubeLoader.getBodiesDefintion()){
            final MassData massData = rubeLoader.getBodyMassData(i);
            final Array<FixtureDef> fixtureDefs = rubeLoader.getFixturesDefinition(i);
            final SceneLoader.ImageDef imageDef = this.rubeLoader.getImageDefinition(i);
            if(imageDef != null) {
                if(bodyDef.type == BodyDef.BodyType.StaticBody){
                    spriteBatchLayer.addActor(new StaticBodyActor(i, new TextureSet(assetManager.get(imageDef.path,Texture.class)),
                                                                    bodyDef.position.x-imageDef.width/2,bodyDef.position.y-imageDef.height/2,
                                                                    imageDef.width,imageDef.height,
                                                                    imageDef.regionX,imageDef.regionY,
                                                                    imageDef.regionWidth,imageDef.regionHeight,
                                                                    Color.WHITE.toFloatBits()){
                        @Override
                        public BodyDef getDefinition() {
                            return bodyDef;
                        }

                        @Override
                        public Array<FixtureDef> getFixturesDefinition() {
                            return fixtureDefs;
                        }

                        @Override
                        public void render(float deltaTime, SpriteBatchRenderer renderer) {
                            super.render(deltaTime, renderer);
                            Runtime.getInstance().runOnPhysicsThread(new Runnable() {
                                @Override
                                public void run() {
                                    Runtime.getInstance().getPhysicsWorld().setGravity(new Vector2(-Gdx.input.getPitch()/10, -10f));
                                }
                            });

                        }
                    });
                }
                else{
                    spriteBatchLayer.addActor(new SpriteBodyActor(i, new TextureSet(assetManager.get(imageDef.path,Texture.class)),imageDef.regionX,imageDef.regionY,
                            imageDef.regionWidth,imageDef.regionHeight) {

                        @Override
                        public BodyDef getDefinition() {
                            this.setSize(imageDef.width, imageDef.height);
                            this.setOriginCenter();
                            return bodyDef;
                        }

                        @Override
                        public Array<FixtureDef> getFixturesDefinition() {
                            return fixtureDefs;
                        }

                        @Override
                        public void setBody(Body body) {
                            if(massData != null) {
                                body.setMassData(massData);
                            }
                            super.setBody(body);
                        }
                    });
                }
            }
            else {
                spriteBatchLayer.addActor(new HeadlessBodyActor(i){
                    @Override
                    public BodyDef getDefinition() {
                        return bodyDef;
                    }

                    @Override
                    public Array<FixtureDef> getFixturesDefinition() {
                        return fixtureDefs;
                    }
                });
            }
            i++;
        }

        Runtime.getInstance().addLayer(spriteBatchLayer);


        final Array<JointDef> joints = this.rubeLoader.getJointsDefinition();
        for(final JointDef jointDef : joints){
            final SceneLoader.WheelJointDef wheelJointDef = (SceneLoader.WheelJointDef) jointDef;
            wheelJointDef.bodyA = ((RigidBody) spriteBatchLayer.getActor(wheelJointDef.bodyIdA)).getBody();
            wheelJointDef.bodyB = ((RigidBody) spriteBatchLayer.getActor(wheelJointDef.bodyIdB)).getBody();
            Runtime.getInstance().getPhysicsWorld().createJoint(wheelJointDef);
        }


        RuntimeProfiler.profile();
    }

    public void layout(){

    }

    public void resize(int width, int height){
        this.layout();
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.assetManager.dispose();
        this.spriteBatchLayer.dispose();
    }

}
