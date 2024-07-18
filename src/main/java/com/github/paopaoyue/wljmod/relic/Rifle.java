package com.github.paopaoyue.wljmod.relic;

import basemod.abstracts.CustomRelic;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Rifle extends CustomRelic {
    public static final String ID = "Wlj:Rifle";
    private static final RelicStrings strings = CardCrawlGame.languagePack.getRelicStrings(ID);
    private static final RelicTier TIER = RelicTier.RARE;
    private static final LandingSound SOUND = LandingSound.CLINK;


    public Rifle() {
        super(ID, ImageMaster.loadImage("image/icon/rifle.png"), TIER, SOUND);
    }

    public String getUpdatedDescription() {
        return strings.DESCRIPTIONS[0];
    }


    @Override
    public void onExhaust(final AbstractCard card) {
        if (!(card instanceof AbstractWorkerCard)) return;
        this.flash();
        this.addToTop(new DamageRandomEnemyAction(new DamageInfo(null, 3, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
    }


    @Override
    public AbstractRelic makeCopy() {
        return new Rifle();
    }
}
