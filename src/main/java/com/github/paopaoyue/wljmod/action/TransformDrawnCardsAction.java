package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransformDrawnCardsAction extends AbstractGameAction {
    private boolean retrieveCard;
    private AbstractCard targetCard;
    private ArrayList<AbstractCard> choices;
    private Map<AbstractCard, AbstractCard> transformMap;
    private boolean skippable;
    private boolean random;

    public TransformDrawnCardsAction(AbstractCard targetCard, boolean skippable, boolean random) {
        this.retrieveCard = false;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.targetCard = targetCard;
        this.skippable = skippable;
        this.random = random;

        this.choices = new ArrayList<>();
        this.transformMap = new HashMap<>();
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            for (AbstractCard card : DrawCardAction.drawnCards) {
                AbstractCard tempCard = card.makeStatEquivalentCopy();
                this.choices.add(tempCard);
                this.transformMap.put(tempCard, card);
            }
            if (this.choices.isEmpty()) {
                this.isDone = true;
            } else if (this.choices.size() == 1) {
                AbstractCard disCard = this.choices.get(0);
                int index = AbstractDungeon.player.hand.group.indexOf(transformMap.get(disCard));
                this.addToTop(new TransformCardInHandAction(index, this.targetCard.makeStatEquivalentCopy()));
                this.isDone = true;
            } else if (this.random) {
                AbstractCard disCard = this.choices.get(AbstractDungeon.cardRandomRng.random(this.choices.size() - 1));
                int index = AbstractDungeon.player.hand.group.indexOf(transformMap.get(disCard));
                this.addToTop(new TransformCardInHandAction(index, this.targetCard.makeStatEquivalentCopy()));
                this.isDone = true;
            } else {
                AbstractDungeon.cardRewardScreen.customCombatOpen(this.choices, TransformDiscardedWorkerAction.TEXT[0], this.skippable);
                this.tickDuration();
            }
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard;
                int index = AbstractDungeon.player.hand.group.indexOf(transformMap.get(disCard));
                this.addToTop(new TransformCardInHandAction(index, this.targetCard.makeStatEquivalentCopy()));
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        this.tickDuration();
    }

}
