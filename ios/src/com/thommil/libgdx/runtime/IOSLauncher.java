package com.thommil.libgdx.runtime;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import com.thommil.libgdx.runtime.test.basic.cache.CacheTestScene;
import com.thommil.libgdx.runtime.test.basic.sprite.SpriteTestScene;
import com.thommil.libgdx.runtime.test.basic.texture.TextureTestScene;
import com.thommil.libgdx.runtime.test.input.dynamic.PhysicsInputTestScene;
import com.thommil.libgdx.runtime.test.input.kinematic.KinematicInputTestScene;
import com.thommil.libgdx.runtime.test.physics.basic.BasicPhysicsTestScene;
import com.thommil.libgdx.runtime.test.physics.collision.CollisionTestScene;
import com.thommil.libgdx.runtime.test.physics.particles.SoftBodyTestScene;
import com.thommil.libgdx.runtime.test.physics.stress.PhysicsStressTestScene;
import com.thommil.libgdx.runtime.test.render.water.WaterTestScene;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        //Basic texture
        return new IOSApplication(new TextureTestScene(), config);
        //Basic sprite
        //return new IOSApplication(new SpriteTestScene(), config);
        //Basic cache
        //return new IOSApplication(new CacheTestScene(), config);

        //Basic Physics
        //return new IOSApplication(new BasicPhysicsTestScene(), config);
        //Stress Physics
        //return new IOSApplication(new PhysicsStressTestScene(), config);
        //Collision Physics
        //return new IOSApplication(new CollisionTestScene(), config);
        //Particles Physics
        //return new IOSApplication(new SoftBodyTestScene(), config);

        //Kinematic input
        //return new IOSApplication(new KinematicInputTestScene(), config);
        //Physics input
        //return new IOSApplication(new PhysicsInputTestScene(), config);

        //Water rendering
        //return new IOSApplication(new WaterTestScene(), config);

    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}