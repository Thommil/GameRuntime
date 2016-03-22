package com.thommil.libgdx.runtime.test.test_13_rube.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.actor.physics.HeadlessBodyActor;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.ViewportLayout;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RubeLoader;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

import java.util.List;

/**
 * @author  Thommil on 04/03/16.
 */
public class RubeLevel implements Disposable {

    final TextureSet layoutTextureSet;
    final SpriteBatchLayer spriteBatchLayer;
    final ViewportLayout viewportLayout;
    final RubeLoader rubeLoader;

    int currentLayout = 0;

    public RubeLevel() {
        this.rubeLoader = new RubeLoader();
        this.rubeLoader.parse(Gdx.files.internal("scene/sample.json"));
        this.layoutTextureSet = new TextureSet(new Texture("static/rube.png"));
        this.spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),10);
        this.viewportLayout = new ViewportLayout(Runtime.getInstance().getViewport());

        final int bodyCount = this.rubeLoader.getBodyCount();
        for(int i=0; i < bodyCount; i++){
            final int bodyIndex = i;
            spriteBatchLayer.addActor(new HeadlessBodyActor(i) {
                @Override
                public BodyDef getDefinition() {
                    RubeLoader.BodyDef bodyDef = rubeLoader.getBodyDefinition(bodyIndex);
                    Gdx.app.log("","Adding "+bodyDef.name);
                    return bodyDef;
                }

                @Override
                public Array<FixtureDef> getFixturesDefinition() {
                    return rubeLoader.getFixturesDefinition(bodyIndex);
                }
            });
        }

        Runtime.getInstance().addLayer(spriteBatchLayer);
        RuntimeProfiler.profile();
    }

    public void layout(){

    }

    public void resize(int width, int height){
        this.viewportLayout.update(width, height);
        this.layout();
    }


    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.layoutTextureSet.dispose();
        this.spriteBatchLayer.dispose();
    }

}
