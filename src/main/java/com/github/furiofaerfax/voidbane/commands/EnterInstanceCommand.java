package com.github.furiofaerfax.voidbane.commands;

import com.github.furiofaerfax.voidbane.VoidbanePlayerSettings;
import com.github.furiofaerfax.voidbane.VoidbaneSettings;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class EnterInstanceCommand extends  AbstractPlayerCommand{

    public EnterInstanceCommand() {
        super("setVoidbaneGivenGiftRepeat", "Super test command!");
        requirePermission (HytalePermissions.fromCommand("voidbaneCommands"));
    }

    RequiredArg<String> messageArg = this.withRequiredArg("permissionlevel", "Id for the target NPC", ArgTypes.STRING);

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType()); // also a component

        String secretKey = messageArg.get(commandContext); // get the argument text by the player
        VoidbanePlayerSettings voidbanePlayerSettings = VoidbaneSettings.getInstance().getPlayer(player.getDisplayName());
        if(voidbanePlayerSettings != null) {
            if (secretKey.equals("voidbaneStoryDoNotTouch")) {
                voidbanePlayerSettings.repeatGiftingWhereAllowed();
            }
        }
    }
}