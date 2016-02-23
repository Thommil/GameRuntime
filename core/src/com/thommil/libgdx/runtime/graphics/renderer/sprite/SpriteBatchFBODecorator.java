package com.thommil.libgdx.runtime.graphics.renderer.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;

/**
 * SpriteBatchRenderer using FBOs
 *
 * The default version is just a white mask
 *
 * Created by thommil on 2/23/16.
 */
public class SpriteBatchFBODecorator extends SpriteBatchRenderer{

    private static final int DECORATOR_VERTEX_SIZE = 4;
    private static final int DECORATOR_FBO_SIZE = 4 * DECORATOR_VERTEX_SIZE;
    private static final int DECORATOR_SIZE = 16;

    /**
     * Decorated SpriteBatchRenderer
     */
    protected final SpriteBatchRenderer decoratedSpriteBatchRenderer;

    /**
     * The frame buffer use for off screen rendering
     */
    protected FrameBuffer frameBuffer;

    /**
     * FBO screen size
     */
    private int screenWidth, screenHeight;

    /**
     * FBO view size
     */
    private float viewWidth, viewHeight;

    /**
     * Indicates if the FBO must be resized
     */
    private boolean hasResized;

    /**
     * Default constructor using the decorated SpriteBatchRenderer
     *
     * @param decoratedSpriteBatchRenderer The decorated SpriteBatchRenderer
     */
    public SpriteBatchFBODecorator(final SpriteBatchRenderer decoratedSpriteBatchRenderer) {
        super(DECORATOR_SIZE);
        this.decoratedSpriteBatchRenderer = decoratedSpriteBatchRenderer;
    }

    /**
     * Must be called when the FBO needs to be resized
     */
    public void onResize(int screenWidth, int screenHeight, float viewWidth, float viewHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.hasResized = true;
    }

    /**
     * Called at beginning of rendering
     */
    @Override
    public void begin() {
        this.decoratedSpriteBatchRenderer.begin();
        this.setupFramebuffer();
        this.frameBuffer.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void setupFramebuffer(){
        //First init
        if(this.frameBuffer == null){
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
            this.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA4444, this.screenWidth, this.screenHeight, false);

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

            this.mesh.setVertices(vertices);
            this.hasResized = false;
        }
    }

    /**
     * Generic method to draw a set of vertices.
     *
     * @param vertices The list of vertices to draw
     */
    @Override
    public void draw(float[] vertices) {
        this.decoratedSpriteBatchRenderer.draw(vertices);
    }

    /**
     * Called at ending of rendering
     */
    @Override
    public void end() {
        this.decoratedSpriteBatchRenderer.end();
        this.frameBuffer.end();

        this.shader.begin();
        this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
        this.frameBuffer.getColorBufferTexture().bind(0);
        this.shader.setUniformi("u_texture", 0);
        this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, 6);
        this.shader.end();
    }

    /**
     * Subclasses should override this method to use their specific Mesh
     *
     * @param size
     */
    @Override
    protected Mesh createMesh(int size) {
        final Mesh mesh = new Mesh(false, 6, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        return mesh;
    }

    /**
     * Subclasses should override this method to use their specific vertices
     *
     * @param size
     */
    @Override
    protected float[] createVertices(int size) {
        this.verticesSize = DECORATOR_VERTEX_SIZE;
        return new float[DECORATOR_SIZE * DECORATOR_FBO_SIZE];
    }

    /**
     * Subclasses should override this method to use their specific shaders
     */
    @Override
    protected ShaderProgram createShader() {
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
                + "  gl_FragColor = vec4(1.0,1.0,1.0,color.a);\n" //
                + "}";

        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }


}
