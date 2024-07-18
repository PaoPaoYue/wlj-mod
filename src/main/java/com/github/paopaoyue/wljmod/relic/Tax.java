package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.github.paopaoyue.wljmod.card.Performer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Tax extends CustomRelic {
    public static final String ID = "Wlj:Tax";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.SOLID;

    private static final int MAX_COUNTER = 10;
    private static final int GOLD_GAIN = 2;

    public Tax() {
        super(ID, ImageMaster.loadImage("image/icon/tax.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.counter = MAX_COUNTER;
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (!this.grayscale && card instanceof Performer) {
            this.flash();
            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(GOLD_GAIN);
            this.counter--;
            if (this.counter == 0) {
                this.grayscale = true;
            }
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.grayscale = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Tax();
    }
}
