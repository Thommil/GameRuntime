package com.thommil.libgdx.runtime.test.test_14_sunset.level;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.layer.Layer;


public class SunsetLayer extends Layer {

    protected SunsetRenderer sunsetRenderer;

    public SunsetLayer(Viewport viewport) {
        super(viewport, 1);
        this.sunsetRenderer = new SunsetRenderer(-viewport.getWorldWidth()/2,-viewport.getWorldHeight()/2,viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    public void setTime(final float time){
        this.sunsetRenderer.setTime(time);
    }

    @Override
    protected void onShow() {}

    @Override
    protected void onResize(int width, int height) {}

    @Override
    protected void onHide() {}

    @Override
    public void render(float deltaTime) {
        this.sunsetRenderer.render(this.viewport.getCamera().combined);
    }

    @Override
    public void dispose() {
        sunsetRenderer.dispose();
    }

    public static class SunsetRenderer implements Disposable{

        public static final int VERTEX_SIZE = 3;
        public static final int VERTEX_COUNT = 6;
        public static final int SIZE = VERTEX_COUNT * VERTEX_SIZE;

        protected final Mesh mesh;
        protected final float[] vertices;
        protected final ShaderProgram shader;

        protected float time = 0;

        public SunsetRenderer(final float x, final float y, final float width, final float height) {
            this.shader = this.createShader();
            this.mesh = this.createMesh();
            this.vertices = this.createVertices();
            this.vertices[0] = this.vertices[15] = x;
            this.vertices[1] = this.vertices[16] = y + height;
            this.vertices[2] = this.vertices[17] = new Color(0.1f, 0.1f, 0.3f, 1f).toFloatBits();

            this.vertices[3] = x;
            this.vertices[4] = y;
            this.vertices[5] = Color.BLACK.toFloatBits();

            this.vertices[6] = this.vertices[9] = x + width;
            this.vertices[7] = this.vertices[10] = y;
            this.vertices[8] = this.vertices[11] = Color.BLACK.toFloatBits();

            this.vertices[12] = x + width;
            this.vertices[13] = y + height;
            this.vertices[14] = new Color(0.1f, 0.1f, 0.3f, 1f).toFloatBits();

            this.setTime(0);
        }

        public void setTime(final float time){
            this.time = time;

            this.mesh.setVertices(this.vertices);
        }

        public void render(final Matrix4 combined){
            this.shader.begin();
            this.shader.setUniformMatrix("u_projectionViewMatrix", combined);
            this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, VERTEX_COUNT);
            this.shader.end();
        }

        protected Mesh createMesh() {
            return new Mesh(true, VERTEX_COUNT, 0, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                    ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
        }

        protected float[] createVertices() {
            return new float[SIZE];
        }

        protected ShaderProgram createShader() {
            final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projectionViewMatrix;\n" //
                    + "varying vec4 v_color;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                    + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "}\n";
            final String fragmentShader = "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" //
                    + "varying vec4 v_color;\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + "  gl_FragColor = v_color;\n" //
                    + "}";

            final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
            if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
            return shader;
        }

        @Override
        public void dispose() {
            this.shader.dispose();
        }
    }
}
/*

// License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.

// The input, n, should have a magnitude in the approximate range [0, 100].
// The output is pseudo-random, in the range [0,1].
float Hash( float n )
{
	return fract( (1.0 + cos(n)) * 415.92653);
}

float Noise2d( in vec2 x )
{
    float xhash = Hash( x.x * 37.0 );
    float yhash = Hash( x.y * 57.0 );
    return fract( xhash + yhash );
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    // Add a camera offset in "FragCoord-space".
    vec2 vCameraOffset = iMouse.xy;
    vec2 vSamplePos = ( fragCoord.xy + floor( vCameraOffset ) ) / iResolution.xy;

    vec3 vColor  = vec3(0.0, 0.0, 0.0);

	// Sky Background Color
	vColor += vec3( 0.1, 0.2, 0.4 ) * vSamplePos.y;

    // Stars
    // Note: Choose fThreshhold in the range [0.99, 0.9999].
    // Higher values (i.e., closer to one) yield a sparser starfield.
    float fThreshhold = 0.97;
    float StarVal = Noise2d( vSamplePos );
    if ( StarVal >= fThreshhold )
    {
        StarVal = pow( (StarVal - fThreshhold)/(1.0 - fThreshhold), 6.0 );
		vColor += vec3( StarVal );
    }

	fragColor = vec4(vColor, 1.0);
}


 */