package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExhaustPileBottomToHandAction extends AbstractGameAction {

    private static final Logger logger = LogManager.getLogger(Reflect.class.getName());

    private static final float PADDING;
    private boolean isOtherCardInCenter;

    public ExhaustPileBottomToHandAction(int amount) {
        this.isOtherCardInCenter = true;
        this.amount = Math.min(amount, AbstractDungeon.player.exhaustPile.size());
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public ExhaustPileBottomToHandAction(int amount, boolean isOtherCardInCenter) {
        this(amount);
        this.isOtherCardInCenter = isOtherCardInCenter;
    }

    public void update() {
        if (this.amount == 0 || AbstractDungeon.player.exhaustPile.size() < amount) {
            this.isDone = true;
        } else {
            Hitbox hb = Reflect.getPrivate(ExhaustPanel.class, AbstractDungeon.overlayMenu.exhaustPanel, "hb", Hitbox.class);
            if (hb == null) {
                logger.error("ExhaustPanel hitbox is null");
                this.isDone = true;
                return;
            }
            for (int i = 0; i < amount; i++) {
                AbstractCard c = AbstractDungeon.player.exhaustPile.getBottomCard();
                c.current_x = hb.cX;
                c.current_y = hb.cY;
                c.drawScale = 0.01F;
                c.targetDrawScale = 0.75F;
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.player.hand.addToHand(c);
                    AbstractDungeon.player.exhaustPile.removeCard(c);
                    if (AbstractDungeon.player.hasPower("Corruption") && c.type == AbstractCard.CardType.SKILL) {
                        c.setCostForTurn(-9);
                    }
                }
                c.unfadeOut();
                c.lighten(false);
                c.unhover();
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            AbstractDungeon.player.onCardDrawOrDiscard();

            this.isDone = true;
        }
    }

    static {
        PADDING = 25.0F * Settings.scale;
    }
}
