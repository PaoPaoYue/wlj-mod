package com.github.paopaoyue.wljmod.action;


import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class GloryAction extends AbstractGameAction {
    public GloryAction(final AbstractPlayer source, final int amount) {
        this.setValues(this.target, source, amount);
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        int toDraw = WljMod.workerManager.getWorkerTypeCountInDiscardPile() * this.amount;
        if (toDraw > 0) {
            this.addToTop(new DrawCardAction(this.source, toDraw));
        }
        this.isDone = true;
    }
}

