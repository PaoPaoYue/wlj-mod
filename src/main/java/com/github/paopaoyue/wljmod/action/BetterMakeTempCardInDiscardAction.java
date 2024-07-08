package com.github.paopaoyue.wljmod.action;

import com.badlogic.gdx.Gdx;
import com.github.paopaoyue.wljmod.effect.BetterShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

public class BetterMakeTempCardInDiscardAction extends AbstractGameAction {
    private AbstractCard c;
    private int numCards;
    private boolean isOtherCardInCenter;
    private boolean hideEffect;

    public BetterMakeTempCardInDiscardAction(AbstractCard card, int amount) {
        this(card, amount, false, false);
    }

    public BetterMakeTempCardInDiscardAction(AbstractCard card, int amount, boolean isOtherCardInCenter, boolean hideEffect) {
        UnlockTracker.markCardAsSeen(card.cardID);
        this.numCards = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startDuration = Settings.FAST_MODE ? Settings.ACTION_DUR_FAST : 0.5F;
        this.duration = this.startDuration;
        this.isOtherCardInCenter = isOtherCardInCenter;
        this.hideEffect = hideEffect;
        this.c = card;
        if (this.c.type != AbstractCard.CardType.CURSE && this.c.type != AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
            this.c.upgrade();
        }
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.numCards == 1 && this.isOtherCardInCenter) {
                AbstractDungeon.effectList.add(new BetterShowCardAndAddToDiscardEffect(this.makeNewCard(), true));
            } else {
                for (int i = 0; i < this.numCards; ++i) {
                    if (i < 6 && !this.hideEffect)
                        AbstractDungeon.effectList.add(new BetterShowCardAndAddToDiscardEffect(this.makeNewCard()));
                    else
                        AbstractDungeon.player.discardPile.addToTop(this.makeNewCard());
                }
            }

            this.duration -= Gdx.graphics.getDeltaTime();
        }

        this.tickDuration();
    }

    private AbstractCard makeNewCard() {
        return this.c.makeStatEquivalentCopy();
    }
}
