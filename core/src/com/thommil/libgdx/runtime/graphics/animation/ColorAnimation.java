package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Animation implementation based on interpolated vector translation
 *
 * Created by thommil on 4/19/16.
 */
public class ColorAnimation extends Animation<ColorAnimation.KeyFrame> {

    protected int iteration = 0;

    private ColorAnimation.KeyFrame colorKeyFrame;
    private ColorAnimation.KeyFrame lastKeyFrame;
    private ColorAnimation.KeyFrame tmpKeyFrame;
    private ColorAnimation.KeyFrame[] inversedKeyFrames;

    protected int lastIteration=0;
    private int lastFrameNumber;

    /**
     * Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the objects representing the frames.
     */
    public ColorAnimation(float frameDuration, ColorAnimation.KeyFrame... keyFrames) {
        super(frameDuration, keyFrames);
    }

    /**
     * Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param keyFrames     the objects representing the frames.
     */
    public ColorAnimation(float frameDuration, PlayMode playMode, ColorAnimation.KeyFrame... keyFrames) {
        super(frameDuration, playMode, keyFrames);
    }

    /**
     * *  Simplified constructor (PlayMode NORMAL)
     *
     * @param frameDuration the time between frames in seconds.
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public ColorAnimation(float frameDuration, Interpolation interpolator, ColorAnimation.KeyFrame... keyFrames) {
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
    public ColorAnimation(float frameDuration, PlayMode playMode, Interpolation interpolator, ColorAnimation.KeyFrame... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
    }

    /**
     * Initialize the animation
     */
    @Override
    public void initialize() {
        this.iteration = 0;
        this.lastIteration = 0;
        this.colorKeyFrame = new ColorAnimation.KeyFrame();
        this.tmpKeyFrame = new ColorAnimation.KeyFrame();
        this.lastKeyFrame = new ColorAnimation.KeyFrame();
        this.inversedKeyFrames = new ColorAnimation.KeyFrame[this.keyFrames.length];
        for(int inversedIndex=0, index = this.keyFrames.length - 1; inversedIndex < this.keyFrames.length; inversedIndex++, index--){
            this.inversedKeyFrames[inversedIndex] = new ColorAnimation.KeyFrame(1f - this.keyFrames[index].r, 1f - this.keyFrames[index].g, 1f - this.keyFrames[index].b, 1f - this.keyFrames[index].a, this.keyFrames[index].interpolation);
        }
    }

    /**
     * Reset animation
     */
    @Override
    public void reset() {
        this.iteration = 0;
        this.lastIteration = 0;
        this.colorKeyFrame.set(0,0,0,0);
        this.tmpKeyFrame.set(0,0,0,0);
        this.lastKeyFrame.set(0,0,0,0);
        this.lastFrameNumber = (this.playMode == PlayMode.LOOP_REVERSED || this.playMode == PlayMode.REVERSED) ? this.keyFrames.length - 1 : 0;
    }

    /**
     * Gets the object state at a given time
     *
     * @param stateTime The time of animation state in seconds
     * @return the object state at the given time
     */
    @Override
    public ColorAnimation.KeyFrame getKeyFrame(float stateTime) {
        this.colorKeyFrame.set(0,0,0,0);
        this.iteration = (int)(stateTime / this.animationDuration);
        final float interpolatedStateTime = this.interpolator.apply(0, this.animationDuration, (stateTime % this.animationDuration) / this.animationDuration);

        int toIndex= 0;
        switch (this.playMode) {
            case NORMAL:
               if(this.iteration == 0) {
                   if (this.keyFrames.length > 1){
                       toIndex = Math.min(this.keyFrames.length - 1, (int) (interpolatedStateTime / this.frameDuration));
                       if(this.lastFrameNumber != toIndex){
                           this.lastKeyFrame.set(0,0,0,0);
                       }
                   }

                   this.colorKeyFrame.interpolate(this.keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, this.keyFrames[toIndex].interpolation);
                }
                else{
                   return this.colorKeyFrame;
               }
                break;
            case REVERSED:
                if(this.iteration == 0) {
                    if (this.keyFrames.length > 1){
                        toIndex = Math.min(this.keyFrames.length - 1, (int) (interpolatedStateTime / this.frameDuration));
                        if(this.lastFrameNumber != toIndex){
                            this.lastKeyFrame.set(0,0,0,0);
                        }
                    }
                    this.colorKeyFrame.interpolate(this.inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, this.inversedKeyFrames[toIndex].interpolation);
                }
                else{
                    return this.colorKeyFrame;
                }
                break;
            case LOOP:
                if (this.keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / this.frameDuration) % this.keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.lastKeyFrame.set(0,0,0,0);
                    }
                }
                else if(this.lastIteration != this.iteration){
                    this.lastKeyFrame.set(0,0,0,0);
                }
                this.colorKeyFrame.interpolate(this.keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, this.keyFrames[toIndex].interpolation);
                break;
            case LOOP_REVERSED:
                if (this.keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / this.frameDuration) % this.keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.lastKeyFrame.set(0,0,0,0);
                    }
                }
                else if(this.lastIteration != this.iteration){
                    this.lastKeyFrame.set(0,0,0,0);
                }
                this.colorKeyFrame.interpolate(this.inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, this.inversedKeyFrames[toIndex].interpolation);
                break;
            case LOOP_PINGPONG:
                if (this.keyFrames.length > 1){
                    toIndex = (int) (interpolatedStateTime / this.frameDuration) % this.keyFrames.length;
                    if(this.lastFrameNumber != toIndex){
                        this.lastKeyFrame.set(0,0,0,0);
                    }
                }
                else if(this.lastIteration != this.iteration){
                    this.lastKeyFrame.set(0,0,0,0);
                }
                if(this.iteration % 2 == 0){
                    this.colorKeyFrame.interpolate(this.keyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, this.keyFrames[toIndex].interpolation);
                }
                else{
                    this.colorKeyFrame.interpolate(inversedKeyFrames[toIndex],(interpolatedStateTime - (toIndex * this.frameDuration)) / this.frameDuration, this.inversedKeyFrames[toIndex].interpolation);
                }
                break;
            default:
                throw new GameRuntimeException(this.playMode.toString()+" playmode not supported");

        }
        Gdx.app.log("",""+this.colorKeyFrame+ " "+ this.lastKeyFrame);
        this.tmpKeyFrame.set(this.colorKeyFrame.r, this.colorKeyFrame.g, this.colorKeyFrame.b, this.colorKeyFrame.a);
        this.colorKeyFrame.set(this.colorKeyFrame.r - this.lastKeyFrame.r, this.colorKeyFrame.g - this.lastKeyFrame.g, this.colorKeyFrame.b - this.lastKeyFrame.b, this.colorKeyFrame.a - this.lastKeyFrame.a);
        this.lastKeyFrame.set(this.tmpKeyFrame.r, this.tmpKeyFrame.g, this.tmpKeyFrame.b, this.tmpKeyFrame.a);
        this.lastFrameNumber = toIndex;
        this.lastIteration = this.iteration;
        Gdx.app.log("",""+this.colorKeyFrame);
        return this.colorKeyFrame;
    }

    /**
     * Defines a keyframe for a ColorAnimation
     */
    public static class KeyFrame extends Color{

        final public Interpolation interpolation;

        /**
         * Constructs a new keyframe at pure black and linear interpolation
         */
        public KeyFrame() {
            super();
            this.interpolation = Interpolation.linear;
        }


        /**
         * Constructor, sets the components of the color and linear interpolation
         *
         * @param r the red component
         * @param g the green component
         * @param b the blue component
         * @param a the alpha component
         */
        public KeyFrame(float r, float g, float b, float a) {
            super(r, g, b, a);
            this.interpolation = Interpolation.linear;
        }

        /**
         * Constructs a new keyframe using the given color and linear interpolation
         *
         * @param color the color
         */
        public KeyFrame(Color color) {
            super(color);
            this.interpolation = Interpolation.linear;
        }

        /**
         * Constructs a new keyframe at pure black and given interpolation
         *
         * @param interpolation The interpolation to use
         */
        public KeyFrame(final Interpolation interpolation) {
            super();
            this.interpolation = interpolation;
        }


        /**
         * Constructor, sets the components of the color and given interpolation
         *
         * @param r the red component
         * @param g the green component
         * @param b the blue component
         * @param a the alpha component
         * @param interpolation The interpolation to use
         */
        public KeyFrame(float r, float g, float b, float a, final Interpolation interpolation) {
            super(r, g, b, a);
            this.interpolation = interpolation;
        }

        /**
         * Constructs a new keyframe using the given color and given interpolation
         *
         * @param color the color
         * @param interpolation The interpolation to use
         */
        public KeyFrame(Color color, final Interpolation interpolation) {
            super(color);
            this.interpolation = interpolation;
        }

        public KeyFrame interpolate(KeyFrame target, float alpha, Interpolation interpolation) {
            this.lerp(target, this.interpolation.apply(alpha));
            return this;
        }
    }
}
