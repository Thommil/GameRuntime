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
public class TranslateAnimation extends Animation<TranslateAnimation.KeyFrame> {

    protected int iteration = 0;

    private TranslateAnimation.KeyFrame translateVector;
    private TranslateAnimation.KeyFrame totalTranslateVector;
    private TranslateAnimation.KeyFrame[] inversedKeyFrames;

    protected int lastIteration=0;
    private int lastFrameNumber;

    /**
     * Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, TranslateAnimation.KeyFrame... keyFrames) {
        super(frameDuration, keyFrames);
    }

    /**
     * Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, PlayMode playMode, TranslateAnimation.KeyFrame... keyFrames) {
        super(frameDuration, playMode, keyFrames);
    }

    /**
     * *  Simplified constructor (PlayMode NORMAL)
     *
     * @param frameDuration the time between frames in seconds.
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public TranslateAnimation(float frameDuration, Interpolation interpolator, TranslateAnimation.KeyFrame... keyFrames) {
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
    public TranslateAnimation(float frameDuration, PlayMode playMode, Interpolation interpolator, TranslateAnimation.KeyFrame... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
    }

    /**
     * Initialize the animation
     */
    @Override
    public void initialize() {
        this.iteration = 0;
        this.lastIteration = 0;
        this.translateVector = new TranslateAnimation.KeyFrame();
        this.totalTranslateVector = new TranslateAnimation.KeyFrame();
        this.inversedKeyFrames = new TranslateAnimation.KeyFrame[this.keyFrames.length];
        for(int inversedIndex=0, index = this.keyFrames.length - 1; inversedIndex < this.keyFrames.length; inversedIndex++, index--){
            this.inversedKeyFrames[inversedIndex] = new TranslateAnimation.KeyFrame(-this.keyFrames[index].x, - this.keyFrames[index].y, this.keyFrames[index].interpolation);
        }
    }

    /**
     * Reset animation
     */
    @Override
    public void reset() {
        this.iteration = 0;
        this.lastIteration = 0;
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
    public TranslateAnimation.KeyFrame getKeyFrame(float stateTime) {
        this.translateVector.set(0,0);
        this.iteration = (int)(stateTime / this.animationDuration);
        final float interpolatedStateTime = this.interpolator.apply(0, this.animationDuration, (stateTime % this.animationDuration) / this.animationDuration);

        int toIndex= 0;
        switch (playMode) {
            case NORMAL:
               if(this.iteration == 0) {
                   if (keyFrames.length > 1){
                       toIndex = Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration));
                       if(this.lastFrameNumber != toIndex){
                           this.totalTranslateVector.set(0,0);
                       }
                   }
                   this.translateVector.interpolate(keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, keyFrames[toIndex].interpolation);
                   this.translateVector.sub(this.totalTranslateVector);
                   this.totalTranslateVector.add(this.translateVector);
                   this.lastFrameNumber = toIndex;
                }
                break;
            case REVERSED:
                if(this.iteration == 0) {
                    if (keyFrames.length > 1){
                        toIndex = Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration));
                        if(this.lastFrameNumber != toIndex){
                            this.totalTranslateVector.set(0,0);
                        }
                    }
                    this.translateVector.interpolate(this.inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, inversedKeyFrames[toIndex].interpolation);
                    this.translateVector.sub(this.totalTranslateVector);
                    this.totalTranslateVector.add(this.translateVector);
                    this.lastFrameNumber = toIndex;
                }
                break;
            case LOOP:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.totalTranslateVector.set(0,0);
                    }
                }
                else if(lastIteration != iteration){
                    this.totalTranslateVector.set(0,0);
                }
                this.translateVector.interpolate(keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, keyFrames[toIndex].interpolation);
                this.translateVector.sub(this.totalTranslateVector);
                this.totalTranslateVector.add(this.translateVector);
                this.lastFrameNumber = toIndex;
                break;
            case LOOP_REVERSED:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.totalTranslateVector.set(0,0);
                    }
                }
                else if(lastIteration != iteration){
                    this.totalTranslateVector.set(0,0);
                }
                this.translateVector.interpolate(inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, inversedKeyFrames[toIndex].interpolation);
                this.translateVector.sub(this.totalTranslateVector);
                this.totalTranslateVector.add(this.translateVector);
                this.lastFrameNumber = toIndex;
                break;
            case LOOP_PINGPONG:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.totalTranslateVector.set(0,0);
                    }
                }
                else if(lastIteration != iteration){
                    this.totalTranslateVector.set(0,0);
                }
                if(this.iteration % 2 == 0){
                    this.translateVector.interpolate(keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, keyFrames[toIndex].interpolation);
                }
                else{
                    this.translateVector.interpolate(inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, inversedKeyFrames[toIndex].interpolation);
                }
                this.translateVector.sub(this.totalTranslateVector);
                this.totalTranslateVector.add(this.translateVector);
                this.lastFrameNumber = toIndex;
                break;
            default:
                throw new GameRuntimeException(playMode.toString()+" playmode not supported");

        }

        this.lastIteration = iteration;


        return this.translateVector;
    }

    /**
     * Defines a keyframe for a TranslateAnimation
     */
    public static class KeyFrame extends Vector2{

        final public Interpolation interpolation;

        /**
         * Constructs a new keyframe at (0,0) and linear interpolation
         */
        public KeyFrame() {
            super();
            this.interpolation = Interpolation.linear;
        }

        /**
         * Constructs a keyframe with the given components and linear interpolation
         *
         * @param x The x-component
         * @param y The y-component
         */
        public KeyFrame(float x, float y) {
            super(x, y);
            this.interpolation = Interpolation.linear;
        }

        /**
         * Constructs a keyframe from the given vector and linear interpolation
         *
         * @param v The vector
         */
        public KeyFrame(Vector2 v) {
            super(v);
            this.interpolation = Interpolation.linear;
        }

        /**
         * Constructs a keyframe with the given components and interpolation
         *
         * @param x The x-component
         * @param y The y-component
         * @param interpolation The interpolation used for this keyframe
         */
        public KeyFrame(final float x, final float y, final Interpolation interpolation) {
            super(x, y);
            this.interpolation = interpolation;
        }

        /**
         * Constructs a vector from the given vector and interpolation
         *
         * @param v The vector
         * @param interpolation The interpolation used for this keyframe
         */
        public KeyFrame(Vector2 v, final Interpolation interpolation) {
            super(v);
            this.interpolation = interpolation;
        }
    }
}
