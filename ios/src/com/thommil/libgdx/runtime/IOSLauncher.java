package com.thommil.libgdx.runtime;

import com.thommil.libgdx.runtime.test.test_02_spritecache.SpriteCacheGame;
import com.thommil.libgdx.runtime.test.test_03_spritebatch.SpriteBatchGame;
import com.thommil.libgdx.runtime.test.test_04_rigidbody.RigidbodyGame;
import com.thommil.libgdx.runtime.test.test_05_softbody.SoftbodyGame;
import com.thommil.libgdx.runtime.test.test_06_input.InputGame;
import com.thommil.libgdx.runtime.test.test_07_particleseffect.ParticlesEffectGame;
import com.thommil.libgdx.runtime.test.test_08_liquid.LiquidGame;
import com.thommil.libgdx.runtime.test.test_09_normalmap.NormalMapGame;
import com.thommil.libgdx.runtime.test.test_10_fine_offscreen.FineOffscreenGame;
import com.thommil.libgdx.runtime.test.test_11_events.EventsGame;
import com.thommil.libgdx.runtime.test.test_12_layout.LayoutGame;
import com.thommil.libgdx.runtime.test.test_13_rube.RubeGame;
import com.thommil.libgdx.runtime.test.test_14_sunset.SunsetGame;
import com.thommil.libgdx.runtime.test.test_15_animation.AnimationGame;
import com.thommil.libgdx.runtime.test.test_16_blob.BlobGame;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();

        Game game;

        // TEST 01 - Screens Workflow
        //game = new ScreensGame();

        // TEST 02 - Sprite Cache
        //game = new SpriteCacheGame();

        // TEST 03 - Sprite Batch
        //game = new SpriteBatchGame();

        // TEST 04 - RigidBody
        //game = new RigidbodyGame();

        // TEST 05 - SoftBody
        //game= new SoftbodyGame();

        // TEST 06 - Input
        //game= new InputGame();

        // TEST 07 - Particles effect
        //game= new ParticlesEffectGame();

        // TEST 08 - Liquid
        //game= new LiquidGame();

        // TEST 09 - NormalMap
        //game= new NormalMapGame();

        // TEST 10 - Fine Offscreen rendering
        //game= new FineOffscreenGame();

        // TEST 11 - Events touch dispatcher
        //game= new EventsGame();

        // TEST 12 - Layout
        //game= new LayoutGame();

        // TEST 13 - Rube Loader
        //game= new RubeGame();

        // TEST 14 - Sunset
        //game= new SunsetGame();

        // TEST 15 - Animation
        //game= new AnimationGame();

        // TEST 16 - Blob
        game= new BlobGame();

        return new IOSApplication(game, config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}