package com.github.paopaoyue.wljmod.relic;

import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Money extends AbstractWljRelic {
    public static final String ID = "Wlj:Money";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.STARTER;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public Money() {
        super(ID, ImageMaster.loadImage("image/icon/money.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void onVictory() {
        this.flash();
        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(6));
        AbstractDungeon.player.gainGold(6);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Money();
    }
}
