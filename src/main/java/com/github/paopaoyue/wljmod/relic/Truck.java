package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Truck extends CustomRelic {
    public static final String ID = "Wlj:Truck";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.HEAVY;


    public Truck() {
        super(ID, ImageMaster.loadImage("image/icon/truck.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.beginPulse();
        this.pulse = true;
    }

    @Override
    public void onPlayerEndTurn() {
        this.stopPulse();
    }

    @Override
    public void onVictory() {
        this.stopPulse();
    }

    public int modifyLayoffAmount(int amountToLayoff) {
        if (this.pulse) {
            return amountToLayoff + 1;
        }
        return amountToLayoff;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Truck();
    }
}
