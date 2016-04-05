package com.thommil.libgdx.runtime.test.test_14_sunset.level;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.layer.Layer;
import com.thommil.libgdx.runtime.tools.GL11;


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

    float time=0;

    @Override
    public void render(float deltaTime) {
        this.sunsetRenderer.setTime(time);
        this.sunsetRenderer.render(this.viewport.getCamera().combined);
            time+=0.001;
    }

    @Override
    public void dispose() {
        sunsetRenderer.dispose();
    }

    public static class SunsetRenderer implements Disposable{

        public static final int VERTEX_SIZE = 3;
        public static final int VERTEX_COUNT = 4;
        public static final int SIZE = VERTEX_COUNT * VERTEX_SIZE;

        protected final Mesh mesh;
        protected final float[] vertices;
        protected final ShaderProgram shader;

        protected final Color topColor = new Color();
        protected final Color bottomColor = new Color();

        public static final float MIDNIGHT = 0f;
        public static final float SUNRISE = 0.5f;
        public static final float NOON = 1f;

        private static final Color midnightTopColor = new Color(0x090a0fff);
        private static final Color midnightBottomColor = new Color(0x0f204bff);

        private static final Color noonTopColor = new Color(0x7aacf1ff);
        private static final Color noonBottomColor = new Color(0xffffffff);


        protected float starsAlpha = 1f;


        public SunsetRenderer(final float x, final float y, final float width, final float height) {
            this.shader = this.createShader();
            this.mesh = this.createMesh();
            this.vertices = this.createVertices();
            this.vertices[0] = x;
            this.vertices[1] = y + height;
            this.vertices[2] = 0;

            this.vertices[3] = x;
            this.vertices[4] = y;
            this.vertices[5] = 0;

            this.vertices[6] = x + width;
            this.vertices[7] = y;
            this.vertices[8] = 0;

            this.vertices[9] = x + width;
            this.vertices[10] = y + height;
            this.vertices[11] = 0;

            this.setTime(MIDNIGHT);
        }

        public void setTime(final float time) {
            if (time < NOON){
                topColor.set(midnightTopColor);
                bottomColor.set(midnightBottomColor);
                topColor.lerp(noonTopColor, time);
                bottomColor.lerp(noonBottomColor, time);
                this.starsAlpha = 1 - time ;
            }
            this.vertices[2] = this.vertices[11] = topColor.toFloatBits();
            this.vertices[5] = this.vertices[8] = bottomColor.toFloatBits();
            this.mesh.setVertices(this.vertices);
        }

        public void render(final Matrix4 combined){
            this.shader.begin();
            this.shader.setUniformMatrix("u_projectionViewMatrix", combined);
            //this.shader.setUniformf("u_starsAlpha", starsAlpha);
            this.mesh.render(this.shader, GL20.GL_TRIANGLES, 0, 6);
            this.shader.end();
        }

        protected Mesh createMesh() {
            Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexArray;
            if (Gdx.gl30 != null) {
                vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
            }
            final Mesh mesh = new Mesh(vertexDataType, true, 4, 6,
                    new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
                    new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));

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
                    + "uniform float u_starsAlpha;\n" //
                    + "\n" //
                    + "varying vec4 v_color;\n" //
                    + "\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + "   gl_FragColor = v_color;\n" //
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