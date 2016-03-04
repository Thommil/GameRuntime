package com.thommil.libgdx.runtime.test.test_01_screens.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.BitmapFontActor;
import com.thommil.libgdx.runtime.layer.BitmapFontBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * Basic and dirty level implementation just to show that levels
 * can be created and handled totally outside framework just using
 * the Runtime.getInstance() API, Layers and Actors.
 *
 * @author  Thommil on 04/03/16.
 */
public class SampleLevel implements Disposable{

    final BitmapFontBatchLayer bitmapFontBatchLayer;
    final BitmapFontActor fontActor;

    public SampleLevel() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 64;
        fontActor = new BitmapFontActor(0,generator.generateFont(parameter));
        generator.dispose();
        bitmapFontBatchLayer = new BitmapFontBatchLayer(Runtime.getInstance().getViewport(),1);
    }

    public void build(){
        fontActor.setText("Playing Level :)");
        fontActor.setPosition(-240,0);
        fontActor.setTargetWidth(480);
        fontActor.setWrap(true);
        bitmapFontBatchLayer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.bitmapFontBatchLayer.addActor(fontActor);
        Runtime.getInstance().addLayer(bitmapFontBatchLayer);
        RuntimeProfiler.profile();
    }

    @Override
    public void dispose() {
        fontActor.dispose();
        bitmapFontBatchLayer.dispose();

    }
}
