package com.github.furiofaerfax.voidbane;

import org.bson.json.JsonObject;

public class VoidbanePlayerSettings {
    public String name = "";

    public Integer currentChapter = 1;
    public boolean repeatLastChapter = false;
    public boolean hasReceivedBossGift1 = false;
    public Integer currentLineHermit = 1;
    public Integer currentLineDragon = 1;

    public boolean currentChapterHermitDone = false;
    public boolean[] npcHermitOccursInChapter = {true, true, true, true, true, true};
    public boolean[] npcHermitGiftInChapter = {false, false, true, false, true, false};
    public boolean[] npcHermitGiftRepeatable = {false, false, true, false, false, false};
    public String[][] npcHermitGifts = {{"",""},{"",""},{"Voidbane_Item_Twilightprism","1"},{"",""},{"Voidbane_Evil_Contained_Statue","1"},{"",""}};

    public boolean currentChapterDragonDone = true;
    public boolean[] npcDragonOccursInChapter = {false, false, false, true, true, true};
    public boolean[] npcDragonGiftInChapter = {false, false, false, true, true, false};
    public boolean[] npcDragonGiftRepeatable = {false, false, false, true, false, false};
    public String[][] npcDragonGifts = {{"",""},{"",""},{"",""},{"Voidbane_Flask_IcyTears","5", "Voidbane_Flask_Of_Draconic_Decorruption", "1"},{"Voidbane_Scale_Fire_Dragon","5"},{"",""}};

    public boolean npcHermitChapterGiftReceived = false;
    public boolean npcDragonChapterGiftReceived = false;


    public int boss1Defeated = -1; // -1 = false, 0=first outcome, 1=second outcome ...
    public int[] chapterEndDecision = {-1, -1, -1, -1, -1, -1};// -1 = not defeated, 0=first outcome, 1=second outcome ...


    VoidbanePlayerSettings(String name) {
        this.name = name;
    }

    VoidbanePlayerSettings(String name, int currentChapter, boolean repeatLastChapter, boolean npcHermitChapterGiftReceived, boolean npcDragonChapterGiftReceived, boolean hasReceivedBossGift1, int currentLineHermit, int currentLineDragon, boolean currentChapterHermitDone, boolean currentChapterDragonDone, int boss1Defeated) {
        this.name = name;
        this.currentChapter = currentChapter;
        this.repeatLastChapter = repeatLastChapter;
        this.npcHermitChapterGiftReceived = npcHermitChapterGiftReceived;
        this.npcDragonChapterGiftReceived = npcDragonChapterGiftReceived;
        this.hasReceivedBossGift1 = hasReceivedBossGift1;
        this.currentLineHermit = currentLineHermit;
        this.currentLineDragon = currentLineDragon;
        this.currentChapterHermitDone = currentChapterHermitDone;
        this.currentChapterDragonDone = currentChapterDragonDone;
        this.boss1Defeated = boss1Defeated;
        this.chapterEndDecision[3] = boss1Defeated;
    }

    public void setNpcChoice(int npcId, int choiceId) {
        switch (npcId) {
            case -1: chapterEndDecision[currentChapter-1] = choiceId; boss1Defeated = choiceId;break;
            default: ;break;
        }
    }

    public int getCurrentNpcLine(int npc_id) {
        switch(npc_id) {
            case 1: return currentLineHermit;
            case 2: return currentLineDragon;
            default: return 0;
        }
    }

    public void setCurrentNpcLineIncrement(int npc_id) {
        switch(npc_id) {
            case 1: currentLineHermit++;break;
            case 2: currentLineDragon++;break;
            default: break;
        }
    }

    public void setCurrentNpcDone(int npc_id, boolean done) {
        switch(npc_id) {
            case 1: currentChapterHermitDone = done;break;
            case 2: currentChapterDragonDone = done;break;
            default: break;
        }
    }

    public void checkChapterEnd() {
        if(currentChapterHermitDone && currentChapterDragonDone) {
            if(currentChapter >= 4 && chapterEndDecision[3] == -1) {

            } else {
                if((npcHermitGiftInChapter[currentChapter-1] == npcHermitChapterGiftReceived) && (npcDragonGiftInChapter[currentChapter-1] == npcDragonChapterGiftReceived)) {
                    progressChapter();
                }
            }
        }
    }

    public String[][] isPlayerAbleToGetGift(int npc_id){

        String[][] data = {{"false", ""}, {"",""}, {"",""}, {"",""}, {"",""}};//This needs to be done with another method such a list, for now it will work, forgot that java dont support classical arrays with dynamic size
        boolean canReceiveYet = false;
        int different_Items = 1;
        switch (npc_id) {
            case 1: if(npcHermitChapterGiftReceived){break;} canReceiveYet = npcHermitGiftInChapter[currentChapter-1]; different_Items =  npcHermitGifts[currentChapter-1].length/2;break;
            case 2: if(npcDragonChapterGiftReceived){break;} canReceiveYet = npcDragonGiftInChapter[currentChapter-1]; different_Items =  npcDragonGifts[currentChapter-1].length/2;break;
            default: break;
        }
        if(canReceiveYet) {
            data[0][0] = canReceiveYet+"";
            data[0][1] = different_Items+"";
            for(int i = 0; i < different_Items; i++) {
                switch (npc_id) {
                    case 1:
                        data[1][i] = npcHermitGifts[currentChapter-1][i*2];
                        data[2][i] = npcHermitGifts[currentChapter-1][i*2+1];
                        data[3][i] = "";
                        data[4][i] = "";
                        break;
                    case 2:
                        data[1][i] = npcDragonGifts[currentChapter-1][i*2];
                        data[2][i] = npcDragonGifts[currentChapter-1][i*2+1];
                        data[3][i] = "";
                        data[4][i] = "";
                        break;
                    default:break;
                }
            }
        }
        return data;
    }

    public void repeatGiftingWhereAllowed() {
        if(npcHermitGiftRepeatable[currentChapter-1]){npcHermitChapterGiftReceived = false;}
        if(npcDragonGiftRepeatable[currentChapter-1]){npcDragonChapterGiftReceived = false;}
    }

    public void setChapterGifts(int npc_id) {
        switch (npc_id) {
            case 1: npcHermitChapterGiftReceived = true;break;
            case 2: npcDragonChapterGiftReceived = true;break;
            default:;break;
        }
    }

    public void progressChapter() {
        currentLineHermit = 1;
        currentLineDragon = 1;
        currentChapter += 1;
        setChappterOccurance();
        npcHermitChapterGiftReceived = false;
        npcDragonChapterGiftReceived = false;
    }

    public void setChappterOccurance() {
        currentChapterHermitDone = !npcHermitOccursInChapter[currentChapter-1];
        currentChapterDragonDone = !npcDragonOccursInChapter[currentChapter-1];
    }

    JsonObject getJson() {
        return new JsonObject("{\n  \"name\": \""+name+"\",\n  \"currentChapter\": "+currentChapter+",\n  \"repeatLastChapter\": "+repeatLastChapter+",\n  \"npcHermitChapterGiftReceived\": "+npcHermitChapterGiftReceived+",\n  \"npcDragonChapterGiftReceived\": "+npcDragonChapterGiftReceived+",\n  \"hasReceivedBossGift1\": "+hasReceivedBossGift1+",\n  \"currentLineHermit\": "+currentLineHermit+",\n  \"currentLineDragon\": "+currentLineDragon+",\n  \"currentChapterHermitDone\": "+currentChapterHermitDone+",\n  \"currentChapterDragonDone\": "+currentChapterDragonDone+",\n  \"boss1Defeated\": "+boss1Defeated+"\n}");
    }

    VoidbanePlayerSettings get() {
        return this;
    }
}