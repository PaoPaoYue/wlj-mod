package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.LayoffAmount;
import com.github.paopaoyue.wljmod.component.SunKnight;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class LapseAction extends AbstractGameAction {

    private static final int LAYOFF_AMOUNT = 3;

    public static AbstractCard lastLapseCard;

    private final AbstractCard card;

    public LapseAction(AbstractCard card) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.amount = LAYOFF_AMOUNT;
        this.card = card;
    }

    public static void reset() {
        lastLapseCard = null;
    }

    @Override
    public void update() {
        if (lastLapseCard != null && !lastLapseCard.name.equals(card.name)) {
            if (WljMod.avatarManager.getCurrentAvatar() instanceof SunKnight) {
                AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
            }
            this.addToTop(new LayoffAction(LayoffAmount.calculateLayoffAmount(amount)));
        }
        lastLapseCard = card;
        this.isDone = true;
    }
}