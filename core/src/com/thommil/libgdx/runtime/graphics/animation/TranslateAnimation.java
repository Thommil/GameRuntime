package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Animation implementation based on TextureRegion (sprite)
 *
 * Created by thommil on 4/19/16.
 */
public class TranslateAnimation extends Animation<Vector2> {

    protected int iteration = 0;

    private Vector2 translateVector;
    private Vector2 totalTranslateVector;
    private Vector2[] inversedKeyFrames;
    private int lastFrameNumber;

    /**
     * Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, Vector2... keyFrames) {
        super(frameDuration, keyFrames);
    }

    /**
     * Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, PlayMode playMode, Vector2... keyFrames) {
        super(frameDuration, playMode, keyFrames);
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
    }

    /**
     * Full constructor
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, PlayMode playMode, Interpolation interpolator, Vector2... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
    }

    /**
     * Initialize the animation
     */
    @Override
    public void initialize() {
        this.translateVector = new Vector2();
        this.totalTranslateVector = new Vector2();
        this.inversedKeyFrames = new Vector2[this.keyFrames.length];
        for(int inversedIndex=0, index = this.keyFrames.length - 1; inversedIndex < this.keyFrames.length; inversedIndex++, index--){
            this.inversedKeyFrames[inversedIndex] = new Vector2(-this.keyFrames[index].x, - this.keyFrames[index].y);
        }
    }

    /**
     * Reset animation
     */
    @Override
    public void reset() {
        this.iteration = 0;
        this.translateVector.set(0,0);
        this.totalTranslateVector.set(0,0);
        this.lastFrameNumber = (this.playMode == PlayMode.LOOP_REVERSED || this.playMode == Animation.PlayMode.REVERSED) ? this.keyFrames.length - 1 : 0;
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

        int toIndex= 0;
        switch (playMode) {
            case NORMAL:
               if(this.iteration == 0) {
                   if (keyFrames.length > 1){
                       toIndex = Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration));
                   }
                   if(this.lastFrameNumber != toIndex){
                       this.totalTranslateVector.set(0,0);
                   }
                   this.translateVector.lerp(keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                   this.translateVector.sub(this.totalTranslateVector);
                   this.totalTranslateVector.add(this.translateVector);
                   this.lastFrameNumber = toIndex;
                }
                break;
            case REVERSED:
                if(this.iteration == 0) {
                    if (keyFrames.length > 1){
                        toIndex = Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration));
                    }
                    if(this.lastFrameNumber != toIndex){
                        this.totalTranslateVector.set(0,0);
                    }
                    this.translateVector.lerp(this.inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                    this.translateVector.sub(this.totalTranslateVector);
                    this.totalTranslateVector.add(this.translateVector);
                    this.lastFrameNumber = toIndex;
                }
                break;
            case LOOP:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                }
                if(this.lastFrameNumber != toIndex){
                    this.totalTranslateVector.set(0,0);
                }
                this.translateVector.lerp(keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                this.translateVector.sub(this.totalTranslateVector);
                this.totalTranslateVector.add(this.translateVector);
                this.lastFrameNumber = toIndex;
                break;
            case LOOP_REVERSED:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                }
                if(this.lastFrameNumber != toIndex){
                    this.totalTranslateVector.set(0,0);
                }
                this.translateVector.lerp(inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                this.translateVector.sub(this.totalTranslateVector);
                this.totalTranslateVector.add(this.translateVector);
                this.lastFrameNumber = toIndex;
                break;
            case LOOP_PINGPONG:
                switch (this.iteration % 2) {
                    case 0:
                        if (keyFrames.length > 1){
                            toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                        }
                        if(this.lastFrameNumber != toIndex){
                            this.totalTranslateVector.set(0,0);
                        }
                        this.translateVector.lerp(keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                        break;
                    case 1:
                        if (keyFrames.length > 1){
                            toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                        }
                        if(this.lastFrameNumber != toIndex){
                            this.totalTranslateVector.set(0,0);
                        }
                        this.translateVector.lerp(inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                        break;
                }
                this.translateVector.sub(this.totalTranslateVector);
                this.totalTranslateVector.add(this.translateVector);
                this.lastFrameNumber = toIndex;
                break;
            default:
                throw new GameRuntimeException(playMode.toString()+" playmode not supported");

        }



        return this.translateVector;
    }
}
