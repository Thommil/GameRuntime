package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 * Collection class dedicated to Texture. The goal is too replaced
 * use of direct Texture by TextureSet for multimap purpose.
 *
 * This class is a mixed mixed of decorator/collection with light
 * footprint and as fast as possible access to GPU.
 *
 * This class is compliant with Renderer Batches (not LibGDX one) and
 * can be used also to improve batch by a repartition of regions on set
 * and adding a texture index in a subclass of actors.
 *
 * Created by tomtom on 25/02/16.
 */
public class TextureSet implements Disposable{

    /**
     * TextureSet uses a generic Uniform format for Sample2D
     */
    public static final String UNIFORM_PREFIX = "u_texture";

    public static final String UNIFORM_TEXTURE_0 = UNIFORM_PREFIX + "0";
    public static final String UNIFORM_TEXTURE_1 = UNIFORM_PREFIX + "1";
    public static final String UNIFORM_TEXTURE_2 = UNIFORM_PREFIX + "2";
    public static final String UNIFORM_TEXTURE_3 = UNIFORM_PREFIX + "3";
    public static final String UNIFORM_TEXTURE_4 = UNIFORM_PREFIX + "4";
    public static final String UNIFORM_TEXTURE_5 = UNIFORM_PREFIX + "5";
    public static final String UNIFORM_TEXTURE_6 = UNIFORM_PREFIX + "6";
    public static final String UNIFORM_TEXTURE_7 = UNIFORM_PREFIX + "7";

    private static String[] UNIFORM_VALUES  = {
            UNIFORM_TEXTURE_0,
            UNIFORM_TEXTURE_1,
            UNIFORM_TEXTURE_2,
            UNIFORM_TEXTURE_3,
            UNIFORM_TEXTURE_4,
            UNIFORM_TEXTURE_5,
            UNIFORM_TEXTURE_6,
            UNIFORM_TEXTURE_7
    };

    /**
     * The inner textures
     */
    public final Texture[] textures;

    /**
     * Constructor using a generic size without content
     *
     * @param size The size of the TextureSet
     */
    public TextureSet(final int size){
        this.textures = new Texture[size];
    }

    /**
     * Constructor
     *
     * @param texture
     */
    public TextureSet(final Texture... texture){
        this.textures = texture;
    }

    /**
     * Get the width of the TextureSet (first texture in the set)
     *
     * @return The width ot the TextureSet
     */
    public int getWidth(){
        return this.textures[0].getWidth();
    }

    /**
     * Get the height of the TextureSet (first texture in the set)
     *
     * @return The height ot the TextureSet
     */
    public int getHeight(){
        return this.textures[0].getHeight();
    }

    /**
     * Set Wrapping value for all textures
     */
    public void setWrapAll(final Texture.TextureWrap u, final Texture.TextureWrap v){
        for(final Texture texture : this.textures){
            texture.setWrap(u, v);
        }
    }

    /**
     * Set Texture filtering value for all textures
     */
    public void setFilterAll(final Texture.TextureFilter minFilter, final Texture.TextureFilter magFilter){
        for(final Texture texture : this.textures){
            texture.setFilter(minFilter,magFilter);
        }
    }

    /**
     * Set all textures/samplers in the current instance in the given shader
     *
     * @param shader The shader to use
     */
    public void setUniformAll(final ShaderProgram shader){
        switch(this.textures.length){
            case 1:
                shader.setUniformi(UNIFORM_TEXTURE_0, 0);
                break;
            case 2:
                shader.setUniformi(UNIFORM_TEXTURE_0, 0);
                shader.setUniformi(UNIFORM_TEXTURE_1, 1);
                break;
            case 3:
                shader.setUniformi(UNIFORM_TEXTURE_0, 0);
                shader.setUniformi(UNIFORM_TEXTURE_1, 1);
                shader.setUniformi(UNIFORM_TEXTURE_2, 2);
                break;
            default :
                for(int i=0; i < this.textures.length; i++){
                    shader.setUniformi(UNIFORM_VALUES[i], i);
                }
        }
    }

    /**
     * Bind all the textures
     */
    public void bindAll(){
        switch(this.textures.length){
            case 1:
                this.textures[0].bind(0);
                break;
            case 2:
                this.textures[0].bind(0);
                this.textures[1].bind(1);
                break;
            case 3:
                this.textures[0].bind(0);
                this.textures[1].bind(1);
                this.textures[2].bind(2);
                break;
            default :
                for(int i=0; i < this.textures.length; i++){
                    this.textures[i].bind(i);
                }
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        for(final Texture texture : this.textures){
            texture.dispose();
        }
    }
}
