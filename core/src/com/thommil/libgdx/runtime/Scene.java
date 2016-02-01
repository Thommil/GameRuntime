package com.thommil.libgdx.runtime;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Main container for Graphics and Physics actors.
 * <br/><br/>
 * This scene is by default handling physics and should be rewritten to
 * deactivate physics support.
 *
 * Created by thommil on 01/02/16.
 */
public class Scene implements Screen {

    /**
     * Physics World container
     */
    protected World physicsWorld;

    /**
     * Default constructor with earth gravity
     */
    public Scene(){
        this(new Vector2(0, -9.8f));
    }

    /**
     * Custom gravity constructor
     *
     * @param gravity The initial gravity of the scene
     */
    public Scene(Vector2 gravity){
        this.physicsWorld = new World(gravity, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.physicsWorld.dispose();
    }
}
