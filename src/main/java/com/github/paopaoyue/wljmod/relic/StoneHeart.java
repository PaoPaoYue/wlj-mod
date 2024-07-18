package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class StoneHeart extends CustomRelic {
    public static final String ID = "Wlj:Stone Heart";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;


    public StoneHeart() {
        super(ID, ImageMaster.loadImage("image/icon/stone_heart.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        for (int i = 0; i < 3; i++) {
            AbstractDungeon.player.discardPile.group.add(new Rabble());
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StoneHeart();
    }
}
