package com.thommil.libgdx.runtime.graphics.renderer.buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * FBO offscreen renderer
 *
 * @author thommil on 03/02/16.
 */
public class OffScreenRenderer implements Disposable{

    public static final int VERTEX_SIZE = 4;
    public static final int VERTEX_COUNT = 6;
    public static final int SIZE = VERTEX_COUNT * VERTEX_SIZE;

    protected final Mesh mesh;
    protected final float[] vertices;
    protected int verticesSize;
    protected final ShaderProgram shader;

    /**
     * The frame buffer use for off screen rendering
     */
    protected FrameBuffer frameBuffer;

    /**
     * The FBO color format
     */
    protected final Pixmap.Format textureFormat;

    /**
     * The viewport used for rendering FBO
     */
    private final Viewport viewport;

    /**
     * Indicates if the FBO must be resized
     */
    private boolean hasResized;

    /**
     * Indicates if the FBO uses a single pass cache
     */
    private final boolean singlePass;

    /**
     * Indicates if the FBO uses a single pass cache
     */
    private boolean mustRedraw = true;

    /**
     * Default constructor using RGBA4444 for FBO texture format
     */
    public OffScreenRenderer(final Viewport viewport) {
        this(viewport, Pixmap.Format.RGBA4444, false);
    }

    /**
     * Constructor with specific color format
     */
    public OffScreenRenderer(final Viewport viewport, final Pixmap.Format textureFormat) {
        this(viewport, textureFormat, false);
    }

    /**
     * Constructor with specific color format and Single pass support (use invalidate() for redraw)
     */
    public OffScreenRenderer(final Viewport viewport, final Pixmap.Format textureFormat, final boolean singlePass) {
        this.viewport = viewport;
        this.mesh = this.createMesh();
        this.vertices = this.createVertices();
        this.shader = this.createShader();
        this.textureFormat = textureFormat;
        this.singlePass = singlePass;
        this.hasResized = true;
    }

    /**
     * Called when the screen/view size changes to resize the inner FBO
     */
    public void onResize(final int width, final int height) {
        this.hasResized = true;
    }

    /**
     * Called at beginning of rendering
     */
    public void begin() {
        if(mustRedraw) {
            this.setupFramebuffer();
            this.frameBuffer.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    /**
     * Setup FBO
     */
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
            this.frameBuffer = new FrameBuffer(this.textureFormat, this.viewport.getScreenWidth(), this.viewport.getScreenHeight(), false);

            //Vertices XY
            //0 & 5
            this.vertices[0] = this.vertices[20] = -this.viewport.getWorldWidth()/2f;
            this.vertices[1] = this.vertices[21] = this.viewport.getWorldHeight()/2f;
            //1
            this.vertices[4] = -this.viewport.getWorldWidth()/2f;
            this.vertices[5] = -this.viewport.getWorldHeight()/2f;
            //2 & 3
            this.vertices[8] = this.vertices[12] = this.viewport.getWorldWidth()/2f;
            this.vertices[9] = this.vertices[13] = -this.viewport.getWorldHeight()/2f;
            //4
            this.vertices[16] = this.viewport.getWorldWidth()/2f;
            this.vertices[17] = this.viewport.getWorldHeight()/2f;

            this.mesh.setVertices(vertices);
            this.hasResized = false;
        }
    }

    /**
     * Invalidate current offscreen texture
     */
    public void invalidate(){
        this.mustRedraw = true;
    }

    /**
     * Indicates if the offscreen renderer must be redrawn
     */
    public boolean mustRedraw() {
        return mustRedraw;
    }

    /**
     * Called at ending of rendering
     *
     * @param restoreViewport The viewport to restore at end of rendering
     */
    public void end() {
        if(mustRedraw) {
            this.frameBuffer.end(this.viewport.getScreenX(), this.viewport.getScreenY(), this.viewport.getScreenWidth(), this.viewport.getScreenHeight());
            mustRedraw = !singlePass;
        }

        this.shader.begin();
        this.shader.setUniformMatrix("u_projTrans", this.viewport.getCamera().combined);
        this.frameBuffer.getColorBufferTexture().bind(0);
        this.shader.setUniformi(TextureSet.UNIFORM_TEXTURE_0, 0);
        this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, VERTEX_COUNT);

        this.shader.end();
    }


    /**
     * Subclasses should override this method to use their specific Mesh
     */
    protected Mesh createMesh() {
        final Mesh mesh = new Mesh(false, VERTEX_COUNT, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        return mesh;
    }

    /**
     * Subclasses should override this method to use their specific vertices
     */
    protected float[] createVertices() {
        this.verticesSize = VERTEX_SIZE;
        return new float[SIZE];
    }

    /**
     * Subclasses should override this method to use their specific shaders
     */
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
                + "uniform sampler2D "+TextureSet.UNIFORM_TEXTURE_0+";\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = texture2D("+TextureSet.UNIFORM_TEXTURE_0+", v_texCoords);\n" //
                + "}";

        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.shader.dispose();
        this.mesh.dispose();
        this.frameBuffer.dispose();
    }

}
