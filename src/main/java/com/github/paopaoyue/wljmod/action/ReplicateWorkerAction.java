package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

public class ReplicateWorkerAction extends AbstractGameAction {


    public ReplicateWorkerAction() {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;

    }

    @Override
    public void update() {
        int size = 10 - AbstractDungeon.player.hand.size();
        List<AbstractCard> workerInDiscardPile = WljMod.workerManager.getWorkerInDiscardPile();
        if (!workerInDiscardPile.isEmpty()) {
            for (int i = 0; i < size; i++) {
                AbstractCard card = workerInDiscardPile.get(AbstractDungeon.cardRandomRng.random(workerInDiscardPile.size() - 1));
                this.addToTop(new MakeTempCardInHandAction(card));
            }
        }
        this.isDone = true;
    }
}