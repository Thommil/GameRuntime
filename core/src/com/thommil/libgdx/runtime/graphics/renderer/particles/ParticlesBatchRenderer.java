package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.scene.Renderer;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;
import com.thommil.libgdx.runtime.tools.GL11;

/**
 * Base Batch for LiquidFun ParticleSystem (ParticlesBody).
 *
 * Created by thommil on 2/10/16.
 */
public class ParticlesBatchRenderer implements Renderer{

    protected final Mesh mesh;
    protected final float[] vertices;
    protected int verticesSize;
    protected final ShaderProgram shader;

    protected int idx = 0;
    protected float lastParticlesRadius;
    protected float currentParticlesRadius;

    protected float particlesScale = 1f;
    protected final Matrix4 combinedMatrix = new Matrix4();

    /**
     * Default constructor
     *
     * @param size The batch size
     */
    public ParticlesBatchRenderer(final int size) {
        this.mesh = createMesh(size);
        this.vertices = createVertices(size);
        this.shader = createShader();
    }

    /**
     * Called at beginning of rendering
     */
    @Override
    public void begin() {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
            Gdx.gl.glEnable(GL11.GL_POINT_SPRITE_OES);
        }

        shader.begin();
        shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
    }

    /**
     * Called at ending of rendering
     */
    @Override
    public void end() {
        if (this.idx > 0) flush();
        shader.end();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
            Gdx.gl.glDisable(GL11.GL_POINT_SPRITE_OES);
        }
        this.lastParticlesRadius = this.currentParticlesRadius = 0;
    }

    /**
     * Sets the rendering scale of particles
     *
     * @param particlesScale rendering scale
     */
    public void setParticlesScale(float particlesScale) {
        this.particlesScale = particlesScale;
    }

    /**
     * Sets the current particles radius
     *
     * @param particlesRadius The current particles radius
     */
    public void setParticlesRadius(float particlesRadius) {
        this.currentParticlesRadius = particlesRadius;
    }


    /**
     * Generic method to draw a set of vertices.
     *
     * @param vertices The list of vertices to draw
     */
    @Override
    public void draw(float[] vertices) {
        int offset = 0;
        int count = vertices.length;
        int remainingVertices = this.vertices.length;
        if (this.currentParticlesRadius != this.lastParticlesRadius) {
            flush();
            this.lastParticlesRadius = this.currentParticlesRadius;
        }else {
            remainingVertices -= this.idx;
            if (remainingVertices == 0) {
                flush();
                remainingVertices = this.vertices.length;
            }
        }
        int copyCount = Math.min(remainingVertices, count);

        System.arraycopy(vertices, offset, this.vertices, this.idx, copyCount);
        this.idx += copyCount;
        count -= copyCount;
        while (count > 0) {
            offset += copyCount;
            flush();
            copyCount = Math.min(this.vertices.length, count);
            System.arraycopy(vertices, offset, this.vertices, 0, copyCount);
            this.idx += copyCount;
            count -= copyCount;
        }
    }

    /**
     * Flushes the batch and renders all remaining vertices
     */
    public void flush () {
        if (this.idx == 0) return;

        final int count = this.idx / this.verticesSize ;

        shader.setUniformf("radius", this.lastParticlesRadius * 2f * this.particlesScale);
        this.mesh.setVertices(this.vertices, 0, this.idx);
        mesh.render(shader, GL20.GL_POINTS, 0, count);

        this.idx = 0;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }

    /**
     * Sets the combined matrix for rendering
     *
     * @param combinedMatrix the combined matrix
     */
    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    @Override
    public void draw(int id) {
        throw new GameRuntimeException("Not implemented");
    }

    /**
     * Subclasses should override this method to use their specific Mesh
     */
    protected Mesh createMesh(final int size){
        if (size > 32767) throw new IllegalArgumentException("Can't have more than 32767 particles per batch (are you serious?): " + size);

        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }

        return new Mesh(vertexDataType, false, size , 0,
                        new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
    }

    /**
     * Subclasses should override this method to use their specific vertices
     */
    protected  float[] createVertices(final int size){
        this.verticesSize = ParticleSystemActor.VERTEX_SIZE;
        return new float[size * ParticleSystemActor.VERTEX_SIZE];
    }

    /**
     * Subclasses should override this method to use their specific shaders
     */
    protected ShaderProgram createShader () {
        String prefix = "";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            prefix += "#version 120\n";
        }
        else {
            prefix += "#version 100\n";
        }

        final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projTrans;\n" //
                    + "uniform float radius;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";

        final String fragmentShader = "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" //
                    + "const float HALF = 0.5;\n" //
                    + "const float FULL = 1.0;\n" //
                    + "\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = vec4(FULL, FULL, FULL, HALF-length(vec2(gl_PointCoord.x - HALF, gl_PointCoord.y - HALF)));\n" //
                    + "}";


        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
