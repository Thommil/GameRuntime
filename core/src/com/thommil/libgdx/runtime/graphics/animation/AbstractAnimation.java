package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;

/**
 * Animation base class (based on LibGDX Animation)
 *
 * @author thommil on 4/19/16.
 */
public abstract class AbstractAnimation<T> {

    final protected T[] keyFrames;

    protected float frameDuration;
    protected float animationDuration;
    protected int iteration = 0;
    protected Animation.PlayMode playMode;
    protected Interpolation interpolator;

    /**
     *  Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames the objects representing the frames.
     */
    public AbstractAnimation (final float frameDuration, T... keyFrames) {
        this(frameDuration, Animation.PlayMode.NORMAL, Interpolation.linear, keyFrames);
    }

    /**
     *  Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode The animation playmode
     * @param keyFrames the objects representing the frames.
     */
    public AbstractAnimation (final float frameDuration, final Animation.PlayMode playMode, T... keyFrames) {
        this(frameDuration, playMode, Interpolation.linear, keyFrames);
    }

    /**
     **  Simplified constructor (PlayMode NORMAL)
     *
     * @param frameDuration the time between frames in seconds.
     * @param interpolator The interpolator to use
     * @param keyFrames the objects representing the frames.
     */
    public AbstractAnimation (float frameDuration, final Interpolation interpolator , T... keyFrames) {
        this(frameDuration, Animation.PlayMode.NORMAL, interpolator, keyFrames);
    }

    /**
     *  Full constructor
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode The animation playmode
     * @param interpolator The interpolator to use
     * @param keyFrames the objects representing the frames.
     */
    public AbstractAnimation (float frameDuration, final Animation.PlayMode playMode, final Interpolation interpolator , T... keyFrames) {
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
        this.keyFrames = keyFrames;
        this.playMode = playMode;
        this.interpolator = interpolator;
    }

    /**
     * Gets the object state at a given time
     *
     * @param stateTime The time of animation state in seconds
     * @return the object state at the given time
     */
    public T getKeyFrame (float stateTime){
        this.iteration = (int)(stateTime / this.animationDuration);
        return this.calculateKeyFrame(this.interpolator.apply(0, this.animationDuration, (stateTime % this.animationDuration) / this.animationDuration));
    }

    /**
     * Main method to override in sublcass to calculate/deduce the key frame to return on getKeyFrame() calls.
     * This method is called after application of interpolator.
     *
     * @param interpolatedStateTime The interpolated state time
     *
     * @return The key frame at wanted state time
     */
    protected abstract T calculateKeyFrame(float interpolatedStateTime);

    /**
     * Gets the object state at the given index in key frames
     *
     * @param index The ly frame index
     *
     * @return the object state at the given index
     */
    public T getKeyFrame (int index){
        return this.keyFrames[index];
    }

    /** Whether the animation would be finished if played without looping (PlayMode#NORMAL), given the state time.
     * @param stateTime
     * @return whether the animation is finished. */
    public boolean isAnimationFinished (float stateTime) {
        int frameNumber = (int)(stateTime / frameDuration);
        return keyFrames.length - 1 < frameNumber;
    }

    /** Sets duration a frame will be displayed.
     * @param frameDuration in seconds */
    public void setFrameDuration (float frameDuration) {
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
    }

    /** @return the duration of a frame in seconds */
    public float getFrameDuration () {
        return frameDuration;
    }

    /** @return the duration of the entire animation, number of frames times frame duration, in seconds */
    public float getAnimationDuration () {
        return animationDuration;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
    }

    public Interpolation getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolation interpolator) {
        this.interpolator = interpolator;
    }
}
