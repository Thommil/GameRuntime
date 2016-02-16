package com.thommil.libgdx.runtime.graphics.batch;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;

/**
 * Custom Batch for LiquidFun ParticleSystem (SoftBody).
 *
 * Basic version, should be used for debugging purpose
 *
 * Created by thommil on 2/10/16.
 */
public class SoftBodyBatch {

    protected final Mesh mesh;

    protected final ShaderProgram shader;

    protected float particlesScale = 1f;

    public SoftBodyBatch() {
        this(10000);
    }

    public SoftBodyBatch(int size) {
        if (size > 32767) throw new IllegalArgumentException("Can't have more than 32767 particles per batch (are you serious?): " + size);

        mesh = new Mesh(Mesh.VertexDataType.VertexArray, false, size , 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE));

        shader = createDefaultShader();
    }

    public void begin (final Matrix4 combined, final float particlesScale) {
        Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
        Gdx.gl.glEnable(0x8861); //GL11.GL_POINT_SPRITE_OES
        this.particlesScale = particlesScale;
        shader.begin();
        shader.setUniformMatrix("u_projTrans", combined);
    }

    public void end () {
        shader.end();
        Gdx.gl20.glDisable(0x8861);
    }

    public void draw (float[] vertices, float radius) {
        final int count = vertices.length / SoftBodyActor.VERTEX_SIZE;
        shader.setUniformf("radius", radius * particlesScale);
        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_POINTS, 0, count);
    }

    public void dispose () {
        mesh.dispose();
        shader.dispose();
    }

    protected ShaderProgram createDefaultShader () {
        String prefix = "";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
            prefix +="#version 120\n";
        else
            prefix +="#version 100\n";

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
                        + "varying vec4 v_color;\n" //
                        + "void main()\n"//
                        + "{\n" //
                        + " float len = length(vec2(gl_PointCoord.x - 0.5, gl_PointCoord.y - 0.5));\n" //
                        + " if(len <= 0.5) {\n" //
                        + " 	gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" //
                        + " } else {\n" //
                        + " 	gl_FragColor = vec4(0.0, 0.0, 0.0, 0.0);\n" //
                        + " }\n" //
                        + "}";

        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
