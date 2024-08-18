package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;

public class ReplicateWorkerAction extends AbstractGameAction {

    private boolean upgraded;

    public ReplicateWorkerAction(int amount, boolean upgraded) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.upgraded = upgraded;
        this.duration = 0.0f;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        List<AbstractCard> workerInDiscardPile = WljMod.workerManager.getWorkerInDiscardPile();
        if (!workerInDiscardPile.isEmpty()) {
            for (int i = 0; i < amount; i++) {
                AbstractCard card = workerInDiscardPile
                        .get(AbstractDungeon.cardRandomRng.random(workerInDiscardPile.size() - 1))
                        .makeStatEquivalentCopy();
                if (upgraded)
                    card.setCostForTurn(card.cost - 1);
                this.addToTop(new MakeTempCardInHandAction(card));
            }
        }
        this.isDone = true;
    }
}