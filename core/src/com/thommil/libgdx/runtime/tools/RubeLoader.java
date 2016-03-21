package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Helper class to load Rube files.
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
    }

    /**
     * Sets the inner Rube Scene from a string
     *
     * @param rubeString The Rube JSON file
     */
    public void parse(final String rubeString){
        this.rubeScene = this.jsonReader.parse(rubeString);
    }

    /**
     * Sets the inner Rube Scene from an JSONValue
     *
     * @param fileHandle The Rube JSON file
     */
    public void parse(final JsonValue rubeScene){
        this.rubeScene = rubeScene;
    }

    /**
     * Get the gravity
     *
     * @return The gravity in in Vec2
     */
    public Vector2 getGravity(){
        return new Vector2(this.rubeScene.get("gravity").getFloat("x"),this.rubeScene.get("gravity").getFloat("y"));
    }

    /**
     * Get the number of bodies in the scene
     *
     * @return The number of bodis in the scene
     */
    public int getBodyCount(){
        return this.rubeScene.get("body").size;
    }

    /**
     * Gets a Box2D Body Definition from its index in the Scene
     *
     * @param index The body index
     *
     * @return The body definition in a BodyDef
     */
    public BodyDef getBodyDefinition(final int index){
        final BodyDef bodyDef = new BodyDef();
        final JsonValue jsonBody = this.rubeScene.get("body").get(index);
        if(jsonBody.has("angle")) {
            bodyDef.angle = jsonBody.getFloat("angle");
        }
        if(jsonBody.has("angularVelocity")) {
            bodyDef.angularVelocity = jsonBody.getFloat("angularVelocity");
        }
        if(jsonBody.has("awake")) {
            bodyDef.awake = jsonBody.getBoolean("awake");
        }
        if(jsonBody.has("linearVelocity")) {
            if (jsonBody.get("linearVelocity").isObject()) {
                bodyDef.linearVelocity.set(jsonBody.get("linearVelocity").getFloat("x"), jsonBody.get("linearVelocity").getFloat("y"));
            }
        }
        if(jsonBody.has("position")) {
            if (jsonBody.get("position").isObject()) {
                bodyDef.position.set(jsonBody.get("position").getFloat("x"), jsonBody.get("position").getFloat("y"));
            }
        }
        if(jsonBody.has("type")) {
            bodyDef.type = BodyDef.BodyType.values()[jsonBody.getInt("type")];
        }
        return bodyDef;
    }

    /**
     * Gets a Box2D Body mass data from its index in the Scene
     *
     * @param index The body index
     *
     * @return The body mass data in a MassData
     */
    public MassData getBodyMassData(final int index){
        final MassData massData = new MassData();
        final JsonValue jsonBody = this.rubeScene.get("body").get(index);
        if(jsonBody.has("massData-I")) {
            massData.I = jsonBody.getFloat("massData-I");
        }
        if(jsonBody.has("massData-mass")) {
            massData.mass = jsonBody.getFloat("massData-mass");
        }
        if(jsonBody.has("massData-center")) {
            massData.center.set(jsonBody.get("massData-center").getFloat("x"),jsonBody.get("massData-center").getFloat("y"));
        }
        return massData;
    }

    /**
     * Gets the list of fixtures definition for the body at specified index
     *
     * @param bodyIndex The body index
     *
     * @return A list of FixtureDef
     */
    public Array<FixtureDef> getFixturesDefinition(final int bodyIndex){
        final JsonValue jsonBody = this.rubeScene.get("body").get(bodyIndex);
        final Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(true, 16);
        for(final JsonValue jsonFixture : jsonBody.get("fixture")){
            final FixtureDef fixtureDef = new FixtureDef();
            if(jsonFixture.has("density")) {
                fixtureDef.density = jsonFixture.getFloat("density");
            }
            if(jsonFixture.has("friction")) {
                fixtureDef.friction = jsonFixture.getFloat("friction");
            }
            if(jsonFixture.has("restitution")) {
                fixtureDef.restitution = jsonFixture.getFloat("restitution");
            }

            if(jsonFixture.has("polygon")) {
                final PolygonShape polygonShape = new PolygonShape();

                fixtureDef.shape = polygonShape;
            }
            else if(jsonFixture.has("chain")) {
                final ChainShape chainShape = new ChainShape();

                fixtureDef.shape = chainShape;
            }
            else if(jsonFixture.has("circle")) {
                final CircleShape circleShape = new CircleShape();
                circleShape.setPosition(new Vector2(jsonFixture.get("circle").get("center").getFloat("x"),jsonFixture.get("circle").get("center").getFloat("y")));
                circleShape.setRadius(jsonFixture.get("circle").getFloat("radius"));
                fixtureDef.shape = circleShape;
            }
            fixtureDefs.add(fixtureDef);
        }

        return fixtureDefs;
    }

}
