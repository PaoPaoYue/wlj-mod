package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.NightmareAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class BrothelAction extends AbstractGameAction {
    public static final String[] TEXT;
    private static final UIStrings uiStrings;
    private static final float DURATION;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("CopyAction");
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }

    public BrothelAction(int amount) {
        this.target = AbstractDungeon.player;
        this.source = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if (this.duration != DURATION) {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                final AbstractCard tmpCard = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
                this.addToTop(new MakeTempCardInHandAction(tmpCard.makeStatEquivalentCopy(), this.amount));
                AbstractDungeon.player.hand.addToHand(tmpCard);
                AbstractDungeon.handCardSelectScreen.selectedCards.clear();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
            this.tickDuration();
            return;
        }
        if (AbstractDungeon.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }
        if (AbstractDungeon.player.hand.size() == 1) {
            this.addToTop(new MakeTempCardInHandAction(AbstractDungeon.player.hand.getBottomCard().makeStatEquivalentCopy(), this.amount));
            this.isDone = true;
            return;
        }
        AbstractDungeon.handCardSelectScreen.open(NightmareAction.TEXT[0], 1, false, false);
        this.tickDuration();
    }
}
