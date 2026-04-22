package com.github.furiofaerfax.voidbane.builders;

import com.github.furiofaerfax.voidbane.actions.ActionTame;
import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.Builder;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.holder.StringArrayHolder;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;

import javax.annotation.Nonnull;

public class BuilderActionTame extends BuilderActionBase {
    protected final StringArrayHolder tameItems = new StringArrayHolder();


    @Nonnull
    @Override
    public String getShortDescription() {
        return "Progress in the story when interaction with the NPC";
    }


    @Nonnull
    @Override
    public String getLongDescription() {
        return this.getShortDescription();
    }


    @Nonnull
    @Override
    public Action build(BuilderSupport builderSupport) {
        return new ActionTame(this, builderSupport);
    }

    @Nonnull
    @Override
    public BuilderDescriptorState getBuilderDescriptorState() {
        return BuilderDescriptorState.Stable;
    }

    @Nonnull
    public Builder<Action> readConfig(@Nonnull JsonElement data) {
        this.requireStringArray(data, "TameItems", this.tameItems, 0, Integer.MAX_VALUE, null, BuilderDescriptorState.Stable, "The Items to Tame an NPC", "The Items to Tame an NPC");
        return super.readConfig(data);
    }


    public String[] getTameItems(@Nonnull BuilderSupport support) {
        return this.tameItems.get(support.getExecutionContext());
    }
}
