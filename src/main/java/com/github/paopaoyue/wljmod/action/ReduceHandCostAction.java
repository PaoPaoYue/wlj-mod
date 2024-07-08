package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.function.Predicate;

public class ReduceHandCostAction extends AbstractGameAction {

    private final AbstractCard currentCard;
    private Predicate<AbstractCard> predicate;

    public ReduceHandCostAction(int reduceAmount, Predicate<AbstractCard> predicate) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.amount = reduceAmount;
        this.currentCard = AbstractDungeon.player.hoveredCard;
        this.predicate = predicate;
    }

    @Override
    public void update() {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (predicate.test(card) && card != currentCard) {
                card.setCostForTurn(card.costForTurn - amount);
            }
        }
        this.isDone = true;
    }
}