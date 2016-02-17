package com.thommil.libgdx.runtime.graphics.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Custom Batch for simple Sprite based on LibGDX SpriteBatch
 *
 * Created by thommil on 2/10/16.
 */
public class SpriteBatch{

    protected final Mesh mesh;
    protected final float[] vertices;
    protected final ShaderProgram shader;

    protected int idx = 0;
    protected Texture lastTexture = null;

    public SpriteBatch() {
        this(1000);
    }

    public SpriteBatch(final int size) {
        mesh = createMesh(size);
        vertices = createVertices(size);
        shader = createShader();
    }

    public void begin (final Matrix4 combinedMatrix) {
        shader.begin();
        shader.setUniformMatrix("u_projTrans", combinedMatrix);
        shader.setUniformi("u_texture", 0);
    }

    public void end () {
        if (idx > 0) flush();
        lastTexture = null;
        shader.end();
    }

    public void draw (Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2, final float color) {
        if (texture != lastTexture)
            switchTexture(texture);
        else if (idx == vertices.length) //
            flush();

        final float fx2 = x + width;
        final float fy2 = y + height;

        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = x;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = fy2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = fx2;
        vertices[idx++] = y;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
    }

    public void draw (Texture texture, float[] spriteVertices, int offset, int count) {
        int remainingVertices = vertices.length;
        if (texture != lastTexture)
            switchTexture(texture);
        else {
            remainingVertices -= idx;
            if (remainingVertices == 0) {
                flush();
                remainingVertices = vertices.length;
            }
        }
        int copyCount = Math.min(remainingVertices, count);

        System.arraycopy(spriteVertices, offset, vertices, idx, copyCount);
        idx += copyCount;
        count -= copyCount;
        while (count > 0) {
            offset += copyCount;
            flush();
            copyCount = Math.min(vertices.length, count);
            System.arraycopy(spriteVertices, offset, vertices, 0, copyCount);
            idx += copyCount;
            count -= copyCount;
        }
    }

    public void flush () {
        if (idx == 0) return;

        final int count = idx / SpriteActor.SPRITE_SIZE * 6;

        lastTexture.bind();
        this.mesh.setVertices(vertices, 0, idx);
        this.mesh.getIndicesBuffer().position(0);
        this.mesh.getIndicesBuffer().limit(count);

        this.mesh.render(shader, GL20.GL_TRIANGLES, 0, count);

        idx = 0;
    }

    public void dispose () {
        mesh.dispose();
        shader.dispose();
    }

    protected void switchTexture (Texture texture) {
        flush();
        lastTexture = texture;
    }

    protected  Mesh createMesh(final int size){
        // 32767 is max index, so 32767 / 6 - (32767 / 6 % 3) = 5460.
        if (size > 5460) throw new IllegalArgumentException("Can't have more than 5460 sprites per batch: " + size);

        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }
        final Mesh mesh = new Mesh(vertexDataType, false, size * 4, size * 6, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        final int len = size * 6;
        final short[] indices = new short[len];
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i] = j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = j;
        }
        mesh.setIndices(indices);

        return mesh;
    }

    protected  float[] createVertices(final int size){
        return new float[size * SpriteActor.SPRITE_SIZE];
    }

    protected ShaderProgram createShader () {
        final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        final String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying vec2 v_texCoords;\n" //
                + "varying vec4 v_color;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
                + "}";

        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
