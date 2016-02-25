package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;

/**
 * Extension of ParticlesBatchRenderer with Colored particles
 *
 * Created by thommil on 2/22/16.
 */
public class ColoredParticlesBatchRenderer extends ParticlesBatchRenderer {


    public ColoredParticlesBatchRenderer(final int maxParticles) {
        super(maxParticles);
    }

    protected Mesh createMesh(final int size){
        if (size > 32767) throw new IllegalArgumentException("Can't have more than 32767 particles per batch (are you serious?): " + size);

        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }

        return new Mesh(vertexDataType, false, size , 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
    }

    protected  float[] createVertices(final int size){
        this.verticesSize = ParticleSystemActor.COLORED_VERTEX_SIZE;
        return new float[size * ParticleSystemActor.COLORED_VERTEX_SIZE];
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
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "uniform float radius;\n" //
                + "\n" //
                + "varying vec4 v_color;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" //
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
                + "\n" //
                + "void main()\n"//
                + "{\n" //
                + " gl_FragColor = v_color;\n" //
                + "}";


        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
