package com.github.paopaoyue.wljmod.power;

import com.badlogic.gdx.graphics.Texture;
import com.github.paopaoyue.wljmod.card.Prisoner;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PolicePower extends AbstractPower {
    public static final String POWER_ID = "Wlj:Police";
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture IMG = ImageMaster.loadImage("image/icon/police.png");

    public PolicePower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = IMG;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.canGoNegative = false;
        this.isTurnBased = true;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.owner.isPlayer) {
            this.flash();
            this.addToBot(new MakeTempCardInHandAction(new Prisoner(), this.amount));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
    }

}
