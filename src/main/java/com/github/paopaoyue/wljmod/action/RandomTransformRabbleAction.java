package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.*;
import com.github.paopaoyue.wljmod.relic.Sheep;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RandomTransformRabbleAction extends AbstractGameAction {

    public RandomTransformRabbleAction() {
        this.duration = 0.0f;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        int i = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof Rabble) {
                AbstractCard replacement;
                int random = AbstractDungeon.cardRandomRng.random(0, 8);
                switch (random) {
                    case 0:
                        replacement = new Prisoner();
                        break;
                    case 1:
                        replacement = new Erwen();
                        break;
                    case 2:
                        replacement = new Crab();
                        break;
                    case 3:
                        replacement = new Gaoshi();
                        break;
                    case 4:
                        replacement = new Leidongxuan();
                        break;
                    case 5:
                        replacement = new Keel();
                        break;
                    default:
                        replacement = new Performer();
                        break;
                }
                if (card.upgraded) {
                    replacement.upgrade();
                }
                AbstractDungeon.player.getRelic(Sheep.ID).flash();
                this.addToTop(new TransformCardInHandAction(i, replacement));
                break;
            }
            i++;
        }
        this.isDone = true;
    }
}
