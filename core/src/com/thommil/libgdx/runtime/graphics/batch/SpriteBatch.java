package com.thommil.libgdx.runtime.graphics.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.NumberUtils;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Custom Batch for simple Sprite based on LibGDX SpriteBatch
 *
 * Created by thommil on 2/10/16.
 */
public class SpriteBatch implements Batch{

    protected final Mesh mesh;
    protected final float[] vertices;
    protected final ShaderProgram shader;

    protected int idx = 0;
    protected Texture lastTexture = null;

    protected final Matrix4 combinedMatrix = new Matrix4();
    protected boolean isDrawing = false;
    protected float color = Color.WHITE.toFloatBits();

    private Color tempColor = new Color(1, 1, 1, 1);

    public SpriteBatch(final int size) {
        mesh = createMesh(size);
        vertices = createVertices(size);
        shader = createShader();
    }

    @Override
    public void begin () {
        isDrawing = true;
        shader.begin();
        shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
        shader.setUniformi("u_texture", 0);
    }

    @Override
    public void end () {
        if (idx > 0) flush();
        lastTexture = null;
        shader.end();
        isDrawing = false;
    }

    @Override
    public void draw (Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        this.draw(texture,x,y,width,height,u,v,u2,v2,this.color);
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

    @Override
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

    @Override
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

    @Override
    public void dispose () {
        mesh.dispose();
        shader.dispose();
    }

    @Override public boolean isDrawing() {
        return isDrawing;
    }

    @Override public ShaderProgram getShader() {
        return this.shader;
    }

    @Override
    public void setColor (Color tint) {
        color = tint.toFloatBits();
    }

    @Override
    public void setColor (float r, float g, float b, float a) {
        int intBits = (int)(255 * a) << 24 | (int)(255 * b) << 16 | (int)(255 * g) << 8 | (int)(255 * r);
        color = NumberUtils.intToFloatColor(intBits);
    }

    @Override
    public void setColor (float color) {
        this.color = color;
    }

    @Override
    public Color getColor () {
        int intBits = NumberUtils.floatToIntColor(color);
        Color color = tempColor;
        color.r = (intBits & 0xff) / 255f;
        color.g = ((intBits >>> 8) & 0xff) / 255f;
        color.b = ((intBits >>> 16) & 0xff) / 255f;
        color.a = ((intBits >>> 24) & 0xff) / 255f;
        return color;
    }

    @Override
    public float getPackedColor () {
        return color;
    }

    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    protected void switchTexture (Texture texture) {
        flush();
        lastTexture = texture;
    }

    /**
     * Create methods below must be overriden for custom Batch
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

    /**
     * Methods below as been removed from this implementation, use LibGDX SpriteBatch if they are needed
     */

    @Override public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, float width, float height) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y, float width, float height) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float width, float height, Affine2 transform) { throw new GameRuntimeException("Not implemented");}
    @Override public Matrix4 getProjectionMatrix() {throw new GameRuntimeException("Not implemented");}
    @Override public Matrix4 getTransformMatrix() { throw new GameRuntimeException("Not implemented");}
    @Override public void setProjectionMatrix(Matrix4 projection) {throw new GameRuntimeException("Not implemented");}
    @Override public void setTransformMatrix(Matrix4 transform) { throw new GameRuntimeException("Not implemented");}
    @Override public void setShader(ShaderProgram shader) { throw new GameRuntimeException("Not implemented");}

    //Blending is just disabled and should be handled manualy at higher level (avoid many GL calls)
    @Override public boolean isBlendingEnabled() {return true;}
    @Override public void disableBlending() {}
    @Override public void enableBlending() {}
    @Override public void setBlendFunction(int srcFunc, int dstFunc) {}
    @Override public int getBlendSrcFunc() { return GL20.GL_SRC_ALPHA;}
    @Override public int getBlendDstFunc() { return GL20.GL_ONE_MINUS_SRC_ALPHA;}
}
