package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.effect.RollDiceEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RollDiceAction extends AbstractGameAction {
    public RollDiceAction() {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.startDuration = Settings.ACTION_DUR_LONG;
        this.duration = this.startDuration;
        this.actionType = ActionType.SPECIAL;

        amount = AbstractDungeon.cardRng.random(1, 6);
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            AbstractDungeon.effectList.add(new RollDiceEffect(this.target.hb.cX, this.target.hb.cY, amount));
        }
        this.tickDuration();
    }
}
