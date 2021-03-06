package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * Extension of ParticlesBatchRenderer with Textured particles
 *
 * @author thommil on 03/02/16.
 */
public class TexturedParticlesBatchRenderer extends ParticlesBatchRenderer {

    protected TextureSet lastTextureSet;
    protected int currentTextureSetSize = 0;
    protected final TextureSet tmpTextureSet = new TextureSet(1);

    public TexturedParticlesBatchRenderer(final int maxParticles) {
        super(maxParticles);
    }

    /**
     * Draws a rectangle using the given vertices. There must be 4 vertices, each made up of 5 elements in this order: x, y, color,
     * u, v. The {@link #getColor()} from the Batch is not applied.
     */
    @Override
    public void draw(Texture texture, float[] vertices, int offset, int count) {
        int remainingVertices = this.vertices.length;
        if (this.currentParticlesRadius != this.lastParticlesRadius) {
            if(this.lastTextureSet == null){
                this.tmpTextureSet.textures[0] = texture;
                this.lastTextureSet = tmpTextureSet;
            }
            this.flush();
            this.lastParticlesRadius = this.currentParticlesRadius;
        }
        else if (this.lastTextureSet == null || texture != this.lastTextureSet.textures[0]) {
            this.flush();
            this.tmpTextureSet.textures[0] = texture;
            this.lastTextureSet = tmpTextureSet;
        }
        else {
            remainingVertices -= this.idx;
            if (remainingVertices == 0) {
                this.flush();
                remainingVertices = this.vertices.length;
            }
        }
        this.copyAndFlush(vertices, Math.min(remainingVertices, count), offset, count);
    }

    /**
     * Draw generic method with textureSet support
     *
     * @param textureSet The textureset to draw
     * @param vertices   The vertices to add to the Batch
     * @param offset     The offset in vertices array
     * @param count      The number of vertices to add
     */
    @Override
    public void draw(TextureSet textureSet, float[] vertices, int offset, int count) {
        if(textureSet.textures.length == 1) {
            this.draw(textureSet.textures[0], vertices, offset, count);
        }
        else {
            int remainingVertices = this.vertices.length;
            if (this.currentParticlesRadius != this.lastParticlesRadius) {
                if(this.lastTextureSet == null){
                    this.lastTextureSet = textureSet;
                }
                this.flush();
                this.lastParticlesRadius = this.currentParticlesRadius;
            }
            else if (textureSet != this.lastTextureSet) {
                this.flush();
                this.lastTextureSet = textureSet;
            } else {
                remainingVertices -= this.idx;
                if (remainingVertices == 0) {
                    this.flush();
                    remainingVertices = this.vertices.length;
                }
            }
            this.copyAndFlush(vertices, Math.min(remainingVertices, count), offset, count);
        }
    }

    private void copyAndFlush(float[] vertices, int copyCount, int offset, int count){
        System.arraycopy(vertices, offset, this.vertices, this.idx, copyCount);
        this.idx += copyCount;
        count -= copyCount;
        while (count > 0) {
            offset += copyCount;
            this.flush();
            copyCount = Math.min(this.vertices.length, count);
            System.arraycopy(vertices, offset, this.vertices, 0, copyCount);
            this.idx += copyCount;
            count -= copyCount;
        }
    }

    /**
     * Flushes the batch and renders all remaining vertices
     */
    @Override
    public void flush() {
        if(this.idx == 0) return;

        if(this.lastTextureSet.textures.length != this.currentTextureSetSize){
            this.lastTextureSet.setUniformAll(this.shader);
            this.currentTextureSetSize = this.lastTextureSet.textures.length;
        }
        this.lastTextureSet.bindAll();

        super.flush();
    }

    @Override
    protected ShaderProgram createShader () {
        String prefix = "";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            prefix += "#version 120\n";
        }
        else {
            prefix += "#version 100\n";
        }

        final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projectionViewMatrix;\n" //
                    + "uniform float radius;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";
         final String fragmentShader = "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" //
                    + "uniform sampler2D "+TextureSet.UNIFORM_TEXTURE_0+";\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = texture2D("+TextureSet.UNIFORM_TEXTURE_0+", gl_PointCoord);\n" //
                    + "}";


        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
