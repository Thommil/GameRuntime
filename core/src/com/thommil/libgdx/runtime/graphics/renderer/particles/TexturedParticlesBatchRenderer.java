package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * Extension of ParticlesBatchRenderer with Textured particles
 *
 * @author thommil on 03/02/16.
 */
public class TexturedParticlesBatchRenderer extends ParticlesBatchRenderer {

    protected TextureSet lastTextureSet;
    protected TextureSet currentTextureSet;
    protected int currentTextureSetSize = 0;

    public TexturedParticlesBatchRenderer(final int maxParticles) {
        super(maxParticles);
    }

    /**
     * Sets current TextureSet
     *
     * @param textureSet The current TextureSet to use
     */
    public void setTextureSet(final TextureSet textureSet){
        this.currentTextureSet = textureSet;
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void end() {
        super.end();
    }

    @Override
    public void draw(float[] vertices) {
         if (this.currentTextureSet != this.lastTextureSet) {
             flush();
             this.lastTextureSet = this.currentTextureSet;
         }
         super.draw(vertices);
    }

    @Override
    public void flush() {
        if(idx == 0) return;

        if(this.lastTextureSet.textures.length != currentTextureSetSize){
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
                    + "uniform sampler2D "+TextureSet.UNIFORM_TEXTURE_0+";\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = texture2D("+TextureSet.UNIFORM_TEXTURE_0+", gl_PointCoord);\n" //
                    + "}";


        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
