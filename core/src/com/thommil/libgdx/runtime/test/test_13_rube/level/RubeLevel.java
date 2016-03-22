package com.thommil.libgdx.runtime.test.test_13_rube.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.actor.physics.*;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.ViewportLayout;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RubeLoader;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

import java.util.List;

/**
 * @author  Thommil on 04/03/16.
 */
public class RubeLevel implements Disposable {

    final SpriteBatchLayer spriteBatchLayer;
    final RubeLoader rubeLoader;
    final AssetManager assetManager;
    int currentLayout = 0;

    public RubeLevel() {
        this.rubeLoader = new RubeLoader();
        this.rubeLoader.parse(Gdx.files.internal("scene/sample.json"));

        this.spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        this.assetManager = new AssetManager();
        assetManager.load("scene/crate.png",Texture.class);
        assetManager.load("scene/mcclory.png",Texture.class);
        assetManager.load("scene/tire.png",Texture.class);
        assetManager.load("scene/truck.png",Texture.class);
        assetManager.finishLoading();

        final int bodyCount = this.rubeLoader.getBodyCount();
        for(int i=0; i < bodyCount; i++){
            final RubeLoader.BodyDef bodyDef = rubeLoader.getBodyDefinition(i);
            final Array<FixtureDef> fixtureDefs = rubeLoader.getFixturesDefinition(i);
            final RubeLoader.ImageDef imageDef = this.rubeLoader.getImageDefinition(i);
            if(imageDef != null) {
                if(bodyDef.type == BodyDef.BodyType.StaticBody){
                    spriteBatchLayer.addActor(new StaticBodyActor(i, new TextureSet(assetManager.get(imageDef.file,Texture.class)),
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
                    });
                }
                else{
                    spriteBatchLayer.addActor(new SpriteBodyActor(i, new TextureSet(assetManager.get(imageDef.file,Texture.class)),imageDef.regionX,imageDef.regionY,
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
                    });
                }
            }
            else {
                spriteBatchLayer.addActor(new HeadlessBodyActor(i) {
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
        }

        Runtime.getInstance().addLayer(spriteBatchLayer);


        final Array<JointDef> joints = this.rubeLoader.getJointsDefinition();
        for(final JointDef jointDef : joints){
            final RubeLoader.WheelJointDef wheelJointDef = (RubeLoader.WheelJointDef) jointDef;
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
