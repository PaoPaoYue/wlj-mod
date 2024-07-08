package com.github.paopaoyue.wljmod.power;

import com.badlogic.gdx.graphics.Texture;
import com.github.paopaoyue.wljmod.card.Prisoner;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PrisonPower extends AbstractPower {
    public static final String POWER_ID = "Wlj:Prison";
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture IMG = ImageMaster.loadImage("image/icon/prison.png");

    private int count = 0;

    public PrisonPower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = IMG;
        this.type = PowerType.BUFF;
        this.amount = amount;
        this.count = this.amount;
        this.canGoNegative = false;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.count = this.amount;
        updateDescription();
    }

    public void onPreLayoff() {
        if (this.count > 0) {
            this.addToBot(new MakeTempCardInHandAction(new Prisoner()));
            this.count--;
            updateDescription();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.count += stackAmount;
    }

    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + this.count + strings.DESCRIPTIONS[1];
    }

}
