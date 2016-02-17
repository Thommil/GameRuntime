package com.thommil.libgdx.runtime.graphics.batch;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;

/**
 * Custom Batch for LiquidFun ParticleSystem (SoftBody).
 *
 * Created by thommil on 2/10/16.
 */
public class SoftBodyBatch {

    protected final Mesh mesh;
    protected final ShaderProgram shader;

    protected final Texture texture;
    protected float particlesScale = 1f;

    public SoftBodyBatch() {
        this(null, 10000);
    }

    public SoftBodyBatch(final Texture texture){
            this(texture, 10000);
    }

    public SoftBodyBatch(final int size) {
        this(null, size);
    }

    public SoftBodyBatch(final Texture texture, final int size) {
        this.texture = texture;
        mesh = createMesh(size);
        shader = createShader();
    }

    public void begin (final Matrix4 combined, final float particlesScale) {
        Gdx.gl.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
        Gdx.gl.glEnable(0x8861); //GL11.GL_POINT_SPRITE_OES
        this.particlesScale = particlesScale;
        shader.begin();
        shader.setUniformMatrix("u_projTrans", combined);
        if(this.texture != null){
            this.texture.bind(0);
            shader.setUniformi("u_texture", 0);
        }
    }

    public void end () {
        shader.end();
        Gdx.gl.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
        Gdx.gl20.glDisable(0x8861);
    }

    public void draw (float[] vertices, float radius) {
        final int count = vertices.length / SoftBodyActor.VERTEX_SIZE;
        shader.setUniformf("radius", radius * particlesScale * 2f);
        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_POINTS, 0, count);
    }

    public void dispose () {
        mesh.dispose();
        shader.dispose();
    }

    protected Mesh createMesh(final int size){
        if (size > 32767) throw new IllegalArgumentException("Can't have more than 32767 particles per batch (are you serious?): " + size);

        return new Mesh(Mesh.VertexDataType.VertexArray, false, size , 0,
                        new VertexAttribute(VertexAttributes.Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE));
    }

    protected ShaderProgram createShader () {
        String prefix = "";
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
            prefix +="#version 120\n";
        else
            prefix +="#version 100\n";

        String vertexShader, fragmentShader;

        if(this.texture == null) {
            vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projTrans;\n" //
                    + "uniform float radius;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";
            fragmentShader = "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" //
                    + "const float HALF = 0.5;\n" //
                    + "const float FULL = 1.0;\n" //
                    + "\n" //
                    + "void main()\n"//
                    + "{\n" //
                    + " gl_FragColor = vec4(FULL, FULL, FULL, HALF-length(vec2(gl_PointCoord.x - HALF, gl_PointCoord.y - HALF)));\n" //
                    + "}";
        }
        else{
            vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + ";\n" //
                    + "uniform mat4 u_projTrans;\n" //
                    + "uniform float radius;\n" //
                    + "\n" //
                    + "void main()\n" //
                    + "{\n" //
                    + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                    + "   gl_PointSize = radius;\n" //
                    + "}\n";
            fragmentShader = "#ifdef GL_ES\n" //
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
        }

        final ShaderProgram shader = new ShaderProgram(prefix + vertexShader, prefix + fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
