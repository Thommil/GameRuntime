package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.physics.AbstractStepable;
import com.thommil.libgdx.runtime.layer.Layer;
import finnstr.libgdx.liquidfun.ParticleSystem;

/**
 * Profiler for Scenes, just call RuntimeProfiler.profile() to start
 * profiling. The Profiler try to use a layer on top of layers stack so
 * setting a layer with a specific index can possibly remove the profiler
 * from the stack.
 *
 * Created by thommil on 13/02/16.
 */
public class RuntimeProfiler {

    private static RuntimeProfiler instance;

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

    public static void profile(){
        RuntimeProfiler.profile(ALL, 1000);
    }

    public static void profile(final byte flags){
        RuntimeProfiler.profile(flags, 1000);
    }

    public static void profile(final byte flags, final int frequency){
        RuntimeProfiler.instance = new RuntimeProfiler(flags, frequency);
    }

    protected RuntimeProfiler(final byte flags, final int frequency){
        this.flags = flags;
        this.frequency = frequency;
        if((this.flags & RuntimeProfiler.GL) != 0){
            GLProfiler.enable();
        }
        this.doProfile();
    }

    protected void doProfile(){
        final float frequencyFactor = 1000f / this.frequency;
        this.startTime = System.currentTimeMillis();

        final Layer profileLayer = new Layer(Runtime.getInstance().getViewport(), 1) {
            @Override public void render(float deltaTime) {
                renderCalls++;
                if(System.currentTimeMillis() - startTime > RuntimeProfiler.this.frequency){
                    RuntimeProfiler.this.logStatistics(renderCalls,stepCalls,frequencyFactor);
                    renderCalls = 0;
                    stepCalls = 0;
                    startTime= System.currentTimeMillis();
                }
            }
            @Override protected void onShow() {}
            @Override protected void onResize(int width, int height) {}
            @Override protected void onHide() {}
            @Override public void dispose() {}
        };

        Runtime.getInstance().addLayer(profileLayer);

        profileLayer.addActor(new AbstractStepable(MathUtils.random(MathUtils.random(0x7ffffffe))) {
            @Override
            public void step(float lastStepDuration) {
                stepCalls++;
            }
        });
    }

    protected void logStatistics(final int renderCalls, final int stepCalls, final float frequencyFactor){
        statsLog.delete(0, statsLog.length);
        if((this.flags & RuntimeProfiler.RENDERER) != 0){
            statsLog.append("[RENDERER:")
                    .append("fps=").append((int)(renderCalls * frequencyFactor))
                    .append("]");
        }
        if((this.flags & RuntimeProfiler.PHYSICS) != 0){
            int particlesCount = 0;
            for(final ParticleSystem particleSystem : Runtime.getInstance().getPhysicsWorld().particleSystems.values()){
                particlesCount += particleSystem.getParticleCount();
            }
            statsLog.append("[PHYSICS:")
                    .append("sps=").append((int)(stepCalls * frequencyFactor)).append(",")
                    .append("bodies=").append(Runtime.getInstance().getPhysicsWorld().getBodyCount()).append(",")
                    .append("particles=").append(particlesCount).append(",")
                    .append("contacts=").append(Runtime.getInstance().getPhysicsWorld().getContactCount())
                    .append("]");
        }
        if((this.flags & RuntimeProfiler.GL) != 0 && renderCalls > 0){
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
        if((this.flags & RuntimeProfiler.MEMORY) != 0){
            statsLog.append("[MEMORY:")
                    .append("java=").append((int)(Gdx.app.getJavaHeap()/1048576)).append("MB, ")
                    .append("native=").append((int)(Gdx.app.getNativeHeap()/1048576)).append("MB")
                    .append("]");
        }
        Gdx.app.log("PROFILER", statsLog.toString());
    }
}
