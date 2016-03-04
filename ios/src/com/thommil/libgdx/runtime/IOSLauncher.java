package com.thommil.libgdx.runtime;

import com.thommil.libgdx.runtime.test.test_02_spritecache.SpriteCacheGame;
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

        // TEST 02 - Screens Workflow
        game = new SpriteCacheGame();

        return new IOSApplication(game, config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}