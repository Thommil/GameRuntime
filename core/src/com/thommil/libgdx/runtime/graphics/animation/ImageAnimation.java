package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Animation implementation based on TextureRegion (sprite)
 *
 * Created by thommil on 4/19/16.
 */
public class ImageAnimation extends Animation<TextureRegion> {

    protected int iteration = 0;

    /**
     * Simplified constructor (PlayMode NORMAL and Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the objects representing the frames.
     */
    public ImageAnimation(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }

    /**
     * Simplified constructor (Linear interpolation)
     *
     * @param frameDuration the time between frames in seconds.
     * @param playMode      The animation playmode
     * @param keyFrames     the objects representing the frames.
     */
    public ImageAnimation(float frameDuration, PlayMode playMode, TextureRegion... keyFrames) {
        super(frameDuration, playMode, keyFrames);
    }

    /**
     * *  Simplified constructor (PlayMode NORMAL)
     *
     * @param frameDuration the time between frames in seconds.
     * @param interpolator  The interpolator to use
     * @param keyFrames     the objects representing the frames.
     */
    public ImageAnimation(float frameDuration, Interpolation interpolator, TextureRegion... keyFrames) {
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
    public ImageAnimation(float frameDuration, PlayMode playMode, Interpolation interpolator, TextureRegion... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
    }

    /**
     * Initialize the animation
     */
    @Override
    public void initialize() {
        this.iteration = 0;
    }

    /**
     * Reset animation
     */
    @Override
    public void reset() {
        this.iteration = 0;
    }

    /**
     * Gets the object state at a given time
     *
     * @param stateTime The time of animation state in seconds
     * @return the object state at the given time
     */
    @Override
    public TextureRegion getKeyFrame(float stateTime) {
        this.iteration = (int)(stateTime / this.animationDuration);
        final float interpolatedStateTime = this.interpolator.apply(0, this.animationDuration, (stateTime % this.animationDuration) / this.animationDuration);

        if (keyFrames.length == 1) return this.keyFrames[0];

        switch (playMode) {
            case NORMAL:
                if(this.iteration == 0) {
                    return this.keyFrames[Math.min(keyFrames.length - 1, (int) (interpolatedStateTime / frameDuration))];
                }
                return this.keyFrames[keyFrames.length - 1];
            case REVERSED:
                if(this.iteration == 0) {
                    return this.keyFrames[Math.max(keyFrames.length - (int)(interpolatedStateTime / frameDuration) - 1, 0)];
                }
                return this.keyFrames[0];
            case LOOP:
                return this.keyFrames[(int)(interpolatedStateTime / frameDuration) % keyFrames.length];
            case LOOP_REVERSED:
                return this.keyFrames[keyFrames.length - (int)(interpolatedStateTime / frameDuration) % keyFrames.length - 1];
            case LOOP_PINGPONG:
                switch (this.iteration % 2){
                    case 0:
                        return this.keyFrames[(int)(interpolatedStateTime / frameDuration) % keyFrames.length];
                    case 1 :
                        return this.keyFrames[keyFrames.length - (int)(interpolatedStateTime / frameDuration) % keyFrames.length - 1];
                }
            default:
                throw new GameRuntimeException(playMode.toString()+" playmode not supported");

        }
    }
}
