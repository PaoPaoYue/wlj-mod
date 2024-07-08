package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class LapseAction extends AbstractGameAction {

    private static final int LAYOFF_AMOUNT = 3;

    private static AbstractCard lastLapseCard;

    private final AbstractCard card;

    public LapseAction(AbstractCard card) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.amount = LAYOFF_AMOUNT;
        this.card = card;
    }

    public static void reset() {
        lastLapseCard = null;
    }

    @Override
    public void update() {
        if (lastLapseCard != null && !lastLapseCard.name.equals(card.name)) {
            this.addToTop(new LayoffAction(amount));
        }
        lastLapseCard = card;
        this.isDone = true;
    }
}