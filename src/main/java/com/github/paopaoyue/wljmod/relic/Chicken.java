package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Chicken extends CustomRelic {
    public static final String ID = "Wlj:Chicken";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.FLAT;

    public Chicken() {
        super(ID, ImageMaster.loadImage("image/icon/chicken.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        int amount = WljMod.workerManager.getWorkerCountOutsideDiscardPile();
        if (amount > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Chicken();
    }
}
