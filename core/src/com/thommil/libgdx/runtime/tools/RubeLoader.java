package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Helper class to load Rube files.
 * @author Thommil on 19/03/16.
 */
public class RubeLoader extends JSONLoader{

    /**
     * Get the gravity
     *
     * @return The gravity in in Vec2
     */
    public Vector2 getGravity(){
        return new Vector2(this.jsonRoot.get("gravity").getFloat("x"),this.jsonRoot.get("gravity").getFloat("y"));
    }

    /**
     * Get the number of bodies in the scene
     *
     * @return The number of bodis in the scene
     */
    public int getBodyCount(){
        return this.jsonRoot.get("body").size;
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
        final JsonValue jsonBody = this.jsonRoot.get("body").get(index);
        if(jsonBody.has("name")) {
            bodyDef.name = jsonBody.getString("name");
        }
        if(jsonBody.has("type")) {
            bodyDef.type = BodyDef.BodyType.values()[jsonBody.getInt("type")];
        }
        if(jsonBody.has("angle")) {
            bodyDef.angle = jsonBody.getFloat("angle");
        }
        if(jsonBody.has("angularDamping")) {
            bodyDef.angularDamping = jsonBody.getFloat("angularDamping");
        }
        if(jsonBody.has("angularVelocity")) {
            bodyDef.angularVelocity = jsonBody.getFloat("angularVelocity");
        }
        if(jsonBody.has("awake")) {
            bodyDef.awake = jsonBody.getBoolean("awake");
        }
        if(jsonBody.has("bullet")) {
            bodyDef.bullet = jsonBody.getBoolean("bullet");
        }
        if(jsonBody.has("fixedRotation")) {
            bodyDef.fixedRotation = jsonBody.getBoolean("fixedRotation");
        }
        if(jsonBody.has("linearDamping")) {
            bodyDef.linearDamping = jsonBody.getFloat("linearDamping");
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
        if(jsonBody.has("active")) {
            bodyDef.active = jsonBody.getBoolean("active");
        }
        if(jsonBody.has("allowSleep")) {
            bodyDef.allowSleep = jsonBody.getBoolean("allowSleep");
        }
        if(jsonBody.has("gravityScale")) {
            bodyDef.gravityScale = jsonBody.getFloat("gravityScale");
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
        final JsonValue jsonBody = this.jsonRoot.get("body").get(index);
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
     * Gets a Box2D Body image from body index in the Scene
     *
     * @param index The body index
     *
     * @return The body image in a BodyImage
     */
    public ImageDef getImageDefinition(final int bodyIndex){
        final JsonValue jsonImages = this.jsonRoot.get("image");
        for(final JsonValue jsonImage : jsonImages){
            if(jsonImage.has("body") && jsonImage.getInt("body") == bodyIndex){
                final ImageDef bodyImage = new ImageDef();
                bodyImage.body = bodyIndex;
                if(jsonImage.has("name")) {
                    bodyImage.name = jsonImage.getString("name");
                }
                if(jsonImage.has("file")) {
                    bodyImage.path = jsonImage.getString("file");
                }
                if(jsonImage.has("customProperties")) {
                    for(final JsonValue jsonCustomProperty : jsonImage.get("customProperties")){
                        if(jsonCustomProperty.getString("name").equals("width")){
                            bodyImage.width = jsonCustomProperty.getFloat("float");
                        }
                        else if(jsonCustomProperty.getString("name").equals("height")){
                            bodyImage.height = jsonCustomProperty.getFloat("float");
                        }
                        else if(jsonCustomProperty.getString("name").equals("regionX")){
                            bodyImage.regionX = jsonCustomProperty.getInt("int");
                        }
                        else if(jsonCustomProperty.getString("name").equals("regionY")){
                            bodyImage.regionY = jsonCustomProperty.getInt("int");
                        }
                        else if(jsonCustomProperty.getString("name").equals("regionWidth")){
                            bodyImage.regionWidth = jsonCustomProperty.getInt("int");
                        }
                        else if(jsonCustomProperty.getString("name").equals("regionHeight")){
                            bodyImage.regionHeight = jsonCustomProperty.getInt("int");
                        }
                        else if(jsonCustomProperty.getString("name").equals("normalOffset")){
                            bodyImage.normalOffset = new float[2];
                            bodyImage.normalOffset[0] = jsonCustomProperty.get("vec2").getFloat("x");
                            bodyImage.normalOffset[1] = jsonCustomProperty.get("vec2").getFloat("y");
                        }
                    }
                }
                return bodyImage;
            }
        }
        return null;
    }

    /**
     * Gets the list of fixtures definition for the body at specified index
     *
     * @param bodyIndex The body index
     *
     * @return A list of FixtureDef
     */
    public Array<FixtureDef> getFixturesDefinition(final int bodyIndex){
        final JsonValue jsonBody = this.jsonRoot.get("body").get(bodyIndex);
        final Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(true, 16);
        for(final JsonValue jsonFixture : jsonBody.get("fixture")){
            final FixtureDef fixtureDef = new FixtureDef();
            if(jsonFixture.has("density")) {
                fixtureDef.density = jsonFixture.getFloat("density");
            }
            if(jsonFixture.has("filter-categoryBits")) {
                fixtureDef.filter.categoryBits = jsonFixture.getShort("filter-categoryBits");
            }
            if(jsonFixture.has("filter-groupIndex")) {
                fixtureDef.filter.categoryBits = jsonFixture.getShort("filter-groupIndex");
            }
            if(jsonFixture.has("filter-maskBits")) {
                fixtureDef.filter.maskBits = jsonFixture.getShort("filter-maskBits");
            }
            if(jsonFixture.has("friction")) {
                fixtureDef.friction = jsonFixture.getFloat("friction");
            }
            if(jsonFixture.has("restitution")) {
                fixtureDef.restitution = jsonFixture.getFloat("restitution");
            }

            if(jsonFixture.has("sensor")) {
                fixtureDef.isSensor = jsonFixture.getBoolean("sensor");
            }
            if(jsonFixture.has("polygon")) {
                final PolygonShape polygonShape = new PolygonShape();
                final float[] xVertices = jsonFixture.get("polygon").get("vertices").get("x").asFloatArray();
                final float[] yVertices = jsonFixture.get("polygon").get("vertices").get("y").asFloatArray();
                final float[] vertices = new float[xVertices.length + yVertices.length];
                for(int inIndex=0, outIndex=0; inIndex < xVertices.length; inIndex++, outIndex+=2){
                    vertices[outIndex] = xVertices[inIndex];
                    vertices[outIndex+1] = yVertices[inIndex];
                }
                polygonShape.set(vertices);
                fixtureDef.shape = polygonShape;
            }
            else if(jsonFixture.has("chain")) {
                final ChainShape chainShape = new ChainShape();
                final float[] xVertices = jsonFixture.get("chain").get("vertices").get("x").asFloatArray();
                final float[] yVertices = jsonFixture.get("chain").get("vertices").get("y").asFloatArray();
                final float[] vertices = new float[xVertices.length + yVertices.length];
                for(int inIndex=0, outIndex=0; inIndex < xVertices.length; inIndex++, outIndex+=2){
                    vertices[outIndex] = xVertices[inIndex];
                    vertices[outIndex+1] = yVertices[inIndex];
                }
                if(jsonFixture.get("chain").has("hasPrevVertex") && jsonFixture.get("chain").getBoolean("hasPrevVertex")){
                    chainShape.setPrevVertex(jsonFixture.get("chain").get("prevVertex").getFloat("x"),jsonFixture.get("chain").get("prevVertex").getFloat("y"));
                }
                if(jsonFixture.get("chain").has("hasNextVertex") && jsonFixture.get("chain").getBoolean("hasNextVertex")){
                    chainShape.setPrevVertex(jsonFixture.get("chain").get("nextVertex").getFloat("x"),jsonFixture.get("chain").get("nextVertex").getFloat("y"));
                }
                chainShape.createChain(vertices);
                fixtureDef.shape = chainShape;
            }
            else if(jsonFixture.has("circle")) {
                final CircleShape circleShape = new CircleShape();
                if(jsonFixture.get("circle").get("center").has("x")) {
                    circleShape.setPosition(new Vector2(jsonFixture.get("circle").get("center").getFloat("x"), jsonFixture.get("circle").get("center").getFloat("y")));
                }
                circleShape.setRadius(jsonFixture.get("circle").getFloat("radius"));
                fixtureDef.shape = circleShape;
            }
            fixtureDefs.add(fixtureDef);
        }
        return fixtureDefs;
    }

    /**
     * Gets the list of joints definition
     *
     * @return A list of JointDef
     */
    public Array<JointDef> getJointsDefinition(){
        final Array<JointDef> jointDefs = new Array<JointDef>(true,16);
        if(this.jsonRoot.has("joint")) {
            for(final JsonValue jsonJoint : this.jsonRoot.get("joint")){
                final String jointType = jsonJoint.getString("type");
                if(jointType.equals("revolute")){
                    final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                    if(jsonJoint.has("name")){
                        revoluteJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        revoluteJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        revoluteJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    revoluteJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    revoluteJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    revoluteJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    revoluteJointDef.enableMotor = jsonJoint.has("enableMotor") && jsonJoint.getBoolean("enableMotor");
                    revoluteJointDef.enableLimit = jsonJoint.has("enableLimit") && jsonJoint.getBoolean("enableLimit");
                    if(jsonJoint.has("lowerLimit")){
                        revoluteJointDef.lowerAngle = jsonJoint.getFloat("lowerLimit");
                    }
                    if(jsonJoint.has("motorSpeed")){
                        revoluteJointDef.motorSpeed = jsonJoint.getFloat("motorSpeed");
                    }
                    if(jsonJoint.has("upperLimit")){
                        revoluteJointDef.upperAngle = jsonJoint.getFloat("upperLimit");
                    }
                    if(jsonJoint.has("maxMotorTorque")){
                        revoluteJointDef.maxMotorTorque = jsonJoint.getFloat("maxMotorTorque");
                    }
                    if(jsonJoint.has("refAngle")){
                        revoluteJointDef.referenceAngle = jsonJoint.getFloat("refAngle");
                    }
                    jointDefs.add(revoluteJointDef);
                }
                else  if(jointType.equals("distance")){
                    final DistanceJointDef distanceJointDef = new DistanceJointDef();
                    if(jsonJoint.has("name")){
                        distanceJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        distanceJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        distanceJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    distanceJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    distanceJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    distanceJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    if(jsonJoint.has("dampingRatio")){
                        distanceJointDef.dampingRatio = jsonJoint.getFloat("dampingRatio");
                    }
                    if(jsonJoint.has("frequency")){
                        distanceJointDef.frequencyHz = jsonJoint.getFloat("frequency");
                    }
                    if(jsonJoint.has("length")){
                        distanceJointDef.length= jsonJoint.getFloat("length");
                    }
                    jointDefs.add(distanceJointDef);
                }
                else  if(jointType.equals("prismatic")){
                    final PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
                    if(jsonJoint.has("name")){
                        prismaticJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        prismaticJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        prismaticJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    prismaticJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    prismaticJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    prismaticJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    prismaticJointDef.enableMotor = jsonJoint.has("enableMotor") && jsonJoint.getBoolean("enableMotor");
                    prismaticJointDef.enableLimit = jsonJoint.has("enableLimit") && jsonJoint.getBoolean("enableLimit");
                    if(jsonJoint.has("localAxisA") && jsonJoint.get("localAxisA").isObject()){
                        prismaticJointDef.localAxisA.set(jsonJoint.get("localAxisA").getFloat("x"),jsonJoint.get("localAxisA").getFloat("y"));
                    }
                    if(jsonJoint.has("lowerLimit")){
                        prismaticJointDef.lowerTranslation = jsonJoint.getFloat("lowerLimit");
                    }
                    if(jsonJoint.has("maxMotorForce")){
                        prismaticJointDef.maxMotorForce= jsonJoint.getFloat("maxMotorForce");
                    }
                    if(jsonJoint.has("motorSpeed")){
                        prismaticJointDef.motorSpeed= jsonJoint.getFloat("motorSpeed");
                    }
                    if(jsonJoint.has("refAngle")){
                        prismaticJointDef.referenceAngle= jsonJoint.getFloat("refAngle");
                    }
                    if(jsonJoint.has("upperLimit")){
                        prismaticJointDef.upperTranslation= jsonJoint.getFloat("upperLimit");
                    }
                    jointDefs.add(prismaticJointDef);
                }
                else  if(jointType.equals("wheel")){
                    final WheelJointDef wheelJointDef = new WheelJointDef();
                    if(jsonJoint.has("name")){
                        wheelJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        wheelJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        wheelJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    wheelJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    wheelJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    wheelJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    wheelJointDef.enableMotor = jsonJoint.has("enableMotor") && jsonJoint.getBoolean("enableMotor");
                    if(jsonJoint.has("localAxisA") && jsonJoint.get("localAxisA").isObject()){
                        wheelJointDef.localAxisA.set(jsonJoint.get("localAxisA").getFloat("x"),jsonJoint.get("localAxisA").getFloat("y"));
                    }
                    if(jsonJoint.has("maxMotorTorque")){
                        wheelJointDef.maxMotorTorque= jsonJoint.getFloat("maxMotorTorque");
                    }
                    if(jsonJoint.has("motorSpeed")){
                        wheelJointDef.motorSpeed= jsonJoint.getFloat("motorSpeed");
                    }
                    if(jsonJoint.has("springDampingRatio")){
                        wheelJointDef.dampingRatio = jsonJoint.getFloat("springDampingRatio");
                    }
                    if(jsonJoint.has("springFrequency")){
                        wheelJointDef.frequencyHz= jsonJoint.getFloat("springFrequency");
                    }
                    jointDefs.add(wheelJointDef);
                }
                else  if(jointType.equals("rope")){
                    final RopeJointDef ropeJointDef = new RopeJointDef();
                    if(jsonJoint.has("name")){
                        ropeJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        ropeJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        ropeJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    ropeJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    ropeJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    ropeJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    if(jsonJoint.has("maxLength")){
                        ropeJointDef.maxLength= jsonJoint.getFloat("maxLength");
                    }
                    jointDefs.add(ropeJointDef);
                }
                else  if(jointType.equals("motor")){
                    final MotorJointDef motorJointDef = new MotorJointDef();
                    if(jsonJoint.has("name")){
                        motorJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        motorJointDef.linearOffset.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    motorJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    motorJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    motorJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    if(jsonJoint.has("maxForce")){
                        motorJointDef.maxForce= jsonJoint.getFloat("maxForce");
                    }
                    if(jsonJoint.has("maxTorque")){
                        motorJointDef.maxTorque= jsonJoint.getFloat("maxTorque");
                    }
                    if(jsonJoint.has("correctionFactor")){
                        motorJointDef.correctionFactor = jsonJoint.getFloat("correctionFactor");
                    }
                    jointDefs.add(motorJointDef);
                }
                else  if(jointType.equals("weld")){
                    final WeldJointDef weldJointDef = new WeldJointDef();
                    if(jsonJoint.has("name")){
                        weldJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        weldJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        weldJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    weldJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    weldJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    weldJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    if(jsonJoint.has("refAngle")){
                        weldJointDef.referenceAngle= jsonJoint.getFloat("refAngle");
                    }
                    if(jsonJoint.has("dampingRatio")){
                        weldJointDef.dampingRatio= jsonJoint.getFloat("dampingRatio");
                    }
                    if(jsonJoint.has("frequency")){
                        weldJointDef.frequencyHz = jsonJoint.getFloat("frequency");
                    }
                    jointDefs.add(weldJointDef);
                }
                else  if(jointType.equals("friction")){
                    final FrictionJointDef frictionJointDef = new FrictionJointDef();
                    if(jsonJoint.has("name")){
                        frictionJointDef.name = jsonJoint.getString("name");
                    }
                    if(jsonJoint.get("anchorA").isObject()){
                        frictionJointDef.localAnchorA.set(jsonJoint.get("anchorA").getFloat("x"),jsonJoint.get("anchorA").getFloat("y"));
                    }
                    if(jsonJoint.get("anchorB").isObject()){
                        frictionJointDef.localAnchorB.set(jsonJoint.get("anchorB").getFloat("x"),jsonJoint.get("anchorB").getFloat("y"));
                    }
                    frictionJointDef.bodyIdA = jsonJoint.getInt("bodyA");
                    frictionJointDef.bodyIdB = jsonJoint.getInt("bodyB");
                    frictionJointDef.collideConnected = jsonJoint.has("collideConnected") && jsonJoint.getBoolean("collideConnected");
                    if(jsonJoint.has("maxForce")){
                        frictionJointDef.maxForce= jsonJoint.getFloat("maxForce");
                    }
                    if(jsonJoint.has("maxTorque")){
                        frictionJointDef.maxTorque= jsonJoint.getFloat("maxTorque");
                    }
                    jointDefs.add(frictionJointDef);
                }
            }
        }
        return jointDefs;
    }

    /**
     * BodyDef extension to support name
     */
    public static class BodyDef extends com.badlogic.gdx.physics.box2d.BodyDef{
        public String name;
    }

    /**
     * RevoluteJointDef extension to support body ids
     */
    public static class RevoluteJointDef extends com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * DistanceJointDef extension to support body ids
     */
    public static class DistanceJointDef extends com.badlogic.gdx.physics.box2d.joints.DistanceJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * PrismaticJointDef extension to support body ids
     */
    public static class PrismaticJointDef extends com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * WheelJointDef extension to support body ids
     */
    public static class WheelJointDef extends com.badlogic.gdx.physics.box2d.joints.WheelJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * RopeJointDef extension to support body ids
     */
    public static class RopeJointDef extends com.badlogic.gdx.physics.box2d.joints.RopeJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * MotorJointDef extension to support body ids
     */
    public static class MotorJointDef extends com.badlogic.gdx.physics.box2d.joints.MotorJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * WeldJointDef extension to support body ids
     */
    public static class WeldJointDef extends com.badlogic.gdx.physics.box2d.joints.WeldJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * FrictionJointDef extension to support body ids
     */
    public static class FrictionJointDef extends com.badlogic.gdx.physics.box2d.joints.FrictionJointDef{
        public String name;
        public int bodyIdA;
        public int bodyIdB;
    }

    /**
     * Image for a Rube Body (custom properties):
     * {
     *  "name" : name,
     *  "file" : "path/to/image",
     *  "body" : bodyId,
     *  "customProperties" : [
     *      {
     *          "name": "width",
     *          "float": width
     *      },
     *      {
     *          "name": "height",
     *          "float": height
     *      },
     *      {
     *          "name": "regionX",
     *          "int": regionX
     *      },
     *      {
     *          "name": "regionY",
     *          "int": regionY
     *      },
     *      {
     *          "name": "regionWidth",
     *          "int": regionWidth
     *      },
     *      {
     *          "name": "regionHeight",
     *          "int": regionHeight
     *      },
     *      {
     *          "name" : "normalOffset",
     *          "vec2" :
     *          {
     *              "x" : 512.0,
     *              "y" : 0
     *          }
     *      }
     *   ]
     * }
     */
    public static class ImageDef {
        public String name;
        public String path;
        public int body;
        public float width;
        public float height;
        public int regionX;
        public int regionY;
        public int regionWidth;
        public int regionHeight;
        public float[] normalOffset;
    }

}
