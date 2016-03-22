package com.thommil.libgdx.runtime.test.test_13_rube.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import com.thommil.libgdx.runtime.actor.physics.HeadlessBodyActor;
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

    final TextureSet layoutTextureSet;
    final SpriteBatchLayer spriteBatchLayer;
    final ViewportLayout viewportLayout;
    final RubeLoader rubeLoader;

    int currentLayout = 0;

    public RubeLevel() {
        this.rubeLoader = new RubeLoader();
        this.rubeLoader.parse(Gdx.files.internal("scene/sample.json"));
        this.layoutTextureSet = new TextureSet(new Texture("static/rube.png"));
        this.spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        this.viewportLayout = new ViewportLayout(Runtime.getInstance().getViewport());

        final int bodyCount = this.rubeLoader.getBodyCount();
        for(int i=0; i < bodyCount; i++){
            final int bodyIndex = i;
            spriteBatchLayer.addActor(new HeadlessBodyActor(i) {
                @Override
                public BodyDef getDefinition() {
                    RubeLoader.BodyDef bodyDef = rubeLoader.getBodyDefinition(bodyIndex);
                    Gdx.app.log("","Adding "+bodyDef.name);
                    return bodyDef;
                }

                @Override
                public Array<FixtureDef> getFixturesDefinition() {
                    return rubeLoader.getFixturesDefinition(bodyIndex);
                }
            });
        }

        Runtime.getInstance().addLayer(spriteBatchLayer);

        final Array<JointDef> joints = this.rubeLoader.getJointsDefinition();
        for(final JointDef jointDef : joints){
            if(jointDef instanceof RubeLoader.WheelJointDef) {
                final RubeLoader.WheelJointDef wheelJointDef = (RubeLoader.WheelJointDef) jointDef;
                wheelJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(wheelJointDef.bodyIdA)).getBody();
                wheelJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(wheelJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(wheelJointDef);
            }
            else if(jointDef instanceof RubeLoader.RevoluteJointDef) {
                final RubeLoader.RevoluteJointDef revoluteJointDef = (RubeLoader.RevoluteJointDef) jointDef;
                revoluteJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(revoluteJointDef.bodyIdA)).getBody();
                revoluteJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(revoluteJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(revoluteJointDef);
            }
            else if(jointDef instanceof RubeLoader.DistanceJointDef) {
                final RubeLoader.DistanceJointDef distanceJointDef = (RubeLoader.DistanceJointDef) jointDef;
                distanceJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(distanceJointDef.bodyIdA)).getBody();
                distanceJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(distanceJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(distanceJointDef);
            }
            else if(jointDef instanceof RubeLoader.PrismaticJointDef) {
                final RubeLoader.PrismaticJointDef prismaticJointDef = (RubeLoader.PrismaticJointDef) jointDef;
                prismaticJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(prismaticJointDef.bodyIdA)).getBody();
                prismaticJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(prismaticJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(prismaticJointDef);
            }
            else if(jointDef instanceof RubeLoader.RopeJointDef) {
                final RubeLoader.RopeJointDef ropeJointDef = (RubeLoader.RopeJointDef) jointDef;
                ropeJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(ropeJointDef.bodyIdA)).getBody();
                ropeJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(ropeJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(ropeJointDef);
            }
            else if(jointDef instanceof RubeLoader.MotorJointDef) {
                final RubeLoader.MotorJointDef motorJointDef = (RubeLoader.MotorJointDef) jointDef;
                motorJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(motorJointDef.bodyIdA)).getBody();
                motorJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(motorJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(motorJointDef);
            }
            //Not supported
            /*else if(jointDef instanceof RubeLoader.WeldJointDef) {
                final RubeLoader.WeldJointDef weldJointDef = (RubeLoader.WeldJointDef) jointDef;
                weldJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(weldJointDef.bodyIdA)).getBody();
                weldJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(weldJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(weldJointDef);
            }*/
            else if(jointDef instanceof RubeLoader.FrictionJointDef) {
                final RubeLoader.FrictionJointDef frictionJointDef = (RubeLoader.FrictionJointDef) jointDef;
                frictionJointDef.bodyA = ((HeadlessBodyActor) spriteBatchLayer.getActor(frictionJointDef.bodyIdA)).getBody();
                frictionJointDef.bodyB = ((HeadlessBodyActor) spriteBatchLayer.getActor(frictionJointDef.bodyIdB)).getBody();
                Runtime.getInstance().getPhysicsWorld().createJoint(frictionJointDef);
            }
        }

        RuntimeProfiler.profile();
    }

    public void layout(){

    }

    public void resize(int width, int height){
        this.viewportLayout.update(width, height);
        this.layout();
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.layoutTextureSet.dispose();
        this.spriteBatchLayer.dispose();
    }

}
