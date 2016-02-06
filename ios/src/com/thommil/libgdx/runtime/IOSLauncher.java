package com.thommil.libgdx.runtime;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        //Basic
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.basic.BasicScene(), config);
        //Basic Physics
        return new IOSApplication(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
        //Basic stress Physics
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}