package com.thommil.libgdx.runtime.test.input.kinematic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Created by tomtom on 03/02/16.
 */
public class SpriteActor implements Renderable<SpriteBatch> {

    Sprite sprite;
    Texture texture;

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

    public SpriteActor() {
        this.texture = new Texture(Gdx.files.internal("ship.png"));
        this.sprite = new Sprite(texture);
        this.sprite.setSize(1f,1f);
        this.sprite.setOriginCenter();
        this.sprite.setCenter(0f,0f);
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        //Gdx.app.debug("SpriteActor","render()");
        spriteVec.set(this.sprite.getX(),this.sprite.getY());

        if(right){
            angle -= 3f;
        }
        else if(left){
            angle += 3f;
        }

        if(forward){
            this.sprite.translate(STEP*-MathUtils.sinDeg(angle),STEP*MathUtils.cosDeg(angle));
        }
        else if(backward){
            this.sprite.translate(STEP*MathUtils.sinDeg(angle),STEP*-MathUtils.cosDeg(angle));
        }
        else if(follow){
            if(this.targetVec.dst2(this.spriteVec) > 1) {
                this.sprite.translate(STEP * followVec.x, STEP * followVec.y);
            }
        }

        this.sprite.setRotation(angle);
        this.sprite.draw(batch);
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
        followVec = targetVec.sub(this.sprite.getX(),this.sprite.getY()).nor();
        angle = followVec.angle() - 90f;
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
