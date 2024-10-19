package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.Wand;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class QuinAction extends AbstractGameAction
{

    public QuinAction() {
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.getCurrentEnergy();
        this.addToTop(new BetterMakeTempCardInHandAction(new Wand(Math.max(0, effect))));
        this.isDone = true;
    }
}