package com.thommil.libgdx.runtime.test.test_01_screens.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.actor.graphics.BitmapFontActor;
import com.thommil.libgdx.runtime.layer.BitmapFontBatchLayer;
import com.thommil.libgdx.runtime.screen.AbstractScreen;

/**
 * Loading screen
 *
 * @author Thommil on 3/4/16.
 */
public class LoadingScreen extends AbstractScreen implements com.thommil.libgdx.runtime.screen.LoadingScreen {

    final BitmapFontBatchLayer bitmapFontBatchLayer;
    final BitmapFontActor fontActor;
    final String textTemplate = "Loading - PROGRESS%";

    public LoadingScreen(Viewport viewport) {
        super(viewport);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 28;
        fontActor = new BitmapFontActor(0,generator.generateFont(parameter));
        generator.dispose();
        bitmapFontBatchLayer = new BitmapFontBatchLayer(viewport, 1);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        fontActor.setText(this.textTemplate.replaceAll("PROGRESS", "00"));
        fontActor.setPosition(-100,0);
        fontActor.setTargetWidth(200);
        bitmapFontBatchLayer.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.bitmapFontBatchLayer.addActor(fontActor);
        this.bitmapFontBatchLayer.show();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        bitmapFontBatchLayer.render(delta);
    }

    /**
     * Indicates the loading progress (0.0 - 1.0)
     *
     * @param progress The loading progress
     */
    @Override
    public void onLoadProgress(float progress) {
        progress *= 100f;
        if(progress < 10){
            fontActor.setText(this.textTemplate.replaceAll("PROGRESS", "0"+progress));
        }
        else{
            fontActor.setText(this.textTemplate.replaceAll("PROGRESS", String.valueOf(progress)));
        }
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {

    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        bitmapFontBatchLayer.resize(width, height);
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }


    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        this.bitmapFontBatchLayer.hide();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        fontActor.dispose();
        bitmapFontBatchLayer.dispose();
    }
}
