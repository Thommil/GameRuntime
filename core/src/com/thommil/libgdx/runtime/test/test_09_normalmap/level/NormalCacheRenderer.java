package com.thommil.libgdx.runtime.test.test_09_normalmap.level;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.cache.SpriteCacheRenderer;

public class NormalCacheRenderer extends SpriteCacheRenderer{

    final float[] lightData = new float[]{0,0,1,1};

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
        this.shader.setUniform4fv("u_lightData",lightData,0,4);
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
                + "uniform vec4 u_lightData;\n" //
                + "const vec3 LightColor = vec3(1.0, 0.8, 0.4);\n" //
                + "const vec3 Falloff = vec3(0.3, 4.0, 20.0);\n" //
                + "const vec3 AmbientColor = vec3(0.3, 0.3, 0.3);\n" //
                + "void main()\n"//
                + "{\n" //
                + "     vec4 DiffuseColor = texture2D("+ TextureSet.UNIFORM_TEXTURE_0+", v_texCoords);\n" //
                + "     vec3 NormalMap = texture2D("+ TextureSet.UNIFORM_TEXTURE_1+", v_texCoords).rgb;\n" //
                + "     NormalMap.g = 1.0 - NormalMap.g; //COMMENT/UNCOMMENT depending on normals generator \n" //
                + "     vec3 LightDir = vec3((u_lightData.xy - gl_FragCoord.xy)/u_lightData.ba , 0.0);\n" //
                + "     LightDir.x *= u_lightData.b / u_lightData.a;\n" //
                + "     float D = length(LightDir);\n" //
                + "     vec3 N = normalize(NormalMap * 2.0 - 1.0);\n" //
                + "     vec3 L = normalize(LightDir);\n" //
                + "     vec3 Diffuse = LightColor.rgb * max(dot(N, L), 0.0);\n" //
                + "     float Attenuation = 1.0 / ( Falloff.x + (Falloff.y*D) + (Falloff.z*D*D) );\n" //
                + "     vec3 Intensity = AmbientColor + Diffuse * Attenuation;\n" //
                + "     vec3 FinalColor = DiffuseColor.rgb * Intensity;\n" //
                + "     gl_FragColor = v_color * vec4(FinalColor, DiffuseColor.a);\n" //
                + "}";
        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
