
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.furiofaerfax.voidbane;

import com.google.gson.JsonElement;
import com.hypixel.hytale.builtin.adventure.npcshop.npc.ActionOpenBarterShop;
import com.hypixel.hytale.builtin.adventure.npcshop.npc.BarterShopExistsValidator;
import com.hypixel.hytale.builtin.adventure.npcshop.npc.builders.BuilderActionOpenBarterShop;
import com.hypixel.hytale.builtin.adventure.shop.barter.BarterPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.InstructionType;
import com.hypixel.hytale.server.npc.asset.builder.holder.AssetHolder;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;

import java.util.EnumSet;
import javax.annotation.Nonnull;

public class VoidbaneShowDialog extends ActionBase {
  //  @Nonnull
    protected final String shopId;

    public VoidbaneShowDialog(@Nonnull VoidbaneStoryUI builder, @Nonnull BuilderSupport support) {
        super(builder);
        this.shopId = builder.getShopId(support);
    }

//    public String getShopId(@Nonnull BuilderSupport support) {
//        return this.shopId.get(support.getExecutionContext());
//    }

    @Override
    public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, @Nonnull InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {

        return super.canExecute(ref, role, sensorInfo, dt, store) && role.getStateSupport().getInteractionIterationTarget() != null;
    }


    @Override
    public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, @Nonnull InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
        super.execute(ref, role, sensorInfo, dt, store);

        Ref<EntityStore> playerRef = role.getStateSupport().getInteractionIterationTarget();
        if(playerRef == null) return  false;

        PlayerRef playerRefComp = store.getComponent(playerRef, PlayerRef.getComponentType());
        if(playerRefComp == null) return false;

        PlayerRef playerComp = store.getComponent(playerRef, PlayerRef.getComponentType());
        if(playerComp == null) return false;


//        playerComp.getPageManager().openCustomPage(
//                playerRef, store, new TutorialDialogPage(playerComp,ref,this.npcName, this.message)
//        );
        System.out.println("GHJKBK");

        return true;
    }


//    @Override
//    public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
//        super.execute(ref, role, sensorInfo, dt, store);
//        Ref<EntityStore> playerReference = role.getStateSupport().getInteractionIterationTarget();
//        if (playerReference == null) {
//
//            return false;
//        } else {
//            PlayerRef playerRefComponent = store.getComponent(playerReference, PlayerRef.getComponentType());
//            if (playerRefComponent == null) {
//                return false;
//            } else {
//                Player playerComponent = store.getComponent(playerReference, Player.getComponentType());
//                if (playerComponent == null) {
//                    return false;
//                } else {
//                    //playerComponent.getPageManager().openCustomPage(ref, store, new BarterPage(playerRefComponent, this.shopId));
//                    System.out.println("HGJIKOGHKJIOPLKHGOKJLGOKLJ");
//                    return true;
//                }
//            }
//        }
//    }

}
