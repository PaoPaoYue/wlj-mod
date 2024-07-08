package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DrawSpecificCardAction extends AbstractGameAction {

    private Predicate<AbstractCard> predicate;

    public DrawSpecificCardAction(int amount, Predicate<AbstractCard> predicate) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.startDuration = 0.0f;
        this.duration = this.startDuration;
        this.actionType = ActionType.WAIT;
        this.amount = amount;
        this.predicate = predicate;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            List<AbstractCard> drawPile = AbstractDungeon.player.drawPile.group;
            List<AbstractCard> tmp = drawPile.stream().filter(predicate).collect(Collectors.toList());
            if (!tmp.isEmpty()) {
                this.amount = Math.min(this.amount, tmp.size());
                for (int j = 0; j < this.amount; j++) {
                    AbstractCard card = tmp.get(AbstractDungeon.cardRandomRng.random(0, tmp.size() - 1));
                    for (int i = drawPile.indexOf(card); i < drawPile.size() - j - 1; i++) {
                        Collections.swap(drawPile, i, i + 1);
                    }
                }

                this.addToTop(new DrawCardAction(this.amount));
            }
        }
        this.tickDuration();
    }
}
