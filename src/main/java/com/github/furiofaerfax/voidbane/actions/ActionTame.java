package com.github.furiofaerfax.voidbane.actions;

import com.github.furiofaerfax.voidbane.builders.BuilderActionTame;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ActionTame extends ActionBase {
    protected final Set<String> tameItems;

    public ActionTame(@Nonnull BuilderActionTame builder, @Nonnull BuilderSupport support) {
        super(builder);
        this.tameItems = new HashSet<>(Arrays.asList(builder.getTameItems(support)));
    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        return super.canExecute(ref, role, sensorInfo, dt, store) && role.getStateSupport().getInteractionIterationTarget() != null;
    }

    @Override
    public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        super.execute(ref, role, sensorInfo, dt, store);
        Ref<EntityStore> playerReference = role.getStateSupport().getInteractionIterationTarget();
        if (playerReference == null) {
            return false;
        } else {
            PlayerRef playerRefComponent = store.getComponent(playerReference, PlayerRef.getComponentType());
            if (playerRefComponent == null) {
                return false;
            } else {
                Player player = store.getComponent(playerReference, Player.getComponentType());
                if (player == null) {
                    return false;
                } else {
                    //player.getPageManager().openCustomPage(ref, store, new BarterPage(playerRefComponent, this.shopId));
                    player.sendMessage(Message.raw("RAWR"));
                    return true;
                }
            }
        }
    }
}
