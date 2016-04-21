package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.GameRuntimeException;

import java.util.Arrays;

/**
 * Animation implementation based on TextureRegion (sprite)
 *
 * Created by thommil on 4/19/16.
 */
public class TranslateAnimation extends AbstractAnimation<Vector2> {

    protected int iteration = 0;

    private final Vector2 translateVector = new Vector2();
    private final Vector2 totalTranslateVector = new Vector2();
    private float[] interpolatedStateTimes;
    private int lastFrameNumber;

    /**
     * Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, Vector2... keyFrames) {
        super(frameDuration, keyFrames);
        interpolatedStateTimes = new float[keyFrames.length];
        this.reset();
    }

    /**
     * Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, Animation.PlayMode playMode, Vector2... keyFrames) {
        super(frameDuration, playMode, keyFrames);
        interpolatedStateTimes = new float[keyFrames.length];
        this.reset();
    }

    /**
     * *  Simplified constructor (PlayMode NORMAL)
     *
     * @param frameDuration the time between frames in seconds.
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, Interpolation interpolator, Vector2... keyFrames) {
        super(frameDuration, interpolator, keyFrames);
        interpolatedStateTimes = new float[keyFrames.length];
        this.reset();
    }

    /**
     * Full constructor
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, Animation.PlayMode playMode, Interpolation interpolator, Vector2... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
        interpolatedStateTimes = new float[keyFrames.length];
        this.reset();
    }

    /**
     * Reset animation
     */
    @Override
    public void reset() {
        this.iteration = 0;
        this.translateVector.set(0,0);
        this.totalTranslateVector.set(0,0);
        this.lastFrameNumber = (this.playMode == Animation.PlayMode.LOOP_REVERSED || this.playMode == Animation.PlayMode.REVERSED) ? this.keyFrames.length - 1 : 0;
        for(int index = 0; index < this.interpolatedStateTimes.length ; index++){
            this.interpolatedStateTimes[index] = this.interpolator.apply(0, this.animationDuration, ((index + 1) * this.frameDuration) / this.animationDuration);
        }
    }

    /**
     * Gets the object state at a given time
     *
     * @param stateTime The time of animation state in seconds
     * @return the object state at the given time
     */
    @Override
    public Vector2 getKeyFrame(float stateTime) {
        this.translateVector.set(0,0);
        this.iteration = (int)(stateTime / this.animationDuration);
        final float interpolatedStateTime = this.interpolator.apply(0, this.animationDuration, (stateTime % this.animationDuration) / this.animationDuration);

        switch (playMode) {
            case NORMAL:
               if(this.iteration == 0) {
                   int toIndex= 0;
                   float passedTime = 0;
                   for(int index=0; index < this.interpolatedStateTimes.length; index ++){
                       if (interpolatedStateTime < this.interpolatedStateTimes[index]){
                           toIndex = index;
                           if(toIndex > 0){
                               this.translateVector.lerp(keyFrames[toIndex],(interpolatedStateTime - this.interpolatedStateTimes[toIndex])/this.frameDuration);
                               this.translateVector.sub(this.totalTranslateVector);
                               this.totalTranslateVector.add(this.translateVector);
                           }
                           else{
                               this.translateVector.lerp(keyFrames[toIndex],interpolatedStateTime/this.interpolatedStateTimes[0]);
                               this.translateVector.sub(this.totalTranslateVector);
                               this.totalTranslateVector.add(this.translateVector);
                           }
                           break;
                       }
                   }
                }
                break;
            case REVERSED:
                if(this.iteration == 0) {
                        //fromIndex = Math.max(keyFrames.length - (int) (interpolatedStateTime / frameDuration) - 1, 0);
                }
                break;
            case LOOP:
                //fromIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                break;
            case LOOP_REVERSED:
                //fromIndex = keyFrames.length - (int) (interpolatedStateTime / frameDuration) % keyFrames.length - 1;
                break;
            case LOOP_PINGPONG:
                switch (this.iteration % 2) {
                    case 0:
                        //fromIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                        break;
                    case 1:
                        //fromIndex = keyFrames.length - (int) (interpolatedStateTime / frameDuration) % keyFrames.length - 1;
                        break;
                }
                break;
            case LOOP_RANDOM:
                throw new GameRuntimeException("LOOP_RANDOM playmode not supported");

        }


        return this.translateVector;
    }
}
