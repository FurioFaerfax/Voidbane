package com.github.furiofaerfax.voidbane;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import org.bson.Document;
import org.bson.json.JsonObject;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public final class VoidbaneSettings {
    private static VoidbaneSettings instance;

    String version = "0.4.2";
    String settingsPath = "voidbane";
    String settingsFile= settingsPath+"/voidbaneSetting.json";
    String playersPath = settingsPath+"/players";

    ArrayList<VoidbanePlayerSettings> players = new ArrayList<>();


    private VoidbaneSettings() {
        File settingFile = new File(settingsFile);

        //Check here, if settings file exists, else save new
        if(settingFile.exists()) {
            Document doc = loadSettings();
            version = doc.getString("version");
            loadPlayers();
        } else {
            initializeSettings();
        }
    }

    public static VoidbaneSettings getInstance(){
        if(instance == null) {
            instance = new VoidbaneSettings();
        }
        return instance;
    }

    void initializeSettings(){
        try {
            Files.createDirectories(Paths.get(settingsPath));
            Files.createDirectories(Paths.get(playersPath));
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
        saveSettings();
    }

    public void saveSettings(){
        JsonObject jsonSettings = new JsonObject("{\n  \"version\": \""+version+"\"\n}");
        saveFile(jsonSettings, settingsFile);
    }

    void saveFile(JsonObject jsonString, String file) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(jsonString.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    void savePlayerData() {
        StringBuilder playersStrings = new StringBuilder();
        for(int i = 0; i < players.size(); i++) {
            playersStrings.append("    ").append("\"").append(i).append("\"").append(":").append(players.get(i).getJson());
            if(i != players.size()-1) {
                playersStrings.append(",\n");
            }
        }
    }


    Document loadSettings() {
        try {
            String json = Files.readString(Path.of(settingsFile));
            return Document.parse(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    Document loadPlayers() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(playersPath))) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    Document player = Document.parse(Files.readString(Path.of(playersPath+"/"+file.getFileName())));
                    players.add(new VoidbanePlayerSettings(player.getString("name"), player.getInteger("currentChapter"), player.getBoolean("repeatLastChapter"), player.getBoolean("npcHermitChapterGiftReceived"), player.getBoolean("npcDragonChapterGiftReceived"), player.getBoolean("hasReceivedBossGift1"), player.getInteger("currentLineHermit"), player.getInteger("currentLineDragon"), player.getBoolean("currentChapterHermitDone"), player.getBoolean("currentChapterDragonDone"), player.getInteger ("boss1Defeated")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }

    public VoidbanePlayerSettings getPlayer(String name) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).name.equals(name)) {
                return players.get(i);
            }
        }
        return null;
    }

    public void createPlayer(String name, UUID playerUUID) {
        File settingFile = new File(playersPath+"/"+name+".json");
        if(!settingFile.exists()) {
            VoidbanePlayerSettings player = new VoidbanePlayerSettings(name);

            Set<String> permissions = new HashSet<>();
            permissions.add("hytale.command.voidbaneCommands");
            PermissionsModule perms = PermissionsModule.get();
            perms.addUserPermission(playerUUID, permissions);

            players.add(player);
            saveFile(player.getJson(), playersPath + "/" + player.name + ".json");
        } else {
            VoidbanePlayerSettings player = getPlayer(name);
            saveFile(player.getJson(), playersPath + "/" + player.name + ".json");
        }
    }


}