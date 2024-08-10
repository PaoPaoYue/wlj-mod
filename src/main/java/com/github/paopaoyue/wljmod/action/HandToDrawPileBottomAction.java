package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.ForethoughtAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class HandToDrawPileBottomAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private boolean anyNumber;

    public HandToDrawPileBottomAction(final boolean anyNumber) {
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.anyNumber = anyNumber;
    }

    @Override
    public void update() {
        if (this.duration != Settings.ACTION_DUR_FAST) {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                for (final AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.p.hand.moveToBottomOfDeck(c);
                }
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
            this.tickDuration();
            return;
        }
        if (this.p.hand.isEmpty()) {
            this.isDone = true;
            return;
        }
        if (this.p.hand.size() == 1 && !this.anyNumber) {
            final AbstractCard c2 = this.p.hand.getTopCard();
            this.p.hand.moveToBottomOfDeck(c2);
            AbstractDungeon.player.hand.refreshHandLayout();
            this.isDone = true;
            return;
        }
        if (!this.anyNumber) {
            AbstractDungeon.handCardSelectScreen.open(ForethoughtAction.TEXT[0], 1, false);
        } else {
            AbstractDungeon.handCardSelectScreen.open(ForethoughtAction.TEXT[0], 99, true, true);
        }
        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ForethoughtAction");
        TEXT = uiStrings.TEXT;
    }
}
