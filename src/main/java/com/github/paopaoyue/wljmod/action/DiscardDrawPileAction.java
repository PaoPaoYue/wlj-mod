package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscardDrawPileAction extends AbstractGameAction {

    public DiscardDrawPileAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            int size = AbstractDungeon.player.drawPile.size();
            for (int i = 0; i < size; ++i) {
                AbstractCard c = AbstractDungeon.player.drawPile.getTopCard();
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
            }
        }
        this.tickDuration();
    }
}
