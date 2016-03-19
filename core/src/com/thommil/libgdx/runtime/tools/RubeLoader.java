package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Helper class to load Rube files
 *
 * @author Thommil on 19/03/16.
 */
public class RubeLoader {

    private JsonValue rubeScene;
    private final JsonReader jsonReader;

    /**
     * Default constructor
     */
    public RubeLoader(){
        this.jsonReader = new JsonReader();
    }

    /**
     * Sets the inner Rube Scene from a file
     *
     * @param rubeFileHandle The Rube JSON file
     */
    public void parse(final FileHandle rubeFileHandle){
        this.rubeScene = this.jsonReader.parse(rubeFileHandle);
        this.parseScene();
    }

    /**
     * Sets the inner Rube Scene from a string
     *
     * @param rubeString The Rube JSON file
     */
    public void parse(final String rubeString){
        this.rubeScene = this.jsonReader.parse(rubeString);
        this.parseScene();
    }

    /**
     * Sets the inner Rube Scene from an JSONValue
     *
     * @param fileHandle The Rube JSON file
     */
    public void parse(final JsonValue rubeScene){
        this.rubeScene = rubeScene;
        this.parseScene();
    }

    /**
     * Sets the inner Rube Scene from current input
     */
    protected void parseScene(){

    }

}
