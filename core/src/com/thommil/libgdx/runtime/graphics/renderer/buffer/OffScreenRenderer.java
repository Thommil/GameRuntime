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
    public static final int VERTEX_COUNT = 4;
    public static final int SIZE = VERTEX_COUNT * VERTEX_SIZE;

    protected final Mesh mesh;
    protected final float[] vertices;
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
     * Indicates if the FBO color buffer must be cleared at each redraw
     */
    private boolean clearScreen = true;

    /**
     * Default constructor using RGBA4444 for FBO texture format, redraw and clear at each render call.
     */
    public OffScreenRenderer(final Viewport viewport) {
        this(viewport, Pixmap.Format.RGBA4444, false, true);
    }

    /**
     * Constructor with specific color format, redraw and clear at each render call.
     */
    public OffScreenRenderer(final Viewport viewport, final Pixmap.Format textureFormat) {
        this(viewport, textureFormat, false, true);
    }

    /**
     * Full constructor
     *
     * @param textureFormat Inner texture color format
     * @param singlePass If true, the FBO will be redrawn only after an invalidate() call
     * @param clearScreen If false, the FBO color buffer will not be cleared before each draw, useful to redraw only a part of buffer
     */
    public OffScreenRenderer(final Viewport viewport, final Pixmap.Format textureFormat, final boolean singlePass, final boolean clearScreen) {
        this.viewport = viewport;
        this.mesh = this.createMesh();
        this.vertices = this.createVertices();
        this.shader = this.createShader();
        this.textureFormat = textureFormat;
        this.singlePass = singlePass;
        this.clearScreen = clearScreen;
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
        if(this.mustRedraw) {
            final boolean mustClearScreen = this.clearScreen || this.hasResized;
            this.setupFramebuffer();
            this.frameBuffer.begin();
            if(mustClearScreen) {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
        }
    }

    /**
     * Get the current offscreen image in a Texture.
     * Can be retrieved to reinject the result in another renderer instead of calling
     * inner draw methods.
     *
     * @return The current offscreen image
     */
    public Texture getOffscreenTexture(){
        return this.frameBuffer.getColorBufferTexture();
    }

    /**
     * Setup FBO
     */
    private void setupFramebuffer(){
        //First init
        if(this.frameBuffer == null){
            //Vertices UV
            //0 & 5
            this.vertices[2] = 0f;
            this.vertices[3] = 1f;
            //1
            this.vertices[6] = 0f;
            this.vertices[7] = 0f;
            //2 & 3
            this.vertices[10] = 1f;
            this.vertices[11] = 0f;
            //4
            this.vertices[14] = 1f;
            this.vertices[15] = 1f;
        }
        //Resize
        if(this.hasResized) {
            this.frameBuffer = new FrameBuffer(this.textureFormat, this.viewport.getScreenWidth(), this.viewport.getScreenHeight(), false);

            //Vertices XY
            //0 & 5
            this.vertices[0] = -this.viewport.getWorldWidth()/2f;
            this.vertices[1] = this.viewport.getWorldHeight()/2f;
            //1
            this.vertices[4] = -this.viewport.getWorldWidth()/2f;
            this.vertices[5] = -this.viewport.getWorldHeight()/2f;
            //2 & 3
            this.vertices[8] = this.viewport.getWorldWidth()/2f;
            this.vertices[9] = -this.viewport.getWorldHeight()/2f;
            //4
            this.vertices[12] = this.viewport.getWorldWidth()/2f;
            this.vertices[13] = this.viewport.getWorldHeight()/2f;

            this.mesh.setVertices(this.vertices);
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
        return this.mustRedraw;
    }

    /**
     * Called at ending of rendering
     *
     * @param restoreViewport The viewport to restore at end of rendering
     */
    public void end() {
        if(this.mustRedraw) {
            this.frameBuffer.end(this.viewport.getScreenX(), this.viewport.getScreenY(), this.viewport.getScreenWidth(), this.viewport.getScreenHeight());
            this.mustRedraw = !this.singlePass;
        }

        this.shader.begin();
        this.shader.setUniformMatrix("u_projectionViewMatrix", this.viewport.getCamera().combined);
        this.frameBuffer.getColorBufferTexture().bind(0);
        this.shader.setUniformi(TextureSet.UNIFORM_TEXTURE_0, 0);
        this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, 6);

        this.shader.end();
    }


    /**
     * Subclasses should override this method to use their specific Mesh
     */
    protected Mesh createMesh() {
        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }
        final Mesh mesh = new Mesh(vertexDataType, true, 4, 6,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        final short[] indices = new short[6];
        short j = 0;
        for (int i = 0; i < 6; i += 6, j += 4) {
            indices[i] = j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = j;
        }
        mesh.setIndices(indices);

        return mesh;
    }

    /**
     * Subclasses should override this method to use their specific vertices
     */
    protected float[] createVertices() {
        return new float[SIZE];
    }

    /**
     * Subclasses should override this method to use their specific shaders
     */
    protected ShaderProgram createShader() {
        final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projectionViewMatrix;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
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
        if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
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
