package com.thommil.libgdx.runtime.graphics.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Animation implementation based on TextureRegion (sprite)
 *
 * Created by thommil on 4/19/16.
 */
public class TranslateAnimation extends AbstractAnimation<Vector2> {

    private final Vector2 tmpVector = new Vector2();
    private int lastFrameNumber;
    private float lastStateTime;

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
    public TranslateAnimation(float frameDuration, Animation.PlayMode playMode, Vector2... keyFrames) {
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
    public TranslateAnimation(float frameDuration, Animation.PlayMode playMode, Interpolation interpolator, Vector2... keyFrames) {
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
    protected Vector2 calculateKeyFrame(float interpolatedStateTime) {
        /*if (keyFrames.length == 1){
            final float frameStateTime = frameNumber * this.frameDuration - ;
            this.tmpVector.set(keyFrames[frameNumber].x * frameStateTime, keyFrames[frameNumber].y * frameStateTime);
            this.tmpVector.set(keyFrames[0].x * interpolatedStateTime, keyFrames[0].y * interpolatedStateTime);
        }
        else{
            int frameNumber = (int) (interpolatedStateTime / frameDuration);
            switch (playMode) {
                case NORMAL:
                    frameNumber = Math.min(keyFrames.length - 1, frameNumber);
                    break;
                case LOOP:
                    frameNumber = frameNumber % keyFrames.length;
                    break;
                case LOOP_PINGPONG:
                    frameNumber = frameNumber % ((keyFrames.length * 2) - 2);
                    if (frameNumber >= keyFrames.length)
                        frameNumber = keyFrames.length - 2 - (frameNumber - keyFrames.length);
                    break;
                case LOOP_RANDOM:
                    int lastFrameNumber = (int) ((interpolatedStateTime) / frameDuration);
                    if (lastFrameNumber != frameNumber) {
                        frameNumber = MathUtils.random(keyFrames.length - 1);
                    } else {
                        frameNumber = this.lastFrameNumber;
                    }
                    break;
                case REVERSED:
                    frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
                    break;
                case LOOP_REVERSED:
                    frameNumber = frameNumber % keyFrames.length;
                    frameNumber = keyFrames.length - frameNumber - 1;
                    break;
            }

            lastFrameNumber = frameNumber;
            lastStateTime = interpolatedStateTime;


        }
        Gdx.app.log("",""+this.tmpVector);*/
        return this.tmpVector;
    }
}
