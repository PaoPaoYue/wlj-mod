package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Predicate;

public class ReduceHandCostAction extends AbstractGameAction {

    private Predicate<AbstractCard> predicate;

    public ReduceHandCostAction(int reduceAmount, Predicate<AbstractCard> predicate) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.amount = reduceAmount;
        this.predicate = predicate;
    }

    @Override
    public void update() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (predicate.test(card) && card != AbstractDungeon.player.cardInUse && card.costForTurn > card.cost - amount) {
                card.setCostForTurn(card.cost - amount);
            }
        }
        this.isDone = true;
    }
}