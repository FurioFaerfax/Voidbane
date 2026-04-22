package com.github.furiofaerfax.voidbane.events;

import com.github.furiofaerfax.voidbane.VoidbaneSettings;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

public class VoidbanePlayerOnEnterLeave {

    public static void onPlayerReady(PlayerReadyEvent event) {
        var player = event.getPlayer();
        VoidbaneSettings.getInstance().createPlayer(player.getDisplayName(), player.getUuid());
    }

    public static void onDisconnect(PlayerDisconnectEvent playerDisconnectEvent) {

    }
}