package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.StringBuilder;
import com.sun.glass.ui.SystemClipboard;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.listener.SceneListener;

/**
 * Global Profiler of Scenes
 *
 * Created by thommil on 13/02/16.
 */
public class SceneProfiler{

    private static SceneProfiler instance;

    /**
     * The scene being analyzed
     */
    final Scene scene;

    /**
     * Flags enabled
     */
    final byte flags;

    /**
     * Display frequency in ms
     */
    final int frequency;

    /**
     * Inner logger
     */
    final Logger logger;

    /**
     * Frame per seconds
     */
    public static final byte RENDERER = 1;

    /**
     * Steps per seconds
     */
    public static final byte PHYSICS = 2;

    /**
     * OpenGL debug
     */
    public static final byte GL = 4;

    /**
     * Memory info
     */
    public static final byte MEMORY = 8;

    /**
     * All flags enabled
     */
    public static final byte ALL = RENDERER | PHYSICS | GL | MEMORY;


    public static void profile(final Scene scene){
        SceneProfiler.profile(scene, ALL, 1000);
    }

    public static void profile(final Scene scene, final byte flags){
        SceneProfiler.profile(scene, flags, 1000);
    }

    public static void profile(final Scene scene, final byte flags, final int frequency){
        if(SceneProfiler.instance != null){
            SceneProfiler.instance.logger.onDispose();
        }
        SceneProfiler.instance = new SceneProfiler(scene, flags, frequency);
    }

    protected SceneProfiler(final Scene scene, final byte flags, final int frequency){
        this.scene = scene;
        this.flags = flags;
        this.frequency = frequency;
        this.logger = new Logger(this);
        if((this.flags & SceneProfiler.GL) != 0){
            GLProfiler.enable();
        }
        new Thread(logger).start();
    }

    private static class Logger extends Thread implements SceneListener{

        final SceneProfiler profiler;

        SceneListener sourceListener;

        boolean running = false;
        boolean paused = false;

        int stepCalls = 0;
        int renderCalls = 0;

        public Logger(SceneProfiler profiler) {
            this.profiler = profiler;
            sourceListener = profiler.scene.getSceneListener();
            profiler.scene.setSceneListener(this);
        }

        @Override
        public void run() {
            running = true;
            StringBuilder outStr = new StringBuilder();
            float frequencyFactor = 1000f / this.profiler.frequency;
            while(running){
                if(!this.paused){
                    outStr.delete(0, outStr.length);
                    if((this.profiler.flags & SceneProfiler.RENDERER) != 0){
                        outStr.append("[RENDERER:")
                                .append("fps=").append((int)(renderCalls * frequencyFactor)).append(",")
                                .append("objets=").append(this.profiler.scene.getRenderablesCount())
                                .append("]");
                    }
                    if((this.profiler.flags & SceneProfiler.PHYSICS) != 0){
                        outStr.append("[PHYSICS:")
                                .append("sps=").append((int)(stepCalls * frequencyFactor)).append(",")
                                .append("bodies=").append(this.profiler.scene.getPhysicsWorld().getBodyCount()).append(",")
                                .append("contacts=").append(this.profiler.scene.getPhysicsWorld().getContactCount())
                                .append("]");
                    }
                    if((this.profiler.flags & SceneProfiler.GL) != 0 && renderCalls > 0){
                        outStr.append("[GL:")
                                .append("calls=").append((int)(GLProfiler.calls / renderCalls)).append(", ")
                                .append("drawCalls=").append((int)(GLProfiler.drawCalls / renderCalls)).append(", ")
                                .append("shaderSwitches=").append((int)(GLProfiler.shaderSwitches / renderCalls)).append(", ")
                                .append("textureBindings=").append((int)(GLProfiler.textureBindings / renderCalls)).append(", ")
                                .append("vertexCount=").append((int)(GLProfiler.vertexCount.average))
                                .append("]");
                        GLProfiler.calls=0;
                        GLProfiler.drawCalls=0;
                        GLProfiler.shaderSwitches=0;
                        GLProfiler.textureBindings=0;

                    }
                    if((this.profiler.flags & SceneProfiler.MEMORY) != 0){
                        outStr.append("[MEMORY:")
                                .append("java=").append((int)(Gdx.app.getJavaHeap()/1048576)).append("MB, ")
                                .append("native=").append((int)(Gdx.app.getNativeHeap()/1048576)).append("MB")
                                .append("]");
                    }
                    Gdx.app.log("PROFILER", outStr.toString());
                    renderCalls=0;
                    stepCalls=0;
                }
                try{
                    Thread.sleep(this.profiler.frequency);
                }catch(InterruptedException ie){
                    Gdx.app.exit();
                }
            }
        }

        /**
         * Called before each worl step with the duration of the last step processing
         *
         * @param lastDuration The duration of the last processing for QoS purpose
         */
        @Override
        public void onStep(long lastDuration) {
            stepCalls++;
            if(this.sourceListener != null) {
                this.sourceListener.onStep(lastDuration);
            }
        }

        /**
         * Called before each rendering
         *
         * @param deltaTime The time ellapsed since last rendering
         */
        @Override
        public void onRender(float deltaTime) {
            renderCalls++;
            if(this.sourceListener != null) {
                this.sourceListener.onRender(deltaTime);
            }
        }

        /**
         * Called on resize()
         */
        @Override
        public void onResize() {
            if(this.sourceListener != null) {
                this.sourceListener.onResize();
            }
        }

        /**
         * Called on show()
         */
        @Override
        public void onShow() {
            this.paused = false;
            if(this.sourceListener != null) {
                this.sourceListener.onShow();
            }
        }

        /**
         * Called on hide()
         */
        @Override
        public void onHide() {
            this.running = false;
            this.paused = true;
            if(this.sourceListener != null) {
                this.sourceListener.onHide();
            }
        }

        /**
         * Called on resume()
         */
        @Override
        public void onResume() {
            this.paused = false;
            if(this.sourceListener != null) {
                this.sourceListener.onResume();
            }
        }

        /**
         * Called on pause()
         */
        @Override
        public void onPause() {
            this.paused = true;
            if(this.sourceListener != null) {
                this.sourceListener.onPause();
            }
        }

        /**
         * called on dispose()
         */
        @Override
        public void onDispose() {
            this.running = false;
            this.paused = true;
            if(this.sourceListener != null) {
                this.sourceListener.onDispose();
            }
        }
    }
}
