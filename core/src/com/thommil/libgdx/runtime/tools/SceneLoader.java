package com.thommil.libgdx.runtime.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.graphics.animation.*;

/**
 * Helper class to load extended Rube files.
 *
 * @author Thommil on 19/03/16.
 */
public class SceneLoader extends JSONLoader{

    /**
     * Inner TextureAtlas representation of images
     */
    protected TextureAtlas textureAtlas;

    /**
     * Get the gravity
     *
     * @return The gravity in in Vec2
     */
    public Vector2 getGravity(){
        if(this.jsonRoot.has("gravity")) {
            return new Vector2(this.jsonRoot.get("gravity").getFloat("x"), this.jsonRoot.get("gravity").getFloat("y"));
        }
        return null;
    }

    /**
     * Gets this list of Box2D Body Definition in the Scene
     *
     * @return The list of bodies definition
     */
    public Array<BodyDef> getBodiesDefintion(){
        if(this.jsonRoot.has("body")) {
            final int bodyCount = this.jsonRoot.get("body").size;
            final Array<BodyDef> bodyDefs = new Array<BodyDef>(true, bodyCount);
            for (int index = 0; index < bodyCount; index++) {
                bodyDefs.add(this.getBodyDefinition(index));
            }
            return bodyDefs;
        }
        return new Array<BodyDef>(true,0);
    }

    /**
     * Gets a Box2D Body Definition from its name in the Scene
     *
     * @param name The body name
     *
     * @return The body definition in a BodyDef
     */
    public BodyDef getBodyDefintion(final String name){
        if(this.jsonRoot.has("body")) {
            int index = 0;
            for (final JsonValue jsonBody : this.jsonRoot.get("body")) {
                if (jsonBody.has("name") && jsonBody.getString("name").equals(name)) {
                    final BodyDef bodyDef = this.getBodyDefintion(jsonBody);
                    bodyDef.index = index;
                    return bodyDef;
                }
                index++;
            }
        }
        return null;
    }

    /**
     * Gets a Box2D Body Definition from its index in the Scene
     *
     * @param index The body index
     *
     * @return The body definition in a BodyDef
     */
    public BodyDef getBodyDefinition(final int index){
        if(this.jsonRoot.has("body")) {
            final BodyDef bodyDef = this.getBodyDefintion(this.jsonRoot.get("body").get(index));
            if (bodyDef != null) {
                bodyDef.index = index;
            }
            return bodyDef;
        }
        return null;
    }

    private BodyDef getBodyDefintion(final JsonValue jsonBody){
        final BodyDef bodyDef = new BodyDef();

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
        if(this.jsonRoot.has("body")) {
            final MassData massData = new MassData();
            final JsonValue jsonBody = this.jsonRoot.get("body").get(index);
            if (jsonBody.has("massData-I")) {
                massData.I = jsonBody.getFloat("massData-I");
            }
            if (jsonBody.has("massData-mass")) {
                massData.mass = jsonBody.getFloat("massData-mass");
            }
            if (jsonBody.has("massData-center")) {
                massData.center.set(jsonBody.get("massData-center").getFloat("x"), jsonBody.get("massData-center").getFloat("y"));
            }
            return massData;
        }
        return null;
    }

    /**
     * Gets the inner images in a TextureAtlas
     *
     * @param assetManager The asset manager used to load the Textures (not directly the TextureAtlas)
     *
     * @return A TextureAtlas containing the inner images
     */
    public TextureAtlas getTextureAtlas(final AssetManager assetManager){
        if(this.textureAtlas == null){
            this.textureAtlas = new TextureAtlas();
            for(final ImageDef imageDef : this.getImagesDefinition()){
                this.textureAtlas.addRegion(imageDef.name, assetManager.get(imageDef.path, Texture.class), imageDef.regionX, imageDef.regionY, imageDef.regionWidth, imageDef.regionHeight);
            }
        }
        return this.textureAtlas;
    }

    /**
     * Gets the list of images in the Scene
     *
     * @return The lis of images
     */
    public Array<ImageDef> getImagesDefinition(){
        if(this.jsonRoot.has("image")) {
            final Array<ImageDef> imageDefs = new Array<ImageDef>(true, this.jsonRoot.get("image").size);
            for (final JsonValue jsonImage : this.jsonRoot.get("image")) {
                imageDefs.add(this.getImageDefintion(jsonImage));
            }
            return imageDefs;
        }
        return new Array<ImageDef>(true,0);
    }

    /**
     * Gets an image its name in the Scene
     *
     * @param name The image name
     *
     * @return The image definition
     */
    public ImageDef getImageDefinition(final String name){
        if(this.jsonRoot.has("image")) {
            final JsonValue jsonImages = this.jsonRoot.get("image");
            for (final JsonValue jsonImage : jsonImages) {
                if (jsonImage.has("name") && jsonImage.getString("name").equals(name)) {
                    return this.getImageDefintion(jsonImage);
                }
            }
        }
        return null;
    }

    /**
     * Gets an image from body index in the Scene
     *
     * @param index The body index
     *
     * @return The image definition
     */
    public ImageDef getImageDefinition(final int bodyIndex){
        if(this.jsonRoot.has("image")) {
            for (final JsonValue jsonImage : this.jsonRoot.get("image")) {
                if (jsonImage.has("body") && jsonImage.getInt("body") == bodyIndex) {
                    return this.getImageDefintion(jsonImage);
                }
            }
        }
        return null;
    }

    private ImageDef getImageDefintion(final JsonValue jsonImage){
        final ImageDef imageDef = new ImageDef();
        if(jsonImage.has("body")) {
            imageDef.body = jsonImage.getInt("body");
        }
        if(jsonImage.has("name")) {
            imageDef.name = jsonImage.getString("name");
        }
        if(jsonImage.has("file")) {
            imageDef.path = jsonImage.getString("file");
        }
        if(jsonImage.has("x")) {
            imageDef.x = jsonImage.getFloat("x");
        }
        if(jsonImage.has("y")) {
            imageDef.y = jsonImage.getFloat("y");
        }
        if(jsonImage.has("width")) {
            imageDef.width = jsonImage.getFloat("width");
        }
        if(jsonImage.has("height")) {
            imageDef.height = jsonImage.getFloat("height");
        }
        if(jsonImage.has("regionX")) {
            imageDef.regionX = jsonImage.getInt("regionX");
        }
        if(jsonImage.has("regionY")) {
            imageDef.regionY = jsonImage.getInt("regionY");
        }
        if(jsonImage.has("regionWidth")) {
            imageDef.regionWidth = jsonImage.getInt("regionWidth");
        }
        if(jsonImage.has("regionHeight")) {
            imageDef.regionHeight = jsonImage.getInt("regionHeight");
        }
        if(jsonImage.has("normalOffset")) {
            imageDef.normalOffset.set(jsonImage.get("normalOffset").getFloat("x"), jsonImage.get("normalOffset").getFloat("y"));
        }
        return imageDef;
    }

    /**
     * Helper method to build an animation from its name and the AssetManager containing the resources
     *
     * @param name The name of the animation
     * @param assetManager The asset manager containing the animation resources
     * @return A new created animation
     */
    public Animation getAnimation(final String name, final AssetManager assetManager){
        final AnimationDef animationDef = this.getAnimationDefinition(name);
        if(animationDef instanceof ImageAnimationDef){
            final Array<TextureRegion> textureRegions = new Array<TextureRegion>(true, animationDef.keyFrames.length);
            for(final ImageAnimationDef.KeyFrame keyFrame : ((ImageAnimationDef)animationDef).keyFrames){
                textureRegions.add(new TextureRegion(assetManager.get(((ImageAnimationDef)animationDef).path, Texture.class), keyFrame.regionX, keyFrame.regionY, keyFrame.regionWidth, keyFrame.regionHeight));
            }
            return new ImageAnimation(animationDef.frameDuration, animationDef.playMode, animationDef.interpolator.toInterpolation(), (TextureRegion[]) textureRegions.toArray(TextureRegion.class));
        }
        else if(animationDef instanceof TranslateAnimationDef){
            final Array<TranslateAnimation.KeyFrame> keyFrames = new Array<TranslateAnimation.KeyFrame>(true, animationDef.keyFrames.length);
            for(final TranslateAnimationDef.KeyFrame keyFrame : ((TranslateAnimationDef)animationDef).keyFrames){
                keyFrames.add(new TranslateAnimation.KeyFrame(keyFrame.xOffset, keyFrame.yOffset, keyFrame.interpolator.toInterpolation()));
            }
            return new TranslateAnimation(animationDef.frameDuration, animationDef.playMode, animationDef.interpolator.toInterpolation(), (TranslateAnimation.KeyFrame[]) keyFrames.toArray(TranslateAnimation.KeyFrame.class));
        }
        else if(animationDef instanceof RotateAnimationDef){
            final Array<RotateAnimation.KeyFrame> keyFrames = new Array<RotateAnimation.KeyFrame>(true, animationDef.keyFrames.length);
            for(final RotateAnimationDef.KeyFrame keyFrame : ((RotateAnimationDef)animationDef).keyFrames){
                keyFrames.add(new RotateAnimation.KeyFrame(keyFrame.angle, keyFrame.interpolator.toInterpolation()));
            }
            return new RotateAnimation(animationDef.frameDuration, animationDef.playMode, animationDef.interpolator.toInterpolation(), (RotateAnimation.KeyFrame[]) keyFrames.toArray(RotateAnimation.KeyFrame.class));
        }
        else if(animationDef instanceof ScaleAnimationDef){
            final Array<ScaleAnimation.KeyFrame> keyFrames = new Array<ScaleAnimation.KeyFrame>(true, animationDef.keyFrames.length);
            for(final ScaleAnimationDef.KeyFrame keyFrame : ((ScaleAnimationDef)animationDef).keyFrames){
                keyFrames.add(new ScaleAnimation.KeyFrame(keyFrame.xOffset, keyFrame.yOffset, keyFrame.interpolator.toInterpolation()));
            }
            return new ScaleAnimation(animationDef.frameDuration, animationDef.playMode, animationDef.interpolator.toInterpolation(), (ScaleAnimation.KeyFrame[]) keyFrames.toArray(ScaleAnimation.KeyFrame.class));
        }
        else if(animationDef instanceof ColorAnimationDef){
            final Array<ColorAnimation.KeyFrame> keyFrames = new Array<ColorAnimation.KeyFrame>(true, animationDef.keyFrames.length);
            for(final ColorAnimationDef.KeyFrame keyFrame : ((ColorAnimationDef)animationDef).keyFrames){
                keyFrames.add(new ColorAnimation.KeyFrame(keyFrame.color, keyFrame.interpolator.toInterpolation()));
            }
            return new ColorAnimation(animationDef.frameDuration, animationDef.playMode, animationDef.interpolator.toInterpolation(), (ColorAnimation.KeyFrame[]) keyFrames.toArray(ColorAnimation.KeyFrame.class));
        }
        return null;
    }

    /**
     * Gets the list of animations in the Scene
     *
     * @return The lis of animations
     */
    public Array<AnimationDef> getAnimationsDefinition(){
        if(this.jsonRoot.has("animation")) {
            final Array<AnimationDef> animationDefs = new Array<AnimationDef>(true, this.jsonRoot.get("animation").size);
            for (final JsonValue jsonAnimation : this.jsonRoot.get("animation")) {
                animationDefs.add(this.getAnimationDefinition(jsonAnimation));
            }
            return animationDefs;
        }
        return new Array<AnimationDef>(true,0);
    }

    /**
     * Gets an animation from its name in the Scene
     *
     * @param name The animation name
     *
     * @return The animation definition
     */
    public AnimationDef getAnimationDefinition(final String name){
        if(this.jsonRoot.has("animation")) {
            for (final JsonValue jsonAnimation : this.jsonRoot.get("animation")) {
                if (jsonAnimation.has("name") && jsonAnimation.getString("name").equals(name)) {
                    return this.getAnimationDefinition(jsonAnimation);
                }
            }
        }
        return null;
    }

    private AnimationDef getAnimationDefinition(final JsonValue jsonAnimation){
        AnimationDef animationDef = null;
        if(jsonAnimation.has("type")) {
            final AnimationDef.Type type = AnimationDef.Type.valueOf(jsonAnimation.getString("type"));
            switch(type){
                case IMAGE: animationDef = new ImageAnimationDef(); break;
                case TRANSLATE : animationDef = new TranslateAnimationDef(); break;
                case ROTATE : animationDef = new RotateAnimationDef(); break;
                case SCALE : animationDef = new ScaleAnimationDef(); break;
                case COLOR : animationDef = new ColorAnimationDef(); break;
            }
        }
        else throw new GameRuntimeException("Missing type in animation definition : " + jsonAnimation.getString("name"));

        if(jsonAnimation.has("name")) {
            animationDef.name = jsonAnimation.getString("name");
        }
        if(jsonAnimation.has("playMode")) {
            animationDef.playMode = Animation.PlayMode.valueOf(jsonAnimation.getString("playMode"));
        }
        if(jsonAnimation.has("frameDuration")) {
            animationDef.frameDuration = jsonAnimation.getFloat("frameDuration");
        }
        if(jsonAnimation.has("interpolator")) {
            animationDef.interpolator = AnimationDef.Interpolator.valueOf(jsonAnimation.getString("interpolator"));
        }
        if(animationDef instanceof ImageAnimationDef){
            final ImageAnimationDef imageAnimationDef = ((ImageAnimationDef)animationDef);
            if(jsonAnimation.has("file")) {
                imageAnimationDef.path = jsonAnimation.getString("file");
            }
            if(jsonAnimation.has("keyFrames")) {
                animationDef.keyFrames = new ImageAnimationDef.KeyFrame[jsonAnimation.get("keyFrames").size];
                for(int index=0; index < animationDef.keyFrames.length; index++){
                    final JsonValue jsonKeyFrame = jsonAnimation.get("keyFrames").get(index);
                    imageAnimationDef.keyFrames[index] = new ImageAnimationDef.KeyFrame();
                    imageAnimationDef.keyFrames[index].regionX = jsonKeyFrame.getInt("regionX");
                    imageAnimationDef.keyFrames[index].regionY = jsonKeyFrame.getInt("regionY");
                    imageAnimationDef.keyFrames[index].regionWidth = jsonKeyFrame.getInt("regionWidth");
                    imageAnimationDef.keyFrames[index].regionHeight = jsonKeyFrame.getInt("regionHeight");
                    if(jsonKeyFrame.has("width")) {
                        imageAnimationDef.keyFrames[index].width = jsonKeyFrame.getFloat("width");
                    }
                    if(jsonKeyFrame.has("height")) {
                        imageAnimationDef.keyFrames[index].height = jsonKeyFrame.getFloat("height");
                    }
                }
            }
            return imageAnimationDef;
        }
        else if(animationDef instanceof TranslateAnimationDef){
            final TranslateAnimationDef translateAnimationDef = ((TranslateAnimationDef) animationDef);
            if(jsonAnimation.has("keyFrames")) {
                animationDef.keyFrames = new TranslateAnimationDef.KeyFrame[jsonAnimation.get("keyFrames").size];
                for(int index=0; index < animationDef.keyFrames.length; index++){
                    final JsonValue jsonKeyFrame = jsonAnimation.get("keyFrames").get(index);
                    translateAnimationDef.keyFrames[index] = new TranslateAnimationDef.KeyFrame();
                    translateAnimationDef.keyFrames[index].xOffset = jsonKeyFrame.getFloat("xOffset");
                    translateAnimationDef.keyFrames[index].yOffset = jsonKeyFrame.getFloat("yOffset");
                    if(jsonKeyFrame.has("interpolator")) {
                        translateAnimationDef.keyFrames[index].interpolator = AnimationDef.Interpolator.valueOf(jsonKeyFrame.getString("interpolator"));
                    }
                }
            }
            return translateAnimationDef;
        }
        else if(animationDef instanceof RotateAnimationDef){
            final RotateAnimationDef rotateAnimationDef = ((RotateAnimationDef) animationDef);
            if(jsonAnimation.has("keyFrames")) {
                animationDef.keyFrames = new RotateAnimationDef.KeyFrame[jsonAnimation.get("keyFrames").size];
                for(int index=0; index < animationDef.keyFrames.length; index++){
                    final JsonValue jsonKeyFrame = jsonAnimation.get("keyFrames").get(index);
                    rotateAnimationDef.keyFrames[index] = new RotateAnimationDef.KeyFrame();
                    rotateAnimationDef.keyFrames[index].angle = jsonKeyFrame.getFloat("angle");
                    if(jsonKeyFrame.has("interpolator")) {
                        rotateAnimationDef.keyFrames[index].interpolator = AnimationDef.Interpolator.valueOf(jsonKeyFrame.getString("interpolator"));
                    }
                }
            }
            return rotateAnimationDef;
        }
        else if(animationDef instanceof ScaleAnimationDef){
            final ScaleAnimationDef scaleAnimationDef = ((ScaleAnimationDef) animationDef);
            if(jsonAnimation.has("keyFrames")) {
                animationDef.keyFrames = new ScaleAnimationDef.KeyFrame[jsonAnimation.get("keyFrames").size];
                for(int index=0; index < animationDef.keyFrames.length; index++){
                    final JsonValue jsonKeyFrame = jsonAnimation.get("keyFrames").get(index);
                    scaleAnimationDef.keyFrames[index] = new ScaleAnimationDef.KeyFrame();
                    scaleAnimationDef.keyFrames[index].xOffset = jsonKeyFrame.getFloat("xOffset");
                    scaleAnimationDef.keyFrames[index].yOffset = jsonKeyFrame.getFloat("yOffset");
                    if(jsonKeyFrame.has("interpolator")) {
                        scaleAnimationDef.keyFrames[index].interpolator = AnimationDef.Interpolator.valueOf(jsonKeyFrame.getString("interpolator"));
                    }
                }
            }
            return scaleAnimationDef;
        }
        else if(animationDef instanceof ColorAnimationDef){
            final ColorAnimationDef colorAnimationDef = ((ColorAnimationDef) animationDef);
            if(jsonAnimation.has("keyFrames")) {
                animationDef.keyFrames = new ColorAnimationDef.KeyFrame[jsonAnimation.get("keyFrames").size];
                for(int index=0; index < animationDef.keyFrames.length; index++){
                    final JsonValue jsonKeyFrame = jsonAnimation.get("keyFrames").get(index);
                    colorAnimationDef.keyFrames[index] = new ColorAnimationDef.KeyFrame();
                    final float[] floatColor = jsonKeyFrame.get("color").asFloatArray();
                    colorAnimationDef.keyFrames[index].color = new Color(floatColor[0],floatColor[1],floatColor[2],floatColor[3]);
                    if(jsonKeyFrame.has("interpolator")) {
                        colorAnimationDef.keyFrames[index].interpolator = AnimationDef.Interpolator.valueOf(jsonKeyFrame.getString("interpolator"));
                    }
                }
            }
            return colorAnimationDef;
        }

        return null;
    }

    /**
     * Helper method to build a ParticleEffect from its name and the asset manager owning the resources
     *
     * @param name The name of the particles effect
     * @param assetManager The asset manager contaning the resources
     * @return A newly created ParticleEffect
     */
    public ParticleEffect getParticleEffect(final String name, final AssetManager assetManager){
        final ParticlesEffectDef particlesEffectDef = this.getParticlesEffectDefinition(name);
        final ParticleEffect particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal(particlesEffectDef.path), this.getTextureAtlas(assetManager));
        return particleEffect;
    }

    /**
     * Gets the list of particles effects in the Scene
     *
     * @return The lis of particles effects
     */
    public Array<ParticlesEffectDef> getParticlesEffectsDefinition(){
        if(this.jsonRoot.has("particles_effect")) {
            final Array<ParticlesEffectDef> particlesEffectDefs = new Array<ParticlesEffectDef>(true, this.jsonRoot.get("particles_effect").size);
            for (final JsonValue jsonParticlesEffect : this.jsonRoot.get("particles_effect")) {
                particlesEffectDefs.add(this.getParticlesEffectDefinition(jsonParticlesEffect));
            }
            return particlesEffectDefs;
        }
        return new Array<ParticlesEffectDef>(true, 0);
    }

    /**
     * Gets a particles effect from its name in the Scene
     *
     * @param name The particles effects name
     *
     * @return The particle effects definition
     */
    public ParticlesEffectDef getParticlesEffectDefinition(final String name){
        if(this.jsonRoot.has("particles_effect")) {
            for(final JsonValue jsonParticlesEffect : this.jsonRoot.get("particles_effect")){
                if(jsonParticlesEffect.has("name") && jsonParticlesEffect.getString("name").equals(name)) {
                    return this.getParticlesEffectDefinition(jsonParticlesEffect);
                }
            }
        }
        return null;
    }

    private ParticlesEffectDef getParticlesEffectDefinition(final JsonValue jsonParticlesEffect){
        final ParticlesEffectDef particlesEffectDef = new ParticlesEffectDef();
        if(jsonParticlesEffect.has("name")) {
            particlesEffectDef.name = jsonParticlesEffect.getString("name");
        }
        if(jsonParticlesEffect.has("file")) {
            particlesEffectDef.path = jsonParticlesEffect.getString("file");
        }
        return particlesEffectDef;
    }

    /**
     * Gets the list of fixtures definition for the body at specified index
     *
     * @param bodyIndex The body index
     *
     * @return A list of FixtureDef
     */
    public Array<FixtureDef> getFixturesDefinition(final int bodyIndex){
        if(this.jsonRoot.has("fixture") && this.jsonRoot.has("body")) {
            final JsonValue jsonBody = this.jsonRoot.get("body").get(bodyIndex);
            final Array<FixtureDef> fixtureDefs = new Array<FixtureDef>(true, 16);
            for (final JsonValue jsonFixture : jsonBody.get("fixture")) {
                final FixtureDef fixtureDef = new FixtureDef();
                if (jsonFixture.has("density")) {
                    fixtureDef.density = jsonFixture.getFloat("density");
                }
                if (jsonFixture.has("filter-categoryBits")) {
                    fixtureDef.filter.categoryBits = jsonFixture.getShort("filter-categoryBits");
                }
                if (jsonFixture.has("filter-groupIndex")) {
                    fixtureDef.filter.categoryBits = jsonFixture.getShort("filter-groupIndex");
                }
                if (jsonFixture.has("filter-maskBits")) {
                    fixtureDef.filter.maskBits = jsonFixture.getShort("filter-maskBits");
                }
                if (jsonFixture.has("friction")) {
                    fixtureDef.friction = jsonFixture.getFloat("friction");
                }
                if (jsonFixture.has("restitution")) {
                    fixtureDef.restitution = jsonFixture.getFloat("restitution");
                }

                if (jsonFixture.has("sensor")) {
                    fixtureDef.isSensor = jsonFixture.getBoolean("sensor");
                }
                if (jsonFixture.has("polygon")) {
                    final PolygonShape polygonShape = new PolygonShape();
                    final float[] xVertices = jsonFixture.get("polygon").get("vertices").get("x").asFloatArray();
                    final float[] yVertices = jsonFixture.get("polygon").get("vertices").get("y").asFloatArray();
                    final float[] vertices = new float[xVertices.length + yVertices.length];
                    for (int inIndex = 0, outIndex = 0; inIndex < xVertices.length; inIndex++, outIndex += 2) {
                        vertices[outIndex] = xVertices[inIndex];
                        vertices[outIndex + 1] = yVertices[inIndex];
                    }
                    polygonShape.set(vertices);
                    fixtureDef.shape = polygonShape;
                } else if (jsonFixture.has("chain")) {
                    final ChainShape chainShape = new ChainShape();
                    final float[] xVertices = jsonFixture.get("chain").get("vertices").get("x").asFloatArray();
                    final float[] yVertices = jsonFixture.get("chain").get("vertices").get("y").asFloatArray();
                    final float[] vertices = new float[xVertices.length + yVertices.length];
                    for (int inIndex = 0, outIndex = 0; inIndex < xVertices.length; inIndex++, outIndex += 2) {
                        vertices[outIndex] = xVertices[inIndex];
                        vertices[outIndex + 1] = yVertices[inIndex];
                    }
                    if (jsonFixture.get("chain").has("hasPrevVertex") && jsonFixture.get("chain").getBoolean("hasPrevVertex")) {
                        chainShape.setPrevVertex(jsonFixture.get("chain").get("prevVertex").getFloat("x"), jsonFixture.get("chain").get("prevVertex").getFloat("y"));
                    }
                    if (jsonFixture.get("chain").has("hasNextVertex") && jsonFixture.get("chain").getBoolean("hasNextVertex")) {
                        chainShape.setPrevVertex(jsonFixture.get("chain").get("nextVertex").getFloat("x"), jsonFixture.get("chain").get("nextVertex").getFloat("y"));
                    }
                    chainShape.createChain(vertices);
                    fixtureDef.shape = chainShape;
                } else if (jsonFixture.has("circle")) {
                    final CircleShape circleShape = new CircleShape();
                    if (jsonFixture.get("circle").get("center").has("x")) {
                        circleShape.setPosition(new Vector2(jsonFixture.get("circle").get("center").getFloat("x"), jsonFixture.get("circle").get("center").getFloat("y")));
                    }
                    circleShape.setRadius(jsonFixture.get("circle").getFloat("radius"));
                    fixtureDef.shape = circleShape;
                }
                fixtureDefs.add(fixtureDef);
            }
            return fixtureDefs;
        }
        return new Array<FixtureDef>(true, 0);
    }

    /**
     * Gets the list of joints definition
     *
     * @return A list of JointDef
     */
    public Array<JointDef> getJointsDefinition(){
        if(this.jsonRoot.has("joint")) {
            final Array<JointDef> jointDefs = new Array<JointDef>(true,16);
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
            return jointDefs;
        }
        return new Array<JointDef>(true, 0);
    }

    /**
     * Sets the inner JSON root from a file
     *
     * @param fileHandle The JSON file
     */
    @Override
    public void parse(FileHandle fileHandle) {
        if(this.textureAtlas != null) {
            this.textureAtlas.getRegions().clear();
            this.textureAtlas.getTextures().clear();
        }
        super.parse(fileHandle);
    }

    /**
     * Sets the inner JSON from a string
     *
     * @param jsonString The JSON string
     */
    @Override
    public void parse(String jsonString) {
        if(this.textureAtlas != null) {
            this.textureAtlas.getRegions().clear();
            this.textureAtlas.getTextures().clear();
        }
        super.parse(jsonString);
    }

    /**
     * Sets the inner JSON root from an JSONValue
     *
     * @param jsonRoot The JSON root
     */
    @Override
    public void parse(JsonValue jsonRoot) {
        if(this.textureAtlas != null) {
            this.textureAtlas.getRegions().clear();
            this.textureAtlas.getTextures().clear();
        }
        super.parse(jsonRoot);
    }

    /**
     * BodyDef extension to support name
     */
    public static class BodyDef extends com.badlogic.gdx.physics.box2d.BodyDef{
        public int index;
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
     * Image defintion (in "images"):
     * {
     *  "name" : name,
     *  "file" : "path/to/image",
     *  "body" : bodyId,
     *  "regionX" : region x,
     *  "regionY" : region y,
     *  "regionWidth" : region width,
     *  "regionHeight" : region height,
     *  "width" : width (optional),
     *  "height" : height (optional)
     *  "x" : body xOffset or world xOffset (optional),
     *  "y" : body yOffset or world yOffset (optional),
     *  "normalOffset" : {
     *      "x" : normal X offset
     *      "y" : normal Y offset
     *  }
     * }
     */
    public static class ImageDef {
        public String name;
        public String path;
        public int body;
        public float x;
        public float y;
        public float width;
        public float height;
        public int regionX;
        public int regionY;
        public int regionWidth;
        public int regionHeight;
        public Vector2 normalOffset = new Vector2();
    }

    /**
     *  Particles effects definition (in "particles_effect")
     *  {
     *  "name" : name,
     *  "file" : "path/to/particle_effect"
     *  }
     *
     */
    public static class ParticlesEffectDef{
        public String name;
        public String path;
    }


    /**
     *  ImageAnimation definition (in "animation")
     *  {
     *  "name" : name,
     *  "type" : "IMAGE"
     *  "file" : path/to/image,
     *  "playMode" : "LOOP" | "REVERSED" | "LOOP" | "LOOP_REVERSED" | "LOOP_PINGPONG" | "LOOP_RANDOM",
     *  "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE",
     *  frameDuration : frame duration (seconds)
     *  keyFrames : [
     *      {
     *          "regionX" : region x,
     *          "regionY" : region y,
     *          "regionWidth" : region width,
     *          "regionHeight" : region height,
     *          "width" : width (optional),
     *          "height" : height (optional)
     *      }
     *      ...
     *  ]
     *  }
     *
     */
    public static class ImageAnimationDef extends AnimationDef<ImageAnimationDef.KeyFrame>{
        public String path;
        public static class KeyFrame{
            public int regionX;
            public int regionY;
            public int regionWidth;
            public int regionHeight;
            public float width;
            public float height;
        }
    }

    /**
     *  TranslateAnimation definition (in "animation")
     *  {
     *  "name" : name,
     *  "type" : "TRANSLATE",
     *  "playMode" : "LOOP" | "REVERSED" | "LOOP_REVERSED" | "LOOP_PINGPONG", (optional -> default : NORMAL)
     *  "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *  frameDuration : frame duration (seconds)
     *  keyFrames : [
     *      {
     *          "xOffset" : x offset,
     *          "yOffset" : y offset,
     *          "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *      }
     *      ...
     *  ]
     *  }
     *
     */
    public static class TranslateAnimationDef extends AnimationDef<TranslateAnimationDef.KeyFrame>{
        public static class KeyFrame{
            public float xOffset;
            public float yOffset;
            public Interpolator interpolator = Interpolator.LINEAR;
        }
    }

    /**
     *  ScaleAnimation definition (in "animation")
     *  {
     *  "name" : name,
     *  "type" : "SCALE",
     *  "playMode" : "LOOP" | "REVERSED" | "LOOP_REVERSED" | "LOOP_PINGPONG", (optional -> default : NORMAL)
     *  "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *  frameDuration : frame duration (seconds)
     *  keyFrames : [
     *      {
     *          "xOffset" : x offset,
     *          "yOffset" : y offset,
     *          "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *      }
     *      ...
     *  ]
     *  }
     *
     */
    public static class ScaleAnimationDef extends AnimationDef<ScaleAnimationDef.KeyFrame>{
        public static class KeyFrame{
            public float xOffset;
            public float yOffset;
            public Interpolator interpolator = Interpolator.LINEAR;
        }
    }

    /**
     *  ColorAnimation definition (in "animation")
     *  {
     *  "name" : name,
     *  "type" : "SCALE",
     *  "playMode" : "LOOP" | "REVERSED" | "LOOP_REVERSED" | "LOOP_PINGPONG", (optional -> default : NORMAL)
     *  "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *  frameDuration : frame duration (seconds)
     *  keyFrames : [
     *      {
     *          "color" : [r,g,b,a] (float format),
     *          "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *      }
     *      ...
     *  ]
     *  }
     *
     */
    public static class ColorAnimationDef extends AnimationDef<ColorAnimationDef.KeyFrame>{
        public static class KeyFrame{
            public Color color;
            public Interpolator interpolator = Interpolator.LINEAR;
        }
    }

    /**
     *  RotateAnimation definition (in "animation")
     *  {
     *  "name" : name,
     *  "type" : "ROTATE",
     *  "playMode" : "LOOP" | "REVERSED" | "LOOP_REVERSED" | "LOOP_PINGPONG", (optional -> default : NORMAL)
     *  "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *  frameDuration : frame duration (seconds)
     *  keyFrames : [
     *      {
     *          "angle" : angle in degrees,
     *          "interpolator" : "LINEAR" | "FADE" | "POW2" | "POW3" | "POW4" | "POW5" | "SINE" | "EXP5" | "EXP10" | "CIRCLE" | "ELASTIC" | "SWING" | "BOUNCE", (optional -> default : LINEAR)
     *      }
     *      ...
     *  ]
     *  }
     *
     */
    public static class RotateAnimationDef extends AnimationDef<RotateAnimationDef.KeyFrame>{
        public static class KeyFrame{
            public float angle;
            public Interpolator interpolator = Interpolator.LINEAR;
        }
    }

    public static class AnimationDef<T>{
        public enum Type{
            IMAGE,
            TRANSLATE,
            ROTATE,
            SCALE,
            COLOR
        }

        public enum Interpolator{
            LINEAR,
            FADE,
            POW2,
            POW2_IN,
            POW2_OUT,
            POW3,
            POW3_IN,
            POW3_OUT,
            POW4,
            POW4_IN,
            POW4_OUT,
            POW5,
            POW5_IN,
            POW5_OUT,
            SINE,
            SINE_IN,
            SINE_OUT,
            EXP5,
            EXP5_IN,
            EXP5_OUT,
            EXP10,
            EXP10_IN,
            EXP10_OUT,
            CIRCLE,
            CIRCLE_IN,
            CIRCLE_OUT,
            ELASTIC,
            ELASTIC_IN,
            ELASTIC_OUT,
            SWING,
            SWING_IN,
            SWING_OUT,
            BOUNCE,
            BOUNCE_IN,
            BOUNCE_OUT;

            public Interpolation toInterpolation(){
                switch(this){
                    case FADE : return Interpolation.fade;
                    case POW2 : return Interpolation.pow2;
                    case POW2_IN : return Interpolation.pow2In;
                    case POW2_OUT : return Interpolation.pow2Out;
                    case POW3 : return Interpolation.pow3;
                    case POW3_IN : return Interpolation.pow3In;
                    case POW3_OUT : return Interpolation.pow3Out;
                    case POW4 : return Interpolation.pow4;
                    case POW4_IN : return Interpolation.pow4In;
                    case POW4_OUT : return Interpolation.pow4Out;
                    case POW5 : return Interpolation.pow5;
                    case POW5_IN : return Interpolation.pow5In;
                    case POW5_OUT : return Interpolation.pow5Out;
                    case SINE : return Interpolation.sine;
                    case SINE_IN : return Interpolation.sineIn;
                    case SINE_OUT : return Interpolation.sineOut;
                    case EXP5 : return Interpolation.exp5;
                    case EXP5_IN : return Interpolation.exp5In;
                    case EXP5_OUT : return Interpolation.exp5Out;
                    case EXP10 : return Interpolation.exp10;
                    case EXP10_IN : return Interpolation.exp10In;
                    case EXP10_OUT : return Interpolation.exp10Out;
                    case CIRCLE : return Interpolation.circle;
                    case CIRCLE_IN : return Interpolation.circleIn;
                    case CIRCLE_OUT : return Interpolation.circleOut;
                    case ELASTIC : return Interpolation.elastic;
                    case ELASTIC_IN : return Interpolation.elasticIn;
                    case ELASTIC_OUT : return Interpolation.elasticOut;
                    case SWING : return Interpolation.swing;
                    case SWING_IN : return Interpolation.swingIn;
                    case SWING_OUT : return Interpolation.swingOut;
                    case BOUNCE : return Interpolation.bounce;
                    case BOUNCE_IN : return Interpolation.bounceIn;
                    case BOUNCE_OUT : return Interpolation.bounceOut;
                    default : return Interpolation.linear;
                }
            }
        }

        public String name;
        public String type;
        public Animation.PlayMode playMode = Animation.PlayMode.NORMAL;
        public Interpolator interpolator = Interpolator.LINEAR;
        public float frameDuration;
        public T[] keyFrames;
    }
}