package com.thommil.libgdx.runtime.test.input.kinematic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.actor.SpriteActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class KinematicActor extends SpriteActor {

    boolean backward=false;
    boolean forward=false;
    boolean right=false;
    boolean left=false;
    boolean follow=false;

    float angle=0;

    public static final float STEP = 0.2f;

    private Vector2 followVec;
    private Vector2 targetVec = new Vector2();
    private Vector2 spriteVec = new Vector2();

    public KinematicActor() {
        super(new Texture(Gdx.files.internal("ship.png")));
        this.setSize(1f,1f);
        this.setOriginCenter();
        this.setCenter(0f,0f);
    }

    @Override
    public void render(float deltaTime, Batch renderer) {
        spriteVec.set(this.x,this.y);

        if(right){
            angle -= 3f;
        }
        else if(left){
            angle += 3f;
        }

        if(forward){
            this.translate(STEP*-MathUtils.sinDeg(angle),STEP*MathUtils.cosDeg(angle));
        }
        else if(backward){
            this.translate(STEP*MathUtils.sinDeg(angle),STEP*-MathUtils.cosDeg(angle));
        }
        else if(follow){
            if(this.targetVec.dst2(this.spriteVec) > 1) {
                this.translate(STEP * followVec.x, STEP * followVec.y);
            }
        }

        this.setRotation(angle);
        super.render(deltaTime,renderer);
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
