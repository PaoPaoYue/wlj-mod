package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.card.Performer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SunKnightAction extends AbstractGameAction {

    public SunKnightAction() {
        this.duration = 0.0f;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        int i = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof AbstractWorkerCard && !(card instanceof Performer)) {
                AbstractCard replacement = new Performer();
                if (card.upgraded) {
                    replacement.upgrade();
                }
                this.addToTop(new TransformCardInHandAction(i, replacement));
            }
            i++;
        }
        this.isDone = true;
    }
}
