package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExhaustDiscardedWorkerAction extends AbstractGameAction {

    public static final String[] TEXT;

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("Wlj:ExhaustDiscardedWorkerAction").TEXT;
    }

    public ExhaustDiscardedWorkerAction(int amount) {
        this.actionType = ActionType.EXHAUST;
        final float action_DUR_FAST = Settings.ACTION_DUR_FAST;
        this.startDuration = action_DUR_FAST;
        this.duration = action_DUR_FAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.duration != this.startDuration) {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

                for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractDungeon.player.discardPile.moveToExhaustPile(c);
                }

                for (final AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    c.unhover();
                    c.target_x = (float) CardGroup.DISCARD_PILE_X;
                    c.target_y = 0.0f;
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }


            this.tickDuration();
            if (this.isDone) {
                for (final AbstractCard c : AbstractDungeon.player.hand.group) {
                    c.applyPowers();
                }
            }
            return;
        }

        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractWorkerCard) {
                tmpGroup.group.add(c);
            }
        }
        this.amount = Math.min(this.amount, tmpGroup.size());
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        tmpGroup.group.sort((a, b) -> {
            int priorityA = (a instanceof AbstractWorkerCard)? WljMod.workerManager.getWorkerPriority((AbstractWorkerCard) a) : 0;
            int priorityB = (b instanceof AbstractWorkerCard)? WljMod.workerManager.getWorkerPriority((AbstractWorkerCard) b) : 0;
            return priorityA - priorityB;
        });
        if (this.amount == 1) {
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, TEXT[0], false);
        } else {
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, TEXT[1] + this.amount + TEXT[2], false);
        }
        this.tickDuration();
    }

}

