package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class SlimeInAJarAction extends AbstractGameAction {

    private static final AbstractCard[] WORKER_CARDS = new AbstractCard[]{
            new Keel(),
            new Erwen(),
            new Crab(),
            new Gaoshi(),
            new Leidongxuan(),
            new Prisoner(),
            new Performer(),
            new Rabble(),
    };

    private boolean retrieveCard;

    public SlimeInAJarAction(int amount) {
        this.retrieveCard = false;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(this.generateCardChoices(), CardRewardScreen.TEXT[1], false);
            this.tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                final AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                    disCard.upgrade();
                }
                disCard.current_x = -1000.0f * Settings.xScale;
                int handAmount = Math.min(this.amount, 10 - AbstractDungeon.player.hand.size());
                int discardAmount = this.amount - handAmount;
                for (int i = 0; i < handAmount; i++) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard.makeStatEquivalentCopy(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                }
                for (int i = 0; i < discardAmount; i++) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard.makeStatEquivalentCopy()));
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        this.tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices() {
        ArrayList<AbstractCard> choices = new ArrayList<AbstractCard>();
        choices.add(WORKER_CARDS[7]);
        choices.add(WORKER_CARDS[6]);
        choices.add(WORKER_CARDS[AbstractDungeon.cardRandomRng.random(0, 5)]);
        return choices;
    }
}
