package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.IndexData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexData;
import com.badlogic.gdx.utils.Array;
import com.thommil.libgdx.runtime.GameRuntimeException;

/**
 * Extended Mesh class with support for additonnal VertexData after construction
 *
 * These VertexData are treated separatly in Shaders, this class should be used when
 * it's not possible to inject data directly in main vertices buffer.
 *
 * @author thommil on 03/02/16.
 */
public class ExtendedMesh extends Mesh {

    final Array<VertexData> extendedVertexData = new Array<VertexData>(false, 4);

    public ExtendedMesh(VertexData vertices, IndexData indices, boolean isVertexArray) {
        super(vertices, indices, isVertexArray);
    }

    /**
     * Creates a new Mesh with the given attributes.
     *
     * @param isStatic    whether this mesh is static or not. Allows for internal optimizations.
     * @param maxVertices the maximum number of vertices this mesh can hold
     * @param maxIndices  the maximum number of indices this mesh can hold
     * @param attributes  the {@link VertexAttribute}s. Each vertex attribute defines one property of a vertex such as position,
     */
    public ExtendedMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        super(isStatic, maxVertices, maxIndices, attributes);
    }

    /**
     * Creates a new Mesh with the given attributes.
     *
     * @param isStatic    whether this mesh is static or not. Allows for internal optimizations.
     * @param maxVertices the maximum number of vertices this mesh can hold
     * @param maxIndices  the maximum number of indices this mesh can hold
     * @param attributes  the {@link VertexAttributes}. Each vertex attribute defines one property of a vertex such as position,
     */
    public ExtendedMesh(boolean isStatic, int maxVertices, int maxIndices, VertexAttributes attributes) {
        super(isStatic, maxVertices, maxIndices, attributes);
    }

    /**
     * by jw: Creates a new Mesh with the given attributes. Adds extra optimizations for dynamic (frequently modified) meshes.
     *
     * @param staticVertices whether vertices of this mesh are static or not. Allows for internal optimizations.
     * @param staticIndices  whether indices of this mesh are static or not. Allows for internal optimizations.
     * @param maxVertices    the maximum number of vertices this mesh can hold
     * @param maxIndices     the maximum number of indices this mesh can hold
     * @param attributes     the {@link VertexAttributes}. Each vertex attribute defines one property of a vertex such as position,
     *                       normal or texture coordinate
     **/
    public ExtendedMesh(boolean staticVertices, boolean staticIndices, int maxVertices, int maxIndices, VertexAttributes attributes) {
        super(staticVertices, staticIndices, maxVertices, maxIndices, attributes);
    }

    /**
     * Creates a new Mesh with the given attributes. This is an expert method with no error checking. Use at your own risk.
     *
     * @param type        the {@link VertexDataType} to be used, VBO or VA.
     * @param isStatic    whether this mesh is static or not. Allows for internal optimizations.
     * @param maxVertices the maximum number of vertices this mesh can hold
     * @param maxIndices  the maximum number of indices this mesh can hold
     * @param attributes  the {@link VertexAttribute}s. Each vertex attribute defines one property of a vertex such as position,
     */
    public ExtendedMesh(VertexDataType type, boolean isStatic, int maxVertices, int maxIndices, VertexAttribute... attributes) {
        super(type, isStatic, maxVertices, maxIndices, attributes);
    }

    /**
     * Adds an extended VertexData
     *
     * @param vertexData The extended VertexData
     */
    public void addVertexData(final VertexData vertexData){
        this.extendedVertexData.add(vertexData);
    }

    /**
     * Binds the underlying VertexBufferObject and IndexBufferObject if indices where given. Use this with OpenGL
     * ES 2.0 and when auto-bind is disabled.
     *
     * @param shader    the shader (does not bind the shader)
     * @param locations array containing the attribute locations.
     */
    @Override
    public void bind(ShaderProgram shader, int[] locations) {
        super.bind(shader, locations);
        for(final VertexData vertexData : this.extendedVertexData){
            if(vertexData.getNumVertices() < this.getNumVertices()){
                throw new GameRuntimeException("Extended VertexData must contains as many vertices ("+vertexData.getNumVertices()+" found) as its Mesh container ("+this.getNumVertices()+" found)");
            }
            vertexData.bind(shader, locations);
        }
    }

    /**
     * Unbinds the underlying VertexBufferObject and IndexBufferObject is indices were given. Use this with OpenGL
     * ES 1.x and when auto-bind is disabled.
     *
     * @param shader    the shader (does not unbind the shader)
     * @param locations array containing the attribute locations.
     */
    @Override
    public void unbind(ShaderProgram shader, int[] locations) {
        super.unbind(shader, locations);
        for(final VertexData vertexData : this.extendedVertexData){
            vertexData.unbind(shader, locations);
        }
    }

    /**
     * <p>
     * Renders the mesh using the given primitive type. offset specifies the offset into either the vertex buffer or the index
     * buffer depending on whether indices are defined. count specifies the number of vertices or indices to use thus count /
     * #vertices per primitive primitives are rendered.
     * </p>
     * <p/>
     * <p>
     * This method will automatically bind each vertex attribute as specified at construction time via {@link VertexAttributes} to
     * the respective shader attributes. The binding is based on the alias defined for each VertexAttribute.
     * </p>
     * <p/>
     * <p>
     * This method must only be called after the {@link ShaderProgram#begin()} method has been called!
     * </p>
     * <p/>
     * <p>
     * This method is intended for use with OpenGL ES 2.0 and will throw an IllegalStateException when OpenGL ES 1.x is used.
     * </p>
     *
     * @param shader        the shader to be used
     * @param primitiveType the primitive type
     * @param offset        the offset into the vertex or index buffer
     * @param count         number of vertices or indices to use
     * @param autoBind      overrides the autoBind member of this Mesh
     */
    @Override
    public void render(ShaderProgram shader, int primitiveType, int offset, int count, boolean autoBind) {
        if (count == 0) return;

        if (autoBind){
            this.bind(shader);
        }

        super.render(shader, primitiveType, offset, count, false);

        if (autoBind){
            this.unbind(shader);
        }
    }

    /**
     * Returns the first {@link VertexAttribute} having the given Usage.
     *
     * @param usage the Usage.
     * @return the VertexAttribute or null if no attribute with that usage was found.
     */
    @Override
    public VertexAttribute getVertexAttribute(int usage) {
        final VertexAttribute vertexAttribute = super.getVertexAttribute(usage);
        if(vertexAttribute == null){
            for(final VertexData vertexData : this.extendedVertexData){
                for(final VertexAttribute vertexDatavertexAttribute : vertexData.getAttributes()){
                    if(vertexDatavertexAttribute.usage == usage) return vertexDatavertexAttribute;
                }
            }
        }
        return null;
    }

    /**
     * Returns the first {@link VertexData} having the given Usage.
     *
     * @param usage the Usage.
     * @return the VertexData or null if no attribute with that usage was found.
     */
    public VertexData getVertexData(int usage){
        for(final VertexData vertexData : this.extendedVertexData){
            for(final VertexAttribute vertexDatavertexAttribute : vertexData.getAttributes()){
                if(vertexDatavertexAttribute.usage == usage){
                    return vertexData;
                }
            }
        }
        return null;
    }

    /**
     * Frees all resources associated with this Mesh
     */
    @Override
    public void dispose() {
        super.dispose();
        for(final VertexData vertexData : this.extendedVertexData){
            vertexData.dispose();
        }
    }
}
