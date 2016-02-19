package com.thommil.libgdx.runtime.graphics.batch;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;

/**
 * Custom Batch for LiquidFun ParticleSystem (SoftBody).
 *
 * Not really a Batch as all vertices are stored in ParticleSystem so only
 * one SoftBodyBatch should be implemented by ParticleSystem
 *
 * Created by thommil on 2/10/16.
 */
public class SoftBodyBatch implements Batch{

    protected final Mesh mesh;
    protected final ShaderProgram shader;

    protected final Texture texture;
    protected float particlesScale = 1f;

    protected final Matrix4 combinedMatrix = new Matrix4();
    protected boolean isDrawing = false;

    public SoftBodyBatch() {
        this(null, 10000);
    }

    public SoftBodyBatch(final Texture texture){
            this(texture, 10000);
    }

    public SoftBodyBatch(final int size) {
        this(null, size);
    }

    public SoftBodyBatch(final Texture texture, final int size) {
        this.texture = texture;
        mesh = createMesh(size);
        shader = createShader();
    }

    @Override
    public void begin () {
        isDrawing = true;
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
            Gdx.gl.glEnable(0x8861); //GL11.GL_POINT_SPRITE_OES
        }

        shader.begin();
        shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
        if(this.texture != null){
            this.texture.bind(0);
            shader.setUniformi("u_texture", 0);
        }
    }

    @Override
    public void end () {
        shader.end();
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.gl.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
            Gdx.gl.glDisable(0x8861); //GL11.GL_POINT_SPRITE_OES
        }
        isDrawing = false;
    }

    public void draw (float[] vertices, float radius) {
        final int count = vertices.length / SoftBodyActor.VERTEX_SIZE;
        shader.setUniformf("radius", radius * particlesScale * 2f);
        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_POINTS, 0, count);
    }

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

        return new Mesh(Mesh.VertexDataType.VertexArray, false, size , 0,
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

        String vertexShader, fragmentShader;

        if(this.texture == null) {
            vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projTrans;\n" //
                    + "uniform float radius;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";
            fragmentShader = "#ifdef GL_ES\n" //
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
        }
        else{
            vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projTrans;\n" //
                    + "uniform float radius;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";
            fragmentShader = "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" //
                    + "uniform sampler2D u_texture;\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = texture2D(u_texture, gl_PointCoord);\n" //
                    + "}";
        }

        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    /**
     * Methods below as been removed from this implementation, use LibGDX SpriteBatch if they are needed
     */
    @Override public void setColor(Color tint) {throw new GameRuntimeException("Not implemented");}
    @Override public void setColor(float r, float g, float b, float a) {throw new GameRuntimeException("Not implemented");}
    @Override public void setColor(float color) {throw new GameRuntimeException("Not implemented");}
    @Override public Color getColor() {throw new GameRuntimeException("Not implemented");}
    @Override public float getPackedColor() {throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float x, float y, float width, float height) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y, float width, float height) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(TextureRegion region, float width, float height, Affine2 transform) { throw new GameRuntimeException("Not implemented");}
    @Override public void disableBlending() {throw new GameRuntimeException("Not implemented");}
    @Override public void enableBlending() {throw new GameRuntimeException("Not implemented");}
    @Override public void setBlendFunction(int srcFunc, int dstFunc) {throw new GameRuntimeException("Not implemented");}
    @Override public int getBlendSrcFunc() {throw new GameRuntimeException("Not implemented");}
    @Override public int getBlendDstFunc() {throw new GameRuntimeException("Not implemented");}
    @Override public Matrix4 getProjectionMatrix() {throw new GameRuntimeException("Not implemented");}
    @Override public Matrix4 getTransformMatrix() { throw new GameRuntimeException("Not implemented");}
    @Override public void setProjectionMatrix(Matrix4 projection) {throw new GameRuntimeException("Not implemented");}
    @Override public void setTransformMatrix(Matrix4 transform) { throw new GameRuntimeException("Not implemented");}
    @Override public void setShader(ShaderProgram shader) { throw new GameRuntimeException("Not implemented");}
    @Override public boolean isBlendingEnabled() { throw new GameRuntimeException("Not implemented");}
    @Override public void draw(Texture texture, float[] spriteVertices, int offset, int count) { throw new GameRuntimeException("Not implemented");}
    @Override public void flush() { throw new GameRuntimeException("Not implemented");}
}