package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Generic loader of a JSON resource intended to be extended
 *
 * @author Thommil on 4/6/16.
 */
public abstract class JSONLoader {

    protected JsonValue jsonRoot;
    private final JsonReader jsonReader;

    /**
     * Default constructor
     */
    public JSONLoader(){
        this.jsonReader = new JsonReader();
    }

    /**
     * Sets the inner JSON root from a file
     *
     * @param fileHandle The JSON file
     */
    public void parse(final FileHandle fileHandle){
        this.jsonRoot = this.jsonReader.parse(fileHandle);
    }

    /**
     * Sets the inner JSON from a string
     *
     * @param jsonString The JSON string
     */
    public void parse(final String jsonString){
        this.jsonRoot = this.jsonReader.parse(jsonString);
    }

    /**
     * Sets the inner JSON root from an JSONValue
     *
     * @param jsonRoot The JSON root
     */
    public void parse(final JsonValue jsonRoot){
        this.jsonRoot = jsonRoot;
    }

    /**
     * Get JSONRoot for custom values
     *
     * @return The Json root of the loader
     */
    public JsonValue getJsonRoot() {
        return jsonRoot;
    }
}
