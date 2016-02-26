package com.thommil.libgdx.runtime.graphics.renderer.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.scene.Renderer;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Custom Batch for simple Sprite based on LibGDX SpriteBatch
 *
 * @author thommil on 03/02/16.
 */
public class SpriteBatchRenderer implements Renderer{

    protected final Mesh mesh;
    protected final float[] vertices;
    protected int verticesSize;
    protected final ShaderProgram shader;

    protected int idx = 0;
    protected TextureSet lastTextureSet = null;
    protected TextureSet currentTextureSet = null;
    protected int currentTextureSetSize = 0;
    protected float color = Color.WHITE.toFloatBits();

    protected final Matrix4 combinedMatrix = new Matrix4();

    /**
     * Default constructor
     *
     * @param size The batch size
     */
    public SpriteBatchRenderer(final int size) {
        this.mesh = createMesh(size);
        this.vertices = createVertices(size);
        this.shader = createShader();
    }

    /**
     * Called at beginning of rendering
     */
    @Override
    public void begin() {
        this.shader.begin();
        this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
    }

    /**
     * Called at ending of rendering
     */
    @Override
    public void end() {
        if (this.idx > 0) flush();
        this.shader.end();
    }

    /**
     * Sets the color of the Sprite
     *
     * @param color The color of the sprite
     */
    public void setColor (float color) {
        this.color = color;
    }

    /**
     * Sets the combined matrix for rendering
     *
     * @param combinedMatrix the combined matrix
     */
    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    /**
     * Sets current TextureSet
     *
     * @param textureSet The current TextureSet to use
     */
    public void setTextureSet(final TextureSet textureSet){
        this.currentTextureSet = textureSet;
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
        if (this.currentTextureSet != this.lastTextureSet) {
            flush();
            this.lastTextureSet = this.currentTextureSet;
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
     * Generic method to draw a predefined object
     *
     * @param id The ID of the object
     */
    @Override
    public void draw(int id) {
        //Not implemented
    }

    /**
     * Flushes the batch and renders all remaining vertices
     */
    public void flush () {
        if (this.idx == 0) return;

        if(this.lastTextureSet.textures.length != currentTextureSetSize){
            this.lastTextureSet.setUniformAll(this.shader);
            this.currentTextureSetSize = this.lastTextureSet.textures.length;
        }

        final int count = this.idx / SpriteActor.SPRITE_SIZE * 6;

        this.lastTextureSet.bindAll();
        this.mesh.setVertices(this.vertices, 0, this.idx);
        this.mesh.getIndicesBuffer().position(0);
        this.mesh.getIndicesBuffer().limit(count);

        this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, count);

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
     * Subclasses should override this method to use their specific Mesh
     */
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

    /**
     * Subclasses should override this method to use their specific vertices
     */
    protected  float[] createVertices(final int size){
        this.verticesSize = SpriteActor.VERTEX_SIZE;
        return new float[size * SpriteActor.SPRITE_SIZE];
    }

    /**
     * Subclasses should override this method to use their specific shaders
     */
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
                + "uniform sampler2D "+TextureSet.UNIFORM_TEXTURE_0+";\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D("+TextureSet.UNIFORM_TEXTURE_0+", v_texCoords);\n" //
                + "}";

        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
