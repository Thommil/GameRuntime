package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.BatchRenderer;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.actor.physics.ParticleSystemActor;
import com.thommil.libgdx.runtime.tools.GL11;

/**
 * Base Batch for LiquidFun ParticleSystem (ParticlesBody).
 *
 * @author thommil on 03/02/16.
 */
public class ParticlesBatchRenderer implements BatchRenderer{

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

    @Override
    public void draw(TextureSet textureSet, float[] vertices, int offset, int count) {
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

    @Override
    public void draw(Texture texture, float[] vertices, int offset, int count) {
        this.draw((TextureSet)null, vertices, offset, count);
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
    @Override
    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    @Override
    public Matrix4 getCombinedMatrix() {
        return this.combinedMatrix;
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

    @Override
    public ShaderProgram getShader() {
        return this.shader;
    }

    /**
     * NOT IMPLEMENTED API
     */
    @Override public void setColor(Color tint) {}
    @Override public void setColor(float r, float g, float b, float a) {}
    @Override public Color getColor() {return null;}
    @Override public void setColor(float color) {}
    @Override public float getPackedColor() {return 0;}
    @Override public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {}
    @Override public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {}
    @Override public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {}
    @Override public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {}
    @Override public void draw(Texture texture, float x, float y) {}
    @Override public void draw(Texture texture, float x, float y, float width, float height) {}
    @Override public void draw(TextureRegion region, float x, float y) {}
    @Override public void draw(TextureRegion region, float x, float y, float width, float height) {}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {}
    @Override public void draw(TextureRegion region, float width, float height, Affine2 transform) {}
    @Override public void disableBlending() {}
    @Override public void enableBlending() {}
    @Override public void setBlendFunction(int srcFunc, int dstFunc) {}
    @Override public int getBlendSrcFunc() {return 0;}
    @Override public int getBlendDstFunc() {return 0;}
    @Override public Matrix4 getProjectionMatrix() {return null;}
    @Override public Matrix4 getTransformMatrix() {return null;}
    @Override public void setProjectionMatrix(Matrix4 projection) {}
    @Override public void setTransformMatrix(Matrix4 transform) {}
    @Override public void setShader(ShaderProgram shader) {}
    @Override public boolean isBlendingEnabled() {return false;}
    @Override public boolean isDrawing() {return false;}
}
