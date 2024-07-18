package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.*;
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
            if (card instanceof AbstractWorkerCard) {
                AbstractCard replacement;
                int random = AbstractDungeon.cardRandomRng.random(0, 7);
                switch (random) {
                    case 0:
                        replacement = new StabbingSword();
                        break;
                    case 1:
                        replacement = new GiantSword();
                        break;
                    case 2:
                        replacement = new Spear();
                        break;
                    case 3:
                        replacement = new IceAndFire();
                        break;
                    default:
                        replacement = new Wand(random - 4);
                        break;
                }
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
