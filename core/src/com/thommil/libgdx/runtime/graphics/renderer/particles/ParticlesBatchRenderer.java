package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.scene.Renderer;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;
import com.thommil.libgdx.runtime.tools.GL11;

/**
 * Base Batch for LiquidFun ParticleSystem (ParticlesBody).
 *
 * Created by thommil on 2/10/16.
 */
public class ParticlesBatchRenderer implements Renderer{

    protected final Mesh mesh;
    protected final ShaderProgram shader;

    protected float particlesRadius;
    protected float particlesScale = 1f;

    protected final Matrix4 combinedMatrix = new Matrix4();
    protected boolean isDrawing = false;

    protected int verticesSize;

    public ParticlesBatchRenderer(final int maxParticles) {
        mesh = createMesh(maxParticles);
        shader = createShader();
        this.verticesSize = ParticleSystemActor.VERTEX_SIZE;
    }

    @Override
    public void begin () {
        isDrawing = true;
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
            Gdx.gl.glEnable(GL11.GL_POINT_SPRITE_OES);
        }

        shader.begin();
        shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
    }

    @Override
    public void end () {
        shader.end();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
            Gdx.gl.glDisable(GL11.GL_POINT_SPRITE_OES);
        }
        isDrawing = false;
    }

    @Override
    public void draw(int id) {
        throw new GameRuntimeException("Not implemented");
    }

    public void setParticlesRadius(float particlesRadius) {
        this.particlesRadius = particlesRadius;
    }

    public void draw (float[] vertices) {
        final int count = vertices.length / verticesSize;
        shader.setUniformf("radius", this.particlesRadius * particlesScale * 2f);
        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_POINTS, 0, count);
    }

    public void dispose () {
        mesh.dispose();
        shader.dispose();
    }


    public void setParticlesScale(float particlesScale) {
        this.particlesScale = particlesScale;
    }

    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    /**
     * Create methods below must be overriden for custom Batch
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
