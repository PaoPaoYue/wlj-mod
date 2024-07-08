package com.github.paopaoyue.wljmod;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.card.AvatarHp;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.github.paopaoyue.wljmod.component.AvatarManager;
import com.github.paopaoyue.wljmod.component.WorkerManager;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.PlayerClassEnum;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpireInitializer
public class WljMod implements EditCharactersSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, AddAudioSubscriber, StartGameSubscriber, OnCardUseSubscriber, OnPlayerTurnStartSubscriber {


    public static final String MOD_ID = "Wlj";
    public static final HashMap<String, Keyword> MOD_DICTIONARY = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(WljMod.class);

    public static AvatarManager avatarManager;
    public static WorkerManager workerManager;

    public WljMod() {
        logger.info("instantiating WljMod");
        BaseMod.subscribe(this);
        BaseMod.addColor(AbstractCardEnum.WLJ_COLOR, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW,
                "image/512/bg_attack.png", "image/512/bg_skill.png", "image/512/bg_power.png",
                "image/512/energy_orb.png", "image/1024/bg_attack.png",
                "image/1024/bg_skill.png", "image/1024/bg_power.png",
                "image/1024/energy_orb.png", "image/icon/small_energy_orb.png");
    }

    public static void initialize() {
        new WljMod();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AvatarHp());
        new AutoAdd(MOD_ID)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new Wlj(),
                "image/character_select/button.png",
                "image/character_select/portrait.png", PlayerClassEnum.WLJ);
    }

    @Override
    public void receiveEditKeywords() {
        if (MOD_DICTIONARY != null) {
            for (Keyword keyword : MOD_DICTIONARY.values()) {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_ID)
                .any(CustomRelic.class, (info, relic) -> {
                    BaseMod.addRelicToCustomPool(relic, AbstractCardEnum.WLJ_COLOR);
                    if (info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditStrings() {
        String language;
        switch (Settings.language) {
            case ZHS:
                language = "zhs";
                break;
            default:
                language = "eng";
                break;
        }
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization/" + language + "/wlj_characters.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, "localization/" + language + "/wlj_cards.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "localization/" + language + "/wlj_powers.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/" + language + "/wlj_relics.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "localization/" + language + "/wlj_ui.json");

//        BaseMod.loadCustomStringsFile(EventStrings.class, "localization/" + language + "/wlj_events.json");

        Gson gson = new Gson();
        String json = Gdx.files.internal("localization/" + language + "/wlj_keywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        for (Keyword keyword : keywords) {
            MOD_DICTIONARY.put(keyword.ID, keyword);
        }
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("Wlj:OPENING", "audio/opening.mp3");
        BaseMod.addAudio("Wlj:TRUE_ENDING", "audio/true_ending.mp3");
        BaseMod.addAudio("Wlj:ROLL_DICE", "audio/roll_dice.mp3");
    }

    @Override
    public void receiveStartGame() {
        avatarManager = new AvatarManager();
        workerManager = new WorkerManager();
    }

    public void receiveCardUsed(AbstractCard abstractCard) {
        if (abstractCard instanceof AbstractWorkerCard) {
            workerManager.increaseTotalWorkerPlayedThisTurn();
        }
    }

    public void receiveOnPlayerTurnStart() {
        workerManager.reset();
    }
}
