package com.github.paopaoyue.wljmod.power;

import com.badlogic.gdx.graphics.Texture;
import com.github.paopaoyue.wljmod.card.Invite;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TaiwuVillagePower extends AbstractPower {
    public static final String POWER_ID = "Wlj:Taiwu Village";
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture IMG = ImageMaster.loadImage("image/icon/taiwu_village.png");

    public TaiwuVillagePower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = IMG;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.canGoNegative = false;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.owner.isPlayer) {
            this.flash();
            this.addToBot(new MakeTempCardInHandAction(new Invite(), this.amount));
        }
    }

    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
    }

}
