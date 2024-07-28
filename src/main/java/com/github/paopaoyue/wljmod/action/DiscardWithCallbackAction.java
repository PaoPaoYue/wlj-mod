package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DiscardWithCallbackAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float DURATION;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
        TEXT = uiStrings.TEXT;
        DURATION = Settings.ACTION_DUR_XFAST;
    }

    private AbstractPlayer p;
    private boolean isRandom;
    private boolean endTurn;
    private Consumer<List<AbstractCard>> callback;

    public DiscardWithCallbackAction(AbstractCreature target, AbstractCreature source, int amount,
                                     boolean isRandom, Consumer<List<AbstractCard>> callback) {
        this(target, source, amount, isRandom, false, callback);
    }

    public DiscardWithCallbackAction(AbstractCreature target, AbstractCreature source, int amount,
                                     boolean isRandom, boolean endTurn, Consumer<List<AbstractCard>> callback) {
        this.p = (AbstractPlayer) target;
        this.isRandom = isRandom;
        this.setValues(target, source, amount);
        this.actionType = ActionType.DISCARD;
        this.endTurn = endTurn;
        this.duration = DURATION;
        this.callback = callback;
    }

    @Override
    public void update() {
        if (this.duration == DURATION) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }

            List<AbstractCard> discardedCards = new ArrayList<>();

            if (this.p.hand.size() <= this.amount) {
                this.amount = this.p.hand.size();
                for (int tmp = this.p.hand.size(), i = 0; i < tmp; ++i) {
                    final AbstractCard c = this.p.hand.getTopCard();
                    discardedCards.add(c);
                    this.p.hand.moveToDiscardPile(c);
                    if (!this.endTurn) {
                        c.triggerOnManualDiscard();
                    }
                    GameActionManager.incrementDiscard(this.endTurn);
                }
                this.callback.accept(discardedCards);
                AbstractDungeon.player.hand.applyPowers();
                this.tickDuration();
                return;
            }
            if (this.isRandom) {
                for (int j = 0; j < this.amount; ++j) {
                    final AbstractCard c = this.p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                    discardedCards.add(c);
                    this.p.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(this.endTurn);
                }
                this.callback.accept(discardedCards);
            } else {
                if (this.amount < 0) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
                    AbstractDungeon.player.hand.applyPowers();
                    this.tickDuration();
                    return;
                }
                DiscardAction.numDiscarded = this.amount;
                if (this.p.hand.size() > this.amount) {
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false);
                }
                AbstractDungeon.player.hand.applyPowers();
                this.tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {

            callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);

            for (final AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.p.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(this.endTurn);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }

}

