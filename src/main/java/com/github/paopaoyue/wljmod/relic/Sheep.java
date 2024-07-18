package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.github.paopaoyue.wljmod.action.RandomTransformRabbleAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Sheep extends CustomRelic {
    public static final String ID = "Wlj:Sheep";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.SOLID;

    public Sheep() {
        super(ID, ImageMaster.loadImage("image/icon/sheep.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void onLoseGold() {
        AbstractDungeon.actionManager.addToBottom(new RandomTransformRabbleAction());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Sheep();
    }
}
