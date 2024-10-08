package com.github.paopaoyue.wljmod.relic;

import com.github.paopaoyue.wljmod.card.Performer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Tax extends AbstractWljRelic {
    public static final String ID = "Wlj:Tax";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.SOLID;

    public static final int GOLD_GAIN = 2;

    public Tax() {
        super(ID, ImageMaster.loadImage("image/icon/tax.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }

    @Override
    public void bossObtainLogic() {
        this.isObtained = true;
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if (c instanceof Performer) {
            return damage + c.baseDamage;
        }
        return damage;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(Money.ID);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Tax();
    }
}
