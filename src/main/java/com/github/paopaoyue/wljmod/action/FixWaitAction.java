package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FixWaitAction extends AbstractGameAction {
    
    public FixWaitAction(float setDur) {
        this.setValues((AbstractCreature) null, (AbstractCreature) null, 0);
        this.duration = setDur;
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        this.tickDuration();
    }
}
