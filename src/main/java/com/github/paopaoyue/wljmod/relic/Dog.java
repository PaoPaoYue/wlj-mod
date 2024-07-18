package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Dog extends CustomRelic {
    public static final String ID = "Wlj:Dog";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public Dog() {
        super(ID, ImageMaster.loadImage("image/icon/dog.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.beginPulse();
        this.pulse = true;
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    public void onLayoff(int layoffSize) {
        if (this.pulse) {
            this.flash();
            this.stopPulse();
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, layoffSize));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Dog();
    }
}
