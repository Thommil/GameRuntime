
package com.thommil.libgdx.runtime.test.test_11_events;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_11_events.level.EventsLevel;

/**
 * Events TEST
 *
 * @author  thommil on 3/4/16.
 */
public class EventsGame extends Game{

    EventsLevel eventsLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.STRECTCH;
        settings.viewport.width = 100;
        settings.viewport.height = 100;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        eventsLevel = new EventsLevel();
        showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowScreen(final Screen screen) {

    }

    @Override
    protected void onResize(int width, int height) {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onDispose() {

    }
}
