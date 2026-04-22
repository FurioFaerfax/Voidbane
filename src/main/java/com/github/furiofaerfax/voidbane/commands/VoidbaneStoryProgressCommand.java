package com.github.furiofaerfax.voidbane.commands;

import com.github.furiofaerfax.voidbane.VoidbanePlayerSettings;
import com.github.furiofaerfax.voidbane.VoidbaneSettings;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackTransaction;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.json.JsonObject;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VoidbaneStoryProgressCommand extends AbstractPlayerCommand {

    String storyPath = "/Server/Languages/en-US/story.json";
    String playersPath = "voidbane/players";

    public VoidbaneStoryProgressCommand() {
        super("setVoidbaneStoryProgress", "Super test command!");
        requirePermission (HytalePermissions.fromCommand("voidbaneCommands"));
    }
    RequiredArg<String> messageArg = this.withRequiredArg("npc_id", "Id for the target NPC", ArgTypes.STRING);
    RequiredArg<String> messageArg2 = this.withRequiredArg("npc_choice", "a choice attribute", ArgTypes.STRING);
    RequiredArg<String> messageArg3 = this.withRequiredArg("permissionlevel", "secret to prevent unwanted manipulation of the progression", ArgTypes.STRING);

    @Override
    protected void execute(@Nonnull CommandContext commandContext, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, @Nonnull PlayerRef playerRef, @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType()); // also a component
        int npc_id = Integer.parseInt(messageArg.get(commandContext)); // get the argument text by the player
        int npc_choice = Integer.parseInt(messageArg2.get(commandContext)); // get the argument text by the player
        String secretKey = messageArg3.get(commandContext); // get the argument text by the player
        VoidbanePlayerSettings voidbanePlayerSettings = VoidbaneSettings.getInstance().getPlayer(player.getDisplayName());
        if(voidbanePlayerSettings != null) {
            if(secretKey.equals("voidbaneStoryDoNotTouch")){

                voidbanePlayerSettings.checkChapterEnd();

                int lineInt = voidbanePlayerSettings.getCurrentNpcLine(npc_id);

                voidbanePlayerSettings.setNpcChoice(npc_id,npc_choice);

                String[] line = getStoryLine(voidbanePlayerSettings.currentChapter, npc_id, lineInt, voidbanePlayerSettings.chapterEndDecision[voidbanePlayerSettings.currentChapter-2]);
                if(!line[1].equals("last_line")) {
                    voidbanePlayerSettings.setCurrentNpcLineIncrement(npc_id);
                } else {
                    voidbanePlayerSettings.setCurrentNpcDone(npc_id, true);

                    String[][] checkPlayerCanGetReceiveGift = voidbanePlayerSettings.isPlayerAbleToGetGift(npc_id);
                    if(checkPlayerCanGetReceiveGift[0][0].equals("true")) {
                        boolean success = false;
                        for(int i = 0; i < Integer.parseInt(checkPlayerCanGetReceiveGift[0][1]); i++) {
                            success = giveStoryItemToPlayer(player, store, ref, checkPlayerCanGetReceiveGift[1][i], Integer.parseInt(checkPlayerCanGetReceiveGift[2][i]), checkPlayerCanGetReceiveGift[3][i].equals("") ? null : BsonDocument.parse(checkPlayerCanGetReceiveGift[3][i]), checkPlayerCanGetReceiveGift[4][i].equals("") ? Double.MAX_VALUE : Double.parseDouble(checkPlayerCanGetReceiveGift[4][i]));
                        }
                        if(success) {
                            voidbanePlayerSettings.setChapterGifts(npc_id);
                        }
                    }
                    voidbanePlayerSettings.checkChapterEnd();
                    VoidbaneSettings.getInstance().createPlayer(voidbanePlayerSettings.name, player.getUuid());
                }

                //If not boss
                if(npc_id > 0) {

                }
            } else {
                player.sendMessage(Message.raw("You do not have permíssion to perform this command"));
            }
        }
    }

    public boolean giveStoryItemToPlayer(Player player, @Nonnull Store<EntityStore> store, @Nonnull Ref<EntityStore> ref, String itemId, int quantity, BsonDocument metadata, double durability) {
        ItemStack stack = new ItemStack(itemId, quantity, metadata).withDurability(durability);
        ItemStackTransaction transaction = player.giveItem(stack, ref, store);
        ItemStack remainder = transaction.getRemainder();
        Message itemNameMessage = Message.translation(itemId);
        if (remainder != null && !remainder.isEmpty()) {
           player.sendMessage(Message.raw("Sorry but you cannot get it now! get some free space in your pockets!"));
            return false;
        } else {
            //player.sendMessage(Message.translation("server.commands.give.received").param("quantity", durability).param("item", itemNameMessage));
            return true;
        }
    }

    String[] getStoryLine(int chapterInt, int npcInt, int lineInt, int chapterDecision) {
        String[] args = {"",""};
        String chapterAlternative = "_0";
        if(chapterDecision != -1) {
            chapterAlternative = "_"+chapterDecision;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(playersPath))) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    Document story = loadJson();
                    BsonDocument bson =  new JsonObject(story.toJson()).toBsonDocument();
                    BsonDocument chapter;
                    if(bson.get("chapter"+chapterInt+chapterAlternative) == null) {
                        return new String[]{"this is sad, but theres no story here,yet. forgive me!", "no_chapter"};
                    } else {
                        chapter = bson.get("chapter"+chapterInt+chapterAlternative).asDocument();
                    }
                    BsonDocument npc = chapter.get("npc"+npcInt).asDocument();
                    if(npc.size() != 0) {
                        args[0] = npc.get("line"+lineInt).asString().getValue();
                        if(lineInt >= npc.size()){
                            args[1] = "last_line";
                        }
                    }else{
                        return new String[]{"this is sad, but theres no story here, forgive me!", "last_line"};
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return args;
    }

    public Document loadJson() {
        try (InputStream is = getClass().getResourceAsStream(storyPath)) {
            if (is == null) {
                System.out.println("File not found in resources!");
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line = "";
                String lines = "";
                while ((line = reader.readLine()) != null) {
                    lines += line;
                }
                Document doc = Document.parse(lines);
                return doc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}