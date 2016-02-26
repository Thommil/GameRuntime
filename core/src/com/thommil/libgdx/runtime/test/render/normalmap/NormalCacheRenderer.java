package com.thommil.libgdx.runtime.test.render.normalmap;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.cache.CacheRenderer;

/**
 * Created by tomtom on 26/02/16.
 */
public class NormalCacheRenderer extends CacheRenderer{

    final float[] lightData = new float[4];

    public NormalCacheRenderer(int size) {
        super(size);
    }

    public void setLightPosition(final int x, final int y){
        lightData[0] = x;
        lightData[1] = lightData[3]-y;
    }

    public void setScreenSize(final int width, final int height){
        lightData[0] = lightData[0] * width/lightData[2];
        lightData[1] = lightData[1] * height/lightData[3];
        lightData[2] = width;
        lightData[3] = height;
    }

    /**
     * Prepares the OpenGL state for CacheRenderer rendering.
     */
    @Override
    public void begin() {
        super.begin();
        this.shader.setUniform4fv("u_LightPos",lightData,0,4);
    }

    /**
     * Subclasses should override this method to use their specific vertices
     */
    @Override
    protected ShaderProgram createShader() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "uniform mat4 u_projectionViewMatrix;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";

        String fragmentShader = "#ifdef GL_ES\n" //
                + "precision mediump float;\n" //
                + "#endif\n" //
                + "varying vec2 v_texCoords;\n" //
                + "varying vec4 v_color;\n" //
                + "uniform sampler2D "+ TextureSet.UNIFORM_TEXTURE_0+";\n" //
                + "uniform sampler2D "+ TextureSet.UNIFORM_TEXTURE_1+";\n" //
                + "uniform vec4 u_LightPos;\n" //
                + "const vec4 LightColor = vec4(0.8, 0.6, 0.6, 1.0);\n" //
                + "const vec3 Falloff = vec3(0.2, 1.0, 10.0);\n" //
                + "const vec4 AmbientColor = vec4(0.8, 0.6, 0.6, 0.4);\n" //
                + "void main()\n"//
                + "{\n" //
                + "     vec4 DiffuseColor = texture2D("+ TextureSet.UNIFORM_TEXTURE_0+", v_texCoords);\n" //
                + "     vec3 NormalMap = texture2D("+ TextureSet.UNIFORM_TEXTURE_1+", v_texCoords).rgb;\n" //
                + "     //NormalMap.g = 1.0 - NormalMap.g;\n" //
                + "     vec3 LightDir = vec3((u_LightPos.xy - gl_FragCoord.xy)/u_LightPos.ba , 0.0);\n" //
                + "     LightDir.x *= u_LightPos.g / u_LightPos.r;\n" //
                + "     float D = length(LightDir);\n" //
                + "     vec3 N = normalize(NormalMap * 2.0 - 1.0);\n" //
                + "     vec3 L = normalize(LightDir);\n" //
                + "     vec3 Diffuse = (LightColor.rgb * LightColor.a) * max(dot(N, L), 0.0);\n" //
                + "     vec3 Ambient = AmbientColor.rgb * AmbientColor.a;\n" //
                + "     float Attenuation = 1.0 / ( Falloff.x + (Falloff.y*D) + (Falloff.z*D*D) );\n" //
                + "     vec3 Intensity = Ambient + Diffuse * Attenuation;\n" //
                + "     vec3 FinalColor = DiffuseColor.rgb * Intensity;\n" //
                + "     gl_FragColor = v_color * vec4(FinalColor, DiffuseColor.a);\n" //
                + "}";
        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
