package com.thommil.libgdx.runtime;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        //Basic texture
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.basic.texture.BasicScene(), config);
        //Basic sprite
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.basic.sprite.BasicScene(), config);
        //Basic cache
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.basic.cache.BasicScene(), config);

        //Basic Physics
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
        //Stress Physics
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);
        //Collision Physics
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.physics.collision.PhysicsScene(), config);
        //Particles Physics
        return new IOSApplication(new com.thommil.libgdx.runtime.test.physics.particles.PhysicsScene(), config);

        //Kinematic input
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.input.kinematic.BasicScene(), config);
        //Physics input
        //return new IOSApplication(new com.thommil.libgdx.runtime.test.input.dynamic.PhysicsScene(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}