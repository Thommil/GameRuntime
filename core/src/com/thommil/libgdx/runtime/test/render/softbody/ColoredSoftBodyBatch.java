package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.thommil.libgdx.runtime.graphics.ExtendedMesh;
import com.thommil.libgdx.runtime.graphics.batch.ParticlesBatch;

/**
 * Created by tomtom on 20/02/16.
 */
public class ColoredSoftBodyBatch extends ParticlesBatch {

    private int colorAttributeOffset = 0;

    public ColoredSoftBodyBatch(int size) {
        super(size);
    }

    /**
     * Add colors to Color buffer (can be used for update using dynamic offset)
     *
     * @param colors The colors to add
     */
    public void addAttributeColors(float[] colors){
        ((ExtendedMesh)this.mesh).getVertexData(VertexAttributes.Usage.ColorPacked).updateVertices(colorAttributeOffset, colors, 0, colors.length);
        colorAttributeOffset += colors.length;
    }

    /**
     * Create methods below must be overriden for custom Batch
     *
     * @param size
     */
    @Override
    protected Mesh createMesh(int size) {

        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }

        ExtendedMesh mesh = new ExtendedMesh(vertexDataType, false, size , 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));

        final VertexData colorData = new VertexBufferObject(true, size, new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));

        final float []vertices = new float[size];
        colorData.setVertices(vertices,0,size);
        mesh.addVertexData(colorData);

        return mesh;
    }

    @Override
    protected ShaderProgram createShader() {
        String prefix = "";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            prefix += "#version 120\n";
        }
        else {
            prefix += "#version 100\n";
        }

        String  vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projTrans;\n" //
                    + "uniform float radius;\n" //
                    + "varying vec4 v_color;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";
        String  fragmentShader = "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" //
                    + "varying vec4 v_color;\n" //
                    + "\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = vec4(v_color.r, v_color.g, v_color.b, v_color.a);\n" //
                    + "}";


        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
