package com.github.paopaoyue.wljmod.power;

import com.badlogic.gdx.graphics.Texture;
import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CorruptionPower extends AbstractPower {
    public static final String POWER_ID = "Wlj:Corruption";
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final Texture IMG = ImageMaster.loadImage("image/icon/bridge_engineer.png");

    public CorruptionPower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        this.img = IMG;
        this.type = PowerType.DEBUFF;
        this.amount = amount;
        this.canGoNegative = false;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && this.owner.isPlayer) {
            if (this.amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
                return;
            }
            this.flash();
            AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(-this.amount));
            AbstractDungeon.player.loseGold(this.amount);
        }
    }

    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
    }

}
