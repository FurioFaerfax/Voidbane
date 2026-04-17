package com.github.furiofaerfax.voidbane;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.github.furiofaerfax.voidbane.commands.ExampleCommand;
import com.github.furiofaerfax.voidbane.events.ExampleEvent;

import javax.annotation.Nonnull;

public class Voidbane extends JavaPlugin {

    public Voidbane(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);
    }
}