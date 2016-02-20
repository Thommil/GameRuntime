package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.physics.AbstractStepable;
import finnstr.libgdx.liquidfun.ParticleSystem;

/**
 * Profiler for Scenes
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

    private final StringBuilder statsLog = new StringBuilder();
    private int stepCalls = 0;
    private int renderCalls = 0;
    long startTime;

    public static void profile(final Scene scene){
        SceneProfiler.profile(scene, ALL, 1000);
    }

    public static void profile(final Scene scene, final byte flags){
        SceneProfiler.profile(scene, flags, 1000);
    }

    public static void profile(final Scene scene, final byte flags, final int frequency){
        SceneProfiler.instance = new SceneProfiler(scene, flags, frequency);
    }

    protected SceneProfiler(final Scene scene, final byte flags, final int frequency){
        this.scene = scene;
        this.flags = flags;
        this.frequency = frequency;
        if((this.flags & SceneProfiler.GL) != 0){
            GLProfiler.enable();
        }
        this.doProfile();
    }

    protected void doProfile(){
        final float frequencyFactor = 1000f / this.frequency;
        this.startTime = System.currentTimeMillis();

        this.scene.addLayer(new Layer(1) {

            @Override
            public void render(float deltaTime) {
                renderCalls++;
                if(System.currentTimeMillis() - startTime > SceneProfiler.this.frequency){
                    SceneProfiler.this.logStatistics(renderCalls,stepCalls,frequencyFactor);
                    renderCalls = 0;
                    stepCalls = 0;
                    startTime= System.currentTimeMillis();
                }
            }
            @Override protected void onShow() {}
            @Override protected void onHide() {}
            @Override protected void onResize(int width, int height) {}
            @Override public void dispose() {}
        });

        this.scene.addActor(new AbstractStepable(MathUtils.random(MathUtils.random(0x7ffffffe))) {
            @Override
            public void step(long lastStepDuration) {
                stepCalls++;
            }
        });
    }

    protected void logStatistics(final int renderCalls, final int stepCalls, final float frequencyFactor){
        statsLog.delete(0, statsLog.length);
        if((this.flags & SceneProfiler.RENDERER) != 0){
            statsLog.append("[RENDERER:")
                    .append("fps=").append((int)(renderCalls * frequencyFactor)).append(",")
                    .append("objets=").append(this.scene.getRenderablesCount())
                    .append("]");
        }
        if((this.flags & SceneProfiler.PHYSICS) != 0){
            int particlesCount = 0;
            for(final ParticleSystem particleSystem : this.scene.getPhysicsWorld().particleSystems.values()){
                particlesCount += particleSystem.getParticleCount();
            }
            statsLog.append("[PHYSICS:")
                    .append("sps=").append((int)(stepCalls * frequencyFactor)).append(",")
                    .append("bodies=").append(this.scene.getPhysicsWorld().getBodyCount()).append(",")
                    .append("particles=").append(particlesCount).append(",")
                    .append("contacts=").append(this.scene.getPhysicsWorld().getContactCount())
                    .append("]");
        }
        if((this.flags & SceneProfiler.GL) != 0 && renderCalls > 0){
            statsLog.append("[GL:")
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
        if((this.flags & SceneProfiler.MEMORY) != 0){
            statsLog.append("[MEMORY:")
                    .append("java=").append((int)(Gdx.app.getJavaHeap()/1048576)).append("MB, ")
                    .append("native=").append((int)(Gdx.app.getNativeHeap()/1048576)).append("MB")
                    .append("]");
        }
        Gdx.app.log("PROFILER", statsLog.toString());
    }
}
