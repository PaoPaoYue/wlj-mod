package com.github.paopaoyue.wljmod.character;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.github.paopaoyue.wljmod.card.Layoff;
import com.github.paopaoyue.wljmod.patch.AbstractCardEnum;
import com.github.paopaoyue.wljmod.patch.PlayerClassEnum;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Wlj extends CustomPlayer {

    public static final String CHARACTER_ID = "wlj";
    public static final int ENERGY_PER_TURN = 3; // how much energy you get every turn
    public static final String CHARACTER_SHOULDER_2 = "image/character/shoulder.png"; // campfire pose
    public static final String CHARACTER_SHOULDER_1 = "image/character/shoulder.png"; // another campfire pose
    public static final String CHARACTER_CORPSE = "image/character/corpse.png"; // dead corpse
    public static final int STARTING_HP = 66;
    public static final int MAX_HP = 66;
    public static final int MAX_ORB_SLOT = 0;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;
    public static final float CHARACTER_IMAG_SWITCH_MAX_DURATION = 0.2f;
    private static final Logger logger = LogManager.getLogger(Wlj.class.getName());
    public static String CHARACTER_IMG = "image/character/wlj.png";
    public static CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString("Wlj:Wlj");
    private float characterImgSwitchDuration = 0f;

    public Wlj() {
        super(CHARACTER_ID, PlayerClassEnum.WLJ, null, null, (String) null, null);

        this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values

        initializeClass(CHARACTER_IMG, CHARACTER_SHOULDER_2, // required call to load textures and setup energy/loadout
                CHARACTER_SHOULDER_1,
                CHARACTER_CORPSE,
                getLoadout(), 0F, 0F, 300.0F, 300.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("Wlj:Strike");
        retVal.add("Wlj:Strike");
        retVal.add("Wlj:Strike");
        retVal.add("Wlj:Strike");
        retVal.add("Wlj:Strike");
        retVal.add("Wlj:Defend");
        retVal.add("Wlj:Defend");
        retVal.add("Wlj:Defend");
        retVal.add("Wlj:Defend");
        retVal.add("Wlj:Defend");
        retVal.add("Wlj:Hire");
        retVal.add("Wlj:Layoff");
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("Wlj:Money");
        return retVal;
    }

    public CharSelectInfo getLoadout() { // the rest of the character loadout so includes your character select screen info plus hp and starting gold
        return new CharSelectInfo(characterStrings.NAMES[0], characterStrings.TEXT[0],
                STARTING_HP, MAX_HP, MAX_ORB_SLOT, STARTING_GOLD, HAND_SIZE,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.WLJ_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return Color.YELLOW.cpy();
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Layoff();
    }

    @Override
    public Color getCardTrailColor() {
        return Color.YELLOW.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 6;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("Wlj:OPENING", MathUtils.random(0.0F, 0.30F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "Wlj:OPENING";
    }

    @Override
    public String getLocalizedCharacterName() {
        return characterStrings.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Wlj();
    }

    @Override
    public String getSpireHeartText() {
        return characterStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.YELLOW.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.BLUNT_LIGHT, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.BLUNT_LIGHT};
    }

    @Override
    public String getVampireText() {
        return characterStrings.TEXT[2];
    }

    @Override
    public List<com.megacrit.cardcrawl.cutscenes.CutscenePanel> getCutscenePanels() {
        List<com.megacrit.cardcrawl.cutscenes.CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("image/scene/true_ending.png", "Wlj:TRUE_ENDING"));
        return panels;
    }

    public float getCharacterImgSwitchDuration() {
        return characterImgSwitchDuration;
    }

    public void setCharacterImgSwitchDuration(float characterImgSwitchDuration) {
        this.characterImgSwitchDuration = characterImgSwitchDuration;
    }

    public void SwitchCharacterImage(String img) {
        this.img = ImageMaster.loadImage(img);
        this.setCharacterImgSwitchDuration(CHARACTER_IMAG_SWITCH_MAX_DURATION);
    }

    @Override
    public void applyPreCombatLogic() {
        SfxUtil.resetAll();
        super.applyPreCombatLogic();
    }
}