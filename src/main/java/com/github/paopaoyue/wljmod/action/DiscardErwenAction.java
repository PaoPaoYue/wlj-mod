package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.Erwen;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.stream.Collectors;

public class DiscardErwenAction extends AbstractGameAction {

    public DiscardErwenAction() {
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
            for (AbstractCard c : AbstractDungeon.player.drawPile.group.stream().filter(c -> c instanceof Erwen).collect(Collectors.toList())) {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
            }
        }
        this.tickDuration();
    }
}
