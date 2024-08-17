package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.Collections;

public class PutAtExhaustPileBottomAction extends AbstractGameAction {

    public static final String[] TEXT;

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("Wlj:PutAtExhaustPileBottomAction").TEXT;
    }

    private CardGroup group;

    public PutAtExhaustPileBottomAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        final float action_DUR_FAST = Settings.ACTION_DUR_FAST;
        this.startDuration = action_DUR_FAST;
        this.duration = action_DUR_FAST;
        this.group = AbstractDungeon.player.drawPile;
    }

    @Override
    public void update() {
        if (this.duration != this.startDuration) {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Collections.reverse(AbstractDungeon.gridSelectScreen.selectedCards);
                for (final AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {

                    if (AbstractDungeon.player.hoveredCard == c) {
                        AbstractDungeon.player.releaseCard();
                    }

                    AbstractDungeon.actionManager.removeFromQueue(c);
                    c.unhover();
                    c.untip();
                    c.stopGlowing();

                    group.removeCard(c);

                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                    AbstractDungeon.player.exhaustPile.addToBottom(c);
                    AbstractDungeon.player.onCardDrawOrDiscard();
                }

                for (final AbstractCard c : group.group) {
                    c.unhover();
                    c.target_x = (float) CardGroup.DRAW_PILE_X;
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

        this.amount = Math.min(this.amount, group.size());
        if (this.amount <= 0) {
            this.isDone = true;
            return;
        }
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        tmpGroup.group.addAll(group.group.subList(group.size() - amount, group.size()));
        if (this.amount == 1) {
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
        } else {
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[1] + this.amount + TEXT[2]);
        }
        this.tickDuration();
    }

}

