package com.github.paopaoyue.wljmod;

import basemod.*;
import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.card.AvatarHp;
import com.github.paopaoyue.wljmod.card.LayoffAmount;
import com.github.paopaoyue.wljmod.character.Wlj;
import com.github.paopaoyue.wljmod.component.AvatarManager;
import com.github.paopaoyue.wljmod.component.AvatarStrings;
import com.github.paopaoyue.wljmod.component.WorkerManager;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.PlayerClassEnum;
import com.github.paopaoyue.wljmod.potion.Cup;
import com.github.paopaoyue.wljmod.potion.Peglin;
import com.github.paopaoyue.wljmod.potion.SlimeInAJar;
import com.github.paopaoyue.wljmod.relic.Useless;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

@SpireInitializer
public class WljMod implements PostInitializeSubscriber, EditCharactersSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, AddAudioSubscriber, StartGameSubscriber, OnCardUseSubscriber, OnPlayerTurnStartSubscriber {

    public static final String MOD_ID = "Wlj";
    public static SpireConfig config = null;
    public static final HashMap<String, Keyword> KEYWORD_DICTIONARY = new HashMap<>();
    public static final HashMap<String, AvatarStrings> AVATAR_DICTIONARY = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(WljMod.class);

    public static AvatarManager avatarManager;
    public static WorkerManager workerManager;

    public static int tempGold;
    public static int displayTempGold;

    public enum ConfigField {
        VOICE_DISABLED("VoiceDisabled");

        final String id;

        ConfigField(String val) {
            this.id = val;
        }
    }

    public static boolean getVoiceDisabled() {
        if (config == null) return false;
        return config.getBool(ConfigField.VOICE_DISABLED.id);
    }

    public WljMod() {
        logger.info("instantiating WljMod");
        BaseMod.subscribe(this);
        BaseMod.addColor(AbstractCardEnum.WLJ_COLOR, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW,
                "image/512/bg_attack.png", "image/512/bg_skill.png", "image/512/bg_power.png",
                "image/512/energy_orb.png", "image/1024/bg_attack.png",
                "image/1024/bg_skill.png", "image/1024/bg_power.png",
                "image/1024/energy_orb.png", "image/icon/small_energy_orb.png");
        try {
            config = new SpireConfig(MOD_ID, "PrideModConfig");
        } catch (IOException e) {
            logger.error("WljMod config initialisation failed:", e);
        }
    }

    public static void initialize() {
        new WljMod();
    }

    public void receivePostInitialize() {
        Texture badgeTexture = ImageMaster.loadImage("image/icon/mod_badge.png");
        Gson gson = new Gson();
        ModInfo info = Arrays.stream(Loader.MODINFOS).filter(modInfo -> modInfo.ID.equals(MOD_ID)).findFirst().orElse(null);
        if (info == null) {
            logger.error("ModInfo not found");
            return;
        }
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("王老菊 Mod - 设置", 400.0f, 700.0f, settingsPanel, (me) -> {
        }));
        settingsPanel.addUIElement(new ModLabeledToggleButton("禁用打出部分卡牌时的老菊语音",
                350f, 650f, Settings.CREAM_COLOR, FontHelper.charDescFont,
                getVoiceDisabled(), settingsPanel, (label) -> {
        }, (button) -> {
            config.setBool(ConfigField.VOICE_DISABLED.id, button.enabled);
            try {
                config.save();
            } catch (IOException e) {
                logger.error("WljMod config save failed:", e);
            }
        }));
        BaseMod.registerModBadge(badgeTexture, info.Name, Strings.join(Arrays.asList(info.Authors), ','), info.Description, settingsPanel);
    }


    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AvatarHp());
        BaseMod.addDynamicVariable(new LayoffAmount());
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
        if (KEYWORD_DICTIONARY != null) {
            for (Keyword keyword : KEYWORD_DICTIONARY.values()) {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(MOD_ID)
                .any(CustomRelic.class, (info, relic) -> {
                    if (relic instanceof Useless) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, AbstractCardEnum.WLJ_COLOR);
                    }
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
                language = "zhs";
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
            KEYWORD_DICTIONARY.put(keyword.ID, keyword);
        }
        json = Gdx.files.internal("localization/" + language + "/wlj_avatars.json").readString(String.valueOf(StandardCharsets.UTF_8));
        AvatarStrings[] avatars = gson.fromJson(json, AvatarStrings[].class);
        for (AvatarStrings avatar : avatars) {
            AVATAR_DICTIONARY.put(avatar.ID, avatar);
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
