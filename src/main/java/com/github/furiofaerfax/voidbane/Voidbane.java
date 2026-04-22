package com.github.furiofaerfax.voidbane;


import com.github.furiofaerfax.voidbane.commands.EnterInstanceCommand;
import com.github.furiofaerfax.voidbane.commands.StoryProgressCommand;
import com.github.furiofaerfax.voidbane.events.PlayerOnEnterLeave;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketFilter;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class Voidbane extends JavaPlugin {

    private PacketFilter inboundFilter;
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();


    public Voidbane(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());

        this.getCommandRegistry().registerCommand(new StoryProgressCommand());
        this.getCommandRegistry().registerCommand(new EnterInstanceCommand());
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, PlayerOnEnterLeave::onPlayerReady);
//        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, PlayerOnEnterLeave::onDisconnect);


    }

//    @Override
//    protected void start() {
////        LOGGER.atInfo().log("On Start: Registering Tame Action");
////        NPCPlugin.get().registerCoreComponentType("Tame", BuilderActionTame::new);
//    }
//
//    @Override
//    protected void shutdown() {
//        if (inboundFilter != null) {
//            PacketAdapters.deregisterInbound(inboundFilter);
//        }
//    }



}