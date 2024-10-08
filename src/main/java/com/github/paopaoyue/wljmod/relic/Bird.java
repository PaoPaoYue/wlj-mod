package com.github.paopaoyue.wljmod.relic;

import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Bird extends AbstractWljRelic {
    public static final String ID = "Wlj:Bird";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.SHOP;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public Bird() {
        super(ID, ImageMaster.loadImage("image/icon/bird.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void onLoseGold() {
        if (AbstractDungeon.player.gold <= 0) {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)
                AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(200));
            AbstractDungeon.player.gainGold(200);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Bird();
    }
}
