package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.math.Interpolation;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Animation implementation based on interpolated vector translation
 *
 * Created by thommil on 4/19/16.
 */
public class RotateAnimation extends Animation<RotateAnimation.KeyFrame> {

    protected int iteration = 0;

    private RotateAnimation.KeyFrame rotateKeyFrame;
    private RotateAnimation.KeyFrame lastKeyFrame;
    private RotateAnimation.KeyFrame tmpKeyFrame;
    private RotateAnimation.KeyFrame[] inversedKeyFrames;

    protected int lastIteration=0;
    private int lastFrameNumber;

    /**
     * Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the objects representing the frames.
     */
    public RotateAnimation(float frameDuration, RotateAnimation.KeyFrame... keyFrames) {
        super(frameDuration, keyFrames);
    }

    /**
     * Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param keyFrames     the objects representing the frames.
     */
    public RotateAnimation(float frameDuration, PlayMode playMode, RotateAnimation.KeyFrame... keyFrames) {
        super(frameDuration, playMode, keyFrames);
    }

    /**
     * *  Simplified constructor (PlayMode NORMAL)
     *
     * @param frameDuration the time between frames in seconds.
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public RotateAnimation(float frameDuration, Interpolation interpolator, RotateAnimation.KeyFrame... keyFrames) {
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
    public RotateAnimation(float frameDuration, PlayMode playMode, Interpolation interpolator, RotateAnimation.KeyFrame... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
    }

    /**
     * Initialize the animation
     */
    @Override
    public void initialize() {
        this.iteration = 0;
        this.lastIteration = 0;
        this.rotateKeyFrame = new RotateAnimation.KeyFrame();
        this.tmpKeyFrame = new RotateAnimation.KeyFrame();
        this.lastKeyFrame = new RotateAnimation.KeyFrame();
        this.inversedKeyFrames = new RotateAnimation.KeyFrame[this.keyFrames.length];
        for(int inversedIndex=0, index = this.keyFrames.length - 1; inversedIndex < this.keyFrames.length; inversedIndex++, index--){
            this.inversedKeyFrames[inversedIndex] = new RotateAnimation.KeyFrame(360f - this.keyFrames[index].angle, this.keyFrames[index].interpolation);
        }
    }

    /**
     * Reset animation
     */
    @Override
    public void reset() {
        this.iteration = 0;
        this.lastIteration = 0;
        this.rotateKeyFrame.angle=0;
        this.tmpKeyFrame.angle=0;
        this.lastKeyFrame.angle=0;
        this.lastFrameNumber = (this.playMode == PlayMode.LOOP_REVERSED || this.playMode == PlayMode.REVERSED) ? this.keyFrames.length - 1 : 0;
    }

    /**
     * Gets the object state at a given time
     *
     * @param stateTime The time of animation state in seconds
     * @return the object state at the given time
     */
    @Override
    public RotateAnimation.KeyFrame getKeyFrame(float stateTime) {
        this.rotateKeyFrame.angle=0;
        this.iteration = (int)(stateTime / this.animationDuration);
        final float interpolatedStateTime = this.interpolator.apply(0, this.animationDuration, (stateTime % this.animationDuration) / this.animationDuration);

        int toIndex= 0;
        switch (playMode) {
            case NORMAL:
               if(this.iteration == 0) {
                   if (keyFrames.length > 1){
                       toIndex = Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration));
                       if(this.lastFrameNumber != toIndex){
                           this.lastKeyFrame.angle=0;
                       }
                   }
                   this.rotateKeyFrame.angle = keyFrames[toIndex].angle * keyFrames[toIndex].interpolation.apply((interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                }
                else return this.rotateKeyFrame;
                break;
            case REVERSED:
                if(this.iteration == 0) {
                    if (keyFrames.length > 1){
                        toIndex = Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration));
                        if(this.lastFrameNumber != toIndex){
                            this.lastKeyFrame.angle=0;
                        }
                    }
                    this.rotateKeyFrame.angle = inversedKeyFrames[toIndex].angle * inversedKeyFrames[toIndex].interpolation.apply((interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                }
                else return this.rotateKeyFrame;
                break;
            case LOOP:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.lastKeyFrame.angle=0;
                    }
                }
                else if(lastIteration != iteration){
                    this.lastKeyFrame.angle=0;
                }
                this.rotateKeyFrame.angle = keyFrames[toIndex].angle * keyFrames[toIndex].interpolation.apply((interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                break;
            case LOOP_REVERSED:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.lastKeyFrame.angle=0;
                    }
                }
                else if(lastIteration != iteration){
                    this.lastKeyFrame.angle=0;
                }
                this.rotateKeyFrame.angle = inversedKeyFrames[toIndex].angle * inversedKeyFrames[toIndex].interpolation.apply((interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                break;
            case LOOP_PINGPONG:
                if (keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / frameDuration) % keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.lastKeyFrame.angle=0;
                    }
                }
                else if(lastIteration != iteration){
                    this.lastKeyFrame.angle=0;
                }
                if(this.iteration % 2 == 0){
                    this.rotateKeyFrame.angle = keyFrames[toIndex].angle * keyFrames[toIndex].interpolation.apply((interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                }
                else{
                    this.rotateKeyFrame.angle = inversedKeyFrames[toIndex].angle * inversedKeyFrames[toIndex].interpolation.apply((interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration);
                }
                break;
            default:
                throw new GameRuntimeException(playMode.toString()+" playmode not supported");

        }

        this.tmpKeyFrame.angle = this.rotateKeyFrame.angle;
        this.rotateKeyFrame.angle =  this.rotateKeyFrame.angle - this.lastKeyFrame.angle;
        this.lastKeyFrame.angle = this.tmpKeyFrame.angle;
        this.lastFrameNumber = toIndex;
        this.lastIteration = iteration;

        return this.rotateKeyFrame;
    }

    /**
     * Defines a keyframe for a RotateAnimation
     */
    public static class KeyFrame{

        public float angle;
        final public Interpolation interpolation;

        /**
         * Constructor with no rotation angle and LINEAR interpolation
         */
        public KeyFrame() {
            this(0, Interpolation.linear);
        }

        /**
         * Constructor with LINEAR interpolation
         *
         * @param angle The roation angle in degrees
         */
        public KeyFrame(float angle) {
            this(angle, Interpolation.linear);
        }

        /**
         * Full constructor
         *
         * @param angle The roation angle in degrees
         * @param interpolation The interpolation to use
         */
        public KeyFrame(final float angle, final Interpolation interpolation) {
            this.angle = angle;
            this.interpolation = interpolation;
        }
    }
}
