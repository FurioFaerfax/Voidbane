package com.github.furiofaerfax.voidbane.pages;

import com.github.furiofaerfax.voidbane.commands.StoryProgressCommand;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
public class StoryDialogBoxPage extends InteractiveCustomUIPage<StoryDialogBoxPage.DialogEventData> {

    private final String chapter;
    private final String lineText;

    private StoryProgressCommand story;

    public static class DialogEventData {
        String action = "";
        public static final BuilderCodec<DialogEventData> CODEC = BuilderCodec.builder(DialogEventData.class, DialogEventData::new).append(new KeyedCodec<>("Action", Codec.STRING), (DialogEventData o, String v) -> o.action = v, (DialogEventData o) -> o.action).add().build();
    }

    public StoryDialogBoxPage(@Nonnull PlayerRef playerRef, String chapter, String lineText, StoryProgressCommand story) {
        super(playerRef, CustomPageLifetime.CantClose, DialogEventData.CODEC);
        this.chapter = chapter;
        this.lineText = lineText;
        this.story = story;
    }

    @Override
    public void build(@Nonnull Ref<EntityStore> ref,@Nonnull UICommandBuilder commandBuilder, @Nonnull UIEventBuilder eventBuilder,@Nonnull Store<EntityStore> store) {
        commandBuilder.append("VoidbaneDialogBox.ui");
        commandBuilder.set("#Chapter.Text", chapter);
        commandBuilder.set("#StoryLines.Text", lineText);
        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ContinueButton", new EventData().append("Action", "Continue"));
        eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CloseButton", new EventData().append("Action", "Close"));
    }

    @Override
    public void handleDataEvent(@Nonnull Ref<EntityStore> ref, @Nonnull Store<EntityStore> store, @Nonnull DialogEventData data) {
        Player player = (Player) store.getComponent(ref, Player.getComponentType());
         switch (data.action) {
            case "Continue":
                String text = story.progressDialog(store, ref)[0];
                updateLine(ref, store, text);
//                System.out.println(text);
                break;

            case "Close":
                player.getPageManager().setPage(ref, store, Page.None);
                story.closedDialog();
                story = null;
//                System.out.println("Closed");
                break;
            default: break;
        }
    }

    private void updateLine(Ref<EntityStore> ref, Store<EntityStore> store,String updatedText) {
        UICommandBuilder commandBuilder = new UICommandBuilder();
        UIEventBuilder eventBuilder = new UIEventBuilder();
        commandBuilder.set("#Chapter.Text", "Chapter "+story.getCurrentChapter());
        commandBuilder.set("#StoryLines.Text", updatedText);
        sendUpdate(commandBuilder, eventBuilder, false);
    }
}
