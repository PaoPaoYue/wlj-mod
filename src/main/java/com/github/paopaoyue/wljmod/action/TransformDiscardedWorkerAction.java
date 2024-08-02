package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.sfx.SfxUtil;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class TransformDiscardedWorkerAction extends AbstractGameAction {

    public static final String[] TEXT;

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("Wlj:TransformDiscardedWorkerAction").TEXT;
    }

    public static SfxUtil sfxUtil = SfxUtil.createInstance(new String[]{"Wlj:SIT_1", "Wlj:SIT_2", "Wlj:SIT_3", "Wlj:SIT_4"}, true, 1.0f, 0.0f, 1.0f);


    private AbstractCard targetCard;
    private boolean anyNumber;
    private int handAmount;
    private int discardAmount;

    public TransformDiscardedWorkerAction(int amount, AbstractCard targetCard, boolean anyNumber) {
        this.actionType = ActionType.CARD_MANIPULATION;
        final float action_DUR_FAST = Settings.ACTION_DUR_FAST;
        this.startDuration = action_DUR_FAST;
        this.duration = action_DUR_FAST;
        this.amount = amount;
        this.targetCard = targetCard;
        this.anyNumber = anyNumber;
    }

    @Override
    public void update() {
        if (this.duration != this.startDuration) {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {

                sfxUtil.playSFX(1.6f);

                for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    AbstractDungeon.player.discardPile.removeCard(c);
                    AbstractCard tempCard = targetCard.makeStatEquivalentCopy();
                    if (c.upgraded) {
                        tempCard.upgrade();
                    }
                    tempCard.current_x = c.current_x;
                    tempCard.current_y = c.current_y;
                    if (handAmount-- > 0) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(tempCard, c.current_x, c.current_y));
                    } else {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(tempCard, c.current_x, c.current_y));
                    }
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
        discardAmount = 0;
        handAmount = this.amount;
        if (this.amount + AbstractDungeon.player.hand.size() > 10) {
            AbstractDungeon.player.createHandIsFullDialog();
            discardAmount = this.amount + AbstractDungeon.player.hand.size() - 10;
            handAmount -= discardAmount;
        }
        tmpGroup.group.sort((a, b) -> {
            int priorityA = (a instanceof AbstractWorkerCard) ? WljMod.workerManager.getWorkerPriority((AbstractWorkerCard) a) : 0;
            int priorityB = (b instanceof AbstractWorkerCard) ? WljMod.workerManager.getWorkerPriority((AbstractWorkerCard) b) : 0;
            return priorityA - priorityB;
        });
        if (this.amount == 1) {
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, anyNumber, TEXT[0]);
        } else {
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, anyNumber, TEXT[1] + this.amount + TEXT[2]);
        }
        this.tickDuration();
    }

}

