package com.github.paopaoyue.wljmod.relic;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Ship extends AbstractWljRelic {
    public static final String ID = "Wlj:Ship";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.HEAVY;

    public Ship() {
        super(ID, ImageMaster.loadImage("image/icon/ship.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Ship();
    }
}
