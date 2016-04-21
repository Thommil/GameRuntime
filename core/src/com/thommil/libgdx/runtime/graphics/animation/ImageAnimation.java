package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Animation implementation based on TextureRegion (sprite)
 *
 * Created by thommil on 4/19/16.
 */
public class ImageAnimation extends AbstractAnimation<TextureRegion> {

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
    public ImageAnimation(float frameDuration, Animation.PlayMode playMode, TextureRegion... keyFrames) {
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
    public ImageAnimation(float frameDuration, Animation.PlayMode playMode, Interpolation interpolator, TextureRegion... keyFrames) {
        super(frameDuration, playMode, interpolator, keyFrames);
    }

    /**
     * Main method to override in subclasses to calculate/deduce the key frame to return on getKeyFrame() calls.
     * This method is called after application of interpolator.
     *
     * @param interpolatedStateTime The interpolated state time
     * @return The key frame at wanted state time
     */
    @Override
    protected TextureRegion calculateKeyFrame(float interpolatedStateTime) {
        if (keyFrames.length == 1) return this.keyFrames[0];

        int frameNumber = 0;
        switch (playMode) {
            case NORMAL:
                switch (this.iteration){
                    case 0:
                        frameNumber = Math.min(keyFrames.length - 1, (int)(interpolatedStateTime / frameDuration));
                        break;
                    default :
                        frameNumber = keyFrames.length - 1;
                }
                break;
            case REVERSED:
                switch (this.iteration){
                    case 0:
                        frameNumber = Math.max(keyFrames.length - (int)(interpolatedStateTime / frameDuration) - 1, 0);
                        break;
                    default :
                        frameNumber = 0;
                }
                break;
            case LOOP:
                frameNumber = (int)(interpolatedStateTime / frameDuration) % keyFrames.length;
                break;
            case LOOP_REVERSED:
                frameNumber = keyFrames.length - (int)(interpolatedStateTime / frameDuration) % keyFrames.length - 1;
                break;
            case LOOP_PINGPONG:
                switch (this.iteration % 2){
                    case 0:
                        frameNumber = (int)(interpolatedStateTime / frameDuration) % keyFrames.length;
                        break;
                    case 1 :
                        frameNumber = keyFrames.length - (int)(interpolatedStateTime / frameDuration) % keyFrames.length - 1;
                        break;
                }
                break;
            case LOOP_RANDOM:
                throw new GameRuntimeException("LOOP_RANDOM playmode not supported");

        }

        return this.keyFrames[frameNumber];
    }
}
