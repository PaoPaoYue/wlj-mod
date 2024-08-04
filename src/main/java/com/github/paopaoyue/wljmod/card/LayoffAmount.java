package com.github.paopaoyue.wljmod.card;

import basemod.abstracts.DynamicVariable;
import com.github.paopaoyue.wljmod.power.AlarmPower;
import com.github.paopaoyue.wljmod.relic.Truck;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class LayoffAmount extends DynamicVariable {

    public static int calculateLayoffAmount(int baseLayoffAmount) {
        int layoffAmount = baseLayoffAmount;
        AlarmPower alarmPower = (AlarmPower) AbstractDungeon.player.getPower(AlarmPower.POWER_ID);
        layoffAmount = (alarmPower == null ? layoffAmount : alarmPower.modifyLayoffAmount(layoffAmount));
        Truck truck = (Truck) AbstractDungeon.player.getRelic(Truck.ID);
        layoffAmount = (truck == null ? layoffAmount : truck.modifyLayoffAmount(layoffAmount));
        return layoffAmount;
    }

    public LayoffAmount() {
    }

    @Override
    public String key() {
        return "L";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractLayoffCard) {
            return ((AbstractLayoffCard) card).isLayoffAmountModified();
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractLayoffCard) {
            ((AbstractLayoffCard) card).setLayoffAmountModified(v);
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractLayoffCard) {
            return ((AbstractLayoffCard) card).getLayoffAmount();
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractLayoffCard) {
            return ((AbstractLayoffCard) card).getBaseLayoffAmount();
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractLayoffCard) {
            return ((AbstractLayoffCard) card).isLayoffAmountUpgraded();
        }
        return false;
    }
}
