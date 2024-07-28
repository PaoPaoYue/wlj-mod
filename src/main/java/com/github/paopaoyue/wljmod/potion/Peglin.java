package com.github.paopaoyue.wljmod.potion;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.DiscardWithCallbackAction;
import com.github.paopaoyue.wljmod.action.FixWaitAction;
import com.github.paopaoyue.wljmod.action.LayoffAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class Peglin extends AbstractPotion {

    public static final String POTION_ID = "Wlj:Peglin";
    private static final PotionStrings potionStrings;

    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    }

    public Peglin() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.EYE, PotionEffect.NONE, Color.GRAY, Color.DARK_GRAY, null);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.description = potionStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        Keyword wljWorker = WljMod.MOD_DICTIONARY.get("Wlj:Worker");
        Keyword wljLayoff = WljMod.MOD_DICTIONARY.get("Wlj:Layoff");
        this.tips.add(new PowerTip(wljWorker.NAMES[0], wljWorker.DESCRIPTION));
        this.tips.add(new PowerTip(wljLayoff.NAMES[0], wljLayoff.DESCRIPTION));
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        this.addToBot(new DiscardWithCallbackAction(target, target, 99, true, false, cards -> {
            this.addToTop(new LayoffAction(99, true, cards::contains));
            this.addToTop(new FixWaitAction(0.5F));
        }));
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return -1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new Peglin();
    }

}