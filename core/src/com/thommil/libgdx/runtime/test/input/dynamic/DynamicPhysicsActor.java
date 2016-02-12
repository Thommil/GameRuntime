package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.actor.PhysicsSpriteActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends PhysicsSpriteActor {

    private static final float STEP_FORCE = 1f;
    private static final float STEAR_FORCE = 0.1f;

    boolean backward=false;
    boolean forward=false;
    boolean right=false;
    boolean left=false;
    boolean follow=false;

    float angle=0;

    private Vector2 followVec;
    private Vector2 targetVec = new Vector2();
    private Vector2 spriteVec = new Vector2();

    public DynamicPhysicsActor() {
        super(new Texture(Gdx.files.internal("ship.png")));
        this.setLayer(0);
        this.setSize(1f,1f);
        this.setCenter(0f,0f);
        this.setOriginCenter();
    }

    @Override
    public void buildBody(World world) {
        BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        dynamicBodyDef.position.set(0f,0f);
        this.body = world.createBody(dynamicBodyDef);
        CircleShape shipShape = new CircleShape();
        shipShape.setRadius(0.4f);
        this.body.createFixture(shipShape,1f).setRestitution(0.5f);
        shipShape.dispose();
    }

    @Override
    public void step(long lastStepDuration) {
        Vector2 bodyPosition = this.body.getPosition();
        float bodyAngle = this.body.getAngle();
        spriteVec.set(bodyPosition.x,bodyPosition.y);
        if(right){
            this.body.applyTorque(-STEAR_FORCE, true);
        }
        else if(left){
            this.body.applyTorque(STEAR_FORCE, true);
        }

        if(forward){
            this.body.applyForceToCenter(STEP_FORCE*-MathUtils.sin(bodyAngle),STEP_FORCE*MathUtils.cos(bodyAngle), true);
        }
        else if(backward){
            this.body.applyForceToCenter(STEP_FORCE*MathUtils.sin(bodyAngle),STEP_FORCE*-MathUtils.cos(bodyAngle), true);
        }
        else if(follow){
            if(this.targetVec.dst2(this.spriteVec) > 1) {
                this.body.applyForceToCenter(STEP_FORCE*followVec.x,STEP_FORCE*followVec.y, true);
            }
        }

        this.setCenter(bodyPosition.x,bodyPosition.y);
        if(follow) {
            this.setRotation(angle);
        }
        else{
            this.setRotation(bodyAngle * 57.2957795f);
        }
    }

    public void forward(boolean enable){
        this.forward = enable;
    }

    public void backward(boolean enable){
        this.backward = enable;
    }

    public void left(boolean enable){
        left = enable;
    }

    public void right(boolean enable){
        right=enable;
    }

    public void follow(boolean enable){
        follow=enable;
    }

    public void target(Vector2 targetVec){
        this.targetVec.set(targetVec);
        followVec = targetVec.sub(this.x,this.y).nor();
        angle = followVec.angle() - 90f;
    }

}
