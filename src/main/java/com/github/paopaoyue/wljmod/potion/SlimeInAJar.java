package com.github.paopaoyue.wljmod.potion;

import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.action.SlimeInAJarAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class SlimeInAJar extends AbstractPotion {

    public static final String POTION_ID = "Wlj:SlimeInAJar";
    private static final PotionStrings potionStrings;

    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    }

    public SlimeInAJar() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.JAR, PotionColor.FAIRY);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = false;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        Keyword wljWorker = WljMod.KEYWORD_DICTIONARY.get("Wlj:Worker");
        this.tips.add(new PowerTip(wljWorker.NAMES[0], wljWorker.DESCRIPTION));
    }

    @Override
    public void use(AbstractCreature target) {
        this.addToBot(new SlimeInAJarAction(this.potency));
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 2;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new SlimeInAJar();
    }

}