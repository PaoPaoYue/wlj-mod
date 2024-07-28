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
import com.github.paopaoyue.wljmod.potion.Cup;
import com.github.paopaoyue.wljmod.potion.Peglin;
import com.github.paopaoyue.wljmod.potion.SlimeInAJar;
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

    public static int tempGold;
    public static int displayTempGold;

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
        BaseMod.addPotion(Cup.class, Color.GREEN, Color.BROWN, null, Cup.POTION_ID, PlayerClassEnum.WLJ);
        BaseMod.addPotion(SlimeInAJar.class, Color.PINK, Color.PURPLE, null, SlimeInAJar.POTION_ID, PlayerClassEnum.WLJ);
        BaseMod.addPotion(Peglin.class, Color.GRAY, Color.DARK_GRAY, null, Peglin.POTION_ID, PlayerClassEnum.WLJ);
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
        BaseMod.loadCustomStringsFile(PotionStrings.class, "localization/" + language + "/wlj_potions.json");
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
        BaseMod.addAudio("Wlj:PAY_1", "audio/pay_1.mp3");
        BaseMod.addAudio("Wlj:PAY_2", "audio/pay_2.mp3");
        BaseMod.addAudio("Wlj:PAY_3", "audio/pay_3.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_1", "audio/layoff_1.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_2", "audio/layoff_2.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_3", "audio/layoff_3.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_4", "audio/layoff_4.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_5", "audio/layoff_5.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_6", "audio/layoff_6.mp3");
        BaseMod.addAudio("Wlj:LAYOFF_DOUBLE", "audio/layoff_double.mp3");
        BaseMod.addAudio("Wlj:EGG_1", "audio/egg_1.mp3");
        BaseMod.addAudio("Wlj:EGG_2", "audio/egg_2.mp3");
        BaseMod.addAudio("Wlj:EGG_3", "audio/egg_3.mp3");
        BaseMod.addAudio("Wlj:SERMON_1", "audio/sermon_1.mp3");
        BaseMod.addAudio("Wlj:SERMON_2", "audio/sermon_2.mp3");
        BaseMod.addAudio("Wlj:SERMON_3", "audio/sermon_3.mp3");
        BaseMod.addAudio("Wlj:BROTHERHOOD_1", "audio/brotherhood_1.mp3");
        BaseMod.addAudio("Wlj:BROTHERHOOD_2", "audio/brotherhood_2.mp3");
        BaseMod.addAudio("Wlj:BROTHERHOOD_3", "audio/brotherhood_3.mp3");
        BaseMod.addAudio("Wlj:LOVE_1", "audio/love_1.mp3");
        BaseMod.addAudio("Wlj:LOVE_2", "audio/love_2.mp3");
        BaseMod.addAudio("Wlj:LOVE_3", "audio/love_3.mp3");
        BaseMod.addAudio("Wlj:LOVE_4", "audio/love_4.mp3");
        BaseMod.addAudio("Wlj:SIT_1", "audio/sit_1.mp3");
        BaseMod.addAudio("Wlj:SIT_2", "audio/sit_2.mp3");
        BaseMod.addAudio("Wlj:SIT_3", "audio/sit_3.mp3");
        BaseMod.addAudio("Wlj:SIT_4", "audio/sit_4.mp3");
        BaseMod.addAudio("Wlj:INVITE_1", "audio/invite_1.mp3");
        BaseMod.addAudio("Wlj:INVITE_2", "audio/invite_2.mp3");
        BaseMod.addAudio("Wlj:INVITE_3", "audio/invite_3.mp3");
        BaseMod.addAudio("Wlj:INVITE_4", "audio/invite_4.mp3");
        BaseMod.addAudio("Wlj:INVITE_5", "audio/invite_5.mp3");
        BaseMod.addAudio("Wlj:INVITE_6", "audio/invite_6.mp3");
        BaseMod.addAudio("Wlj:INVITE_7", "audio/invite_7.mp3");
    }

    @Override
    public void receiveStartGame() {
        avatarManager = new AvatarManager();
        workerManager = new WorkerManager();

        tempGold = 0;
        displayTempGold = 0;
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
