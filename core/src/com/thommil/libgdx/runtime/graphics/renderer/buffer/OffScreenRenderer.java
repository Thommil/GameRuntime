package com.thommil.libgdx.runtime.graphics.renderer.buffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.BatchRenderer;

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

    protected final Matrix4 combinedMatrix = new Matrix4();

    /**
     * The frame buffer use for off screen rendering
     */
    protected FrameBuffer frameBuffer;

    /**
     * The FBO color format
     */
    protected final Pixmap.Format textureFormat;

    /**
     * FBO size
     */
    private int screenWidth, screenHeight;

    /**
     * FBO view size
     */
    private float width, height;

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
    public OffScreenRenderer() {
        this(Pixmap.Format.RGBA4444, false, true);
    }

    /**
     * Constructor with specific color format, redraw and clear at each render call.
     */
    public OffScreenRenderer(final Pixmap.Format textureFormat) {
        this(textureFormat, false, true);
    }

    /**
     * Full constructor
     *
     * @param textureFormat Inner texture color format
     * @param singlePass If true, the FBO will be redrawn only after an invalidate() call
     * @param clearScreen If false, the FBO color buffer will not be cleared before each draw, useful to redraw only a part of buffer
     */
    public OffScreenRenderer(final Pixmap.Format textureFormat, final boolean singlePass, final boolean clearScreen) {
        this.mesh = this.createMesh();
        this.vertices = this.createVertices();
        this.shader = this.createShader();
        this.textureFormat = textureFormat;
        this.singlePass = singlePass;
        this.clearScreen = clearScreen;
    }

    /**
     * Called when the screen/view size changes to resize the inner FBO
     *
     * @param width The framebuffer width (in world units)
     * @param height The framebuffer height (in world units)
     * @param screenWidth The display area width in pixels
     * @param screenHeight The display area height in pixels
     */
    public void onResize(final float width, final float height, final int screenWidth, final int screenHeight) {
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.hasResized = true;
    }

    /**
     * Sets the combined matrix for rendering
     *
     * @param combinedMatrix the combined matrix
     */
    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    /**
     * Called at beginning of rendering
     */
    public void begin() {
        if(mustRedraw) {
            final boolean mustClearScreen = clearScreen || hasResized;
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
            this.frameBuffer = new FrameBuffer(this.textureFormat, this.screenWidth, this.screenHeight, false);

            //Vertices XY
            //0 & 5
            this.vertices[0] = this.vertices[20] = -this.width/2f;
            this.vertices[1] = this.vertices[21] = this.height/2f;
            //1
            this.vertices[4] = -this.width/2f;
            this.vertices[5] = -this.height/2f;
            //2 & 3
            this.vertices[8] = this.vertices[12] = this.width/2f;
            this.vertices[9] = this.vertices[13] = -this.height/2f;
            //4
            this.vertices[16] = this.width/2f;
            this.vertices[17] = this.height/2f;

            this.mesh.setVertices(vertices);
            this.hasResized = false;
        }
    }

    /**
     * Generic method to draw a set of vertices.
     *
     * @param viewport The viewport used to render the FBO
     */
    public void draw(final Viewport viewport) {
        if(mustRedraw) {
            this.frameBuffer.end(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
            mustRedraw = !singlePass;
        }

        this.shader.begin();
        this.shader.setUniformMatrix("u_projTrans", this.combinedMatrix);
        this.frameBuffer.getColorBufferTexture().bind(0);
        this.shader.setUniformi(TextureSet.UNIFORM_TEXTURE_0, 0);
        this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, VERTEX_COUNT);
    }

    /**
     * Draw the offscreen result in the specified view only
     *
     * @param viewport The viewport used to render the FBO
     * @param x The X coord of the view
     * @param y The Y coord of the view
     * @param width The width of the view
     * @param height The height of the view
     */
    public void draw(final Viewport viewport, final float x, final float y, final float width, final float height){
        //Vertices XY
        //0 & 5
        this.vertices[0] = this.vertices[20] = x;
        this.vertices[1] = this.vertices[21] = y + height;
        //1
        this.vertices[4] = x;
        this.vertices[5] = y;
        //2 & 3
        this.vertices[8] = this.vertices[12] = x + width;
        this.vertices[9] = this.vertices[13] = y;
        //4
        this.vertices[16] = x + width;
        this.vertices[17] = y + height;

        this.mesh.setVertices(vertices);
        this.draw(viewport);
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
     */
    public void end() {
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
