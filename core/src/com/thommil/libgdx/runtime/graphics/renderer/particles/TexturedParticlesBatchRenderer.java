package com.thommil.libgdx.runtime.graphics.renderer.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;

/**
 * Extension of ParticlesBatchRenderer with Textured particles
 *
 * Created by thommil on 2/22/16.
 */
public class TexturedParticlesBatchRenderer extends ParticlesBatchRenderer {

    protected Texture lastTexture;
    protected Texture currentTexture;

    public TexturedParticlesBatchRenderer(final int maxParticles) {
        super(maxParticles);
    }

    /**
     * Sets current texture
     *
     * @param texture The current texture to use
     */
    public void setTexture(final Texture texture){
        this.currentTexture = texture;
    }

    @Override
    public void begin() {
        super.begin();
        this.shader.setUniformi("u_texture", 0);
    }

    @Override
    public void end() {
        super.end();
        this.lastTexture = this.currentTexture = null;
    }

    @Override
    public void draw(float[] vertices) {
         if (this.currentTexture != this.lastTexture) {
             flush();
             this.lastTexture = this.currentTexture;
         }
         super.draw(vertices);
    }

    @Override
    public void flush() {
        this.lastTexture.bind();
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
                    + "uniform sampler2D u_texture;\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = texture2D(u_texture, gl_PointCoord);\n" //
                    + "}";


        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
