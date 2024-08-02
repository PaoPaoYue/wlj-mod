package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SunKnightAction extends AbstractGameAction {

    private final boolean upgraded;

    public SunKnightAction(boolean upgraded) {
        this.duration = 0.0f;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        int i = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof AbstractWorkerCard) {
                AbstractCard replacement = getRandomCard();
                if (card.upgraded) {
                    replacement.upgrade();
                }
                this.addToTop(new TransformCardInHandAction(i, replacement));
            }
            i++;
        }
        this.isDone = true;
    }

    private AbstractCard getRandomCard() {
        int random = AbstractDungeon.cardRandomRng.random(0, 7);
        if (upgraded) {
            switch (random) {
                case 0:
                    return new StabbingSword();
                case 1:
                case 2:
                    return new Spear();
                case 3:
                    return new IceAndFire();
                case 4:
                    return new Wand(0);
                case 5:
                case 6:
                    return new Wand(1);
                case 7:
                    return new Wand(2);
                default:
                    return new Wand(3);
            }
        } else {
            switch (random) {
                case 0:
                    return new StabbingSword();
                case 1:
                    return new Spear();
                case 2:
                    return new IceAndFire();
                case 3:
                    return new GiantSword();
                case 4:
                    return new Wand(0);
                case 5:
                    return new Wand(1);
                case 6:
                    return new Wand(2);
                case 7:
                    return new Wand(3);
                default:
                    return new Wand(4);
            }
        }
    }
}
