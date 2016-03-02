package com.thommil.libgdx.runtime;

import com.badlogic.gdx.graphics.GL20;

/**
 * POJO class used to define settings
 *
 * @author Thommil on 02/03/2016
 */
public class Settings{

    /**
     * Core settings
     */
    public final Core core = new Core();

    /**
     * Viewport settings
     */
    public final Viewport viewport = new Viewport();

    /**
     * Physics settings
     */
    public final Physics physics = new Physics();

    /**
     * Graphics settings
     */
    public final Graphics graphics = new Graphics();

    /**
     * User Data object for passing data across screens and levels
     */
    public Object userData = null;

    /**
     * Core settings class
     */
    public static class Core{

    }

    public static class Viewport{
        public static final int SCREEN = 0;
        public static final int FILL = 1;
        public static final int FIT = 2;
        public static final int STRECTCH = 3;

        /**
         * View type among SCREEN, FILL, FIT or STRETCH
         */
        public int type = SCREEN;

        /**
         * Viewport width (in world units), not applied in SCREEN mode
         */
        public float width = 640;

        /**
         * Viewport height (in world units), not applied in SCREEN mode
         */
        public float height = 480;
    }

    public static class Graphics{
        /**
         * Indicates if the Runtime must clear color buffer
         */
        public boolean clearScreen = true;

        /**
         * Color buffer clear color
         */
        public float[] clearColor = {0f, 0f, 0f, 1f};

        /**
         * Allows to enabled depth mask
         */
        public boolean depthMaskEnabled = false;

        /**
         * Allows to disabled blending
         */
        public boolean blendEnabled = true;

        /**
         * Default blending SRC function
         */
        public int blendSrcFunc = GL20.GL_SRC_ALPHA;

        /**
         * Default blending DST function
         */
        public int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    }

    public static class Physics{
        /**
         * Allows to disabled physics engine
         */
        public boolean enabled = true;

        /**
         * Maximum tasks executed before each step (runOnPhysicsThread() calls)
         */
        public int maxTasks = 10;

        /**
         * Default gravity settings
         */
        public float[] gravity = {0.0f,-9.8f};

        /**
         * Physics steps frequency (in ms)
         */
        public float frequency = 1/100f;

        /**
         * Scale time in steps calls
         */
        public float timeFactor = 1f;

        /**
         * RigidBodies velocity iterations
         */
        public int velocityIterations = 8;

        /**
         * RigidBodies position iterations
         */
        public int positionIterations = 3;

        /**
         * ParticlesBodies velocity iterations
         */
        public int particleIterations = 3;

        /**
         * Enabled debug renderer (RigidBodies only)
         */
        public boolean debug = false;
    }


}
