package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class EldenWaitAction extends AbstractGameAction
{
    private boolean freeToPlayOnce;
    private int energyOnUse;

    public EldenWaitAction(int amount, boolean freeToPlayOnce, int energyOnUse) {
        this.amount = amount;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
            AbstractDungeon.player.getRelic("Chemical X").flash();
        }
        if (effect > 0) {
            for (int i = 0; i < effect; ++i) {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!mo.isDeadOrEscaped() && mo.getIntentBaseDmg() > 0) {
                        this.addToBot(new GainBlockAction(AbstractDungeon.player, this.amount));
                    }
                }
            }
            if (!this.freeToPlayOnce) {
                AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}
