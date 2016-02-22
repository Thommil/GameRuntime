package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.batch.ParticlesBatch;

/**
 * Created by tomtom on 16/02/16.
 */
public class WaterBatch extends ParticlesBatch {

    private final float particlesScaleFactor = 2f;

    final ShaderProgram waterShader;
    final Mesh waterMesh;
    final float[] vertices = new float[24];
    FrameBuffer fbo;
    private int screenWidth, screenHeight;
    private float viewWidth, viewHeight;
    boolean hasResized;

    public WaterBatch() {
        super(5000);
        this.waterMesh = this.createWaterMesh();
        this.waterShader = this.createWaterShader();
    }

    public void onResize(int screenWidth, int screenHeight, float viewWidth, float viewHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.hasResized = true;
        this.particlesScale *= this.particlesScaleFactor;
    }

    @Override
    public void begin() {
        //First render off particles
        super.begin();
        this.setupFramebuffer();
        this.fbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void setupFramebuffer(){
        //First init
        if(this.fbo == null){
            //Vertices UV
            //0 & 5
            this.vertices[2] = this.vertices[22] = 0f;
            this.vertices[3] = this.vertices[23] = 1f;
            //1
            this.vertices[6] = 0f;
            this.vertices[7] = 0f;
            //2 & 3
            this.vertices[10] = this.vertices[14] = 1f;
            this.vertices[11] = this.vertices[15] = 0f;
            //4
            this.vertices[18] = 1f;
            this.vertices[19] = 1f;
        }
        //Resize
        if(this.hasResized) {
            this.fbo = new FrameBuffer(Pixmap.Format.RGBA4444, this.screenWidth, this.screenHeight, false);

            //Vertices XY
            //0 & 5
            this.vertices[0] = this.vertices[20] = -this.viewWidth/2f;
            this.vertices[1] = this.vertices[21] = this.viewHeight/2f;
            //1
            this.vertices[4] = -this.viewWidth/2f;
            this.vertices[5] = -this.viewHeight/2f;
            //2 & 3
            this.vertices[8] = this.vertices[12] = this.viewWidth/2f;
            this.vertices[9] = this.vertices[13] = -this.viewHeight/2f;
            //4
            this.vertices[16] = this.viewWidth/2f;
            this.vertices[17] = this.viewHeight/2f;

            this.waterMesh.setVertices(vertices);
            this.hasResized = false;
        }
    }

    @Override
    public void draw(float[] vertices, float radius) {
        //Currently rendering on FBO
        super.draw(vertices, radius);

        //End rendering on FBO
        this.fbo.end();
        super.end();

        //The apply second pass to render water on screen
        this.waterShader.begin();
        this.waterShader.setUniformMatrix("u_projTrans", this.combinedMatrix);
        this.fbo.getColorBufferTexture().bind(0);
        this.waterShader.setUniformi("u_texture", 0);
        this.waterMesh.render(this.waterShader, GL20.GL_TRIANGLES, 0, 6);
    }

    @Override
    public void end() {
        //End rendering on screen
        this.waterShader.end();
    }

    protected Mesh createWaterMesh(){
        final Mesh mesh = new Mesh(false, 6, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        return mesh;
    }

    public ShaderProgram createWaterShader(){
        final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
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
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  vec4 color = texture2D(u_texture, v_texCoords);\n" //
                + "  if( color.r < 0.15){\n" //
                + "     color = vec4(0,0,0,0);\n" //
                + "  }\n" //
                + "  else if( color.r < 0.3){\n" //
                + "     color = vec4(0.7,0.7,1.0,0.5);\n" //
                + "  }\n" //
                + "  else{\n" //
                + "     color = vec4(0.0,0.0,1.0,0.3);\n" //
                + "  }\n" //
                + "  gl_FragColor = color;\n" //
                + "}";

        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.waterMesh.dispose();
        this.waterShader.dispose();
    }
}
