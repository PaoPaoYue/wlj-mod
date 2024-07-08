package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class LipAction extends AbstractGameAction {
    private AbstractMonster m;

    private int amount;

    public LipAction(AbstractMonster m, int amount) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.m = m;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (final AbstractCard c : DrawCardAction.drawnCards) {
            if (c.costForTurn == 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.m, AbstractDungeon.player, new WeakPower(this.m, this.amount, false), 1));
                break;
            }
        }
        this.isDone = true;
    }
}