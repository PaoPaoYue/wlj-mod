package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

public class EggAction extends AbstractGameAction {

    private boolean animated = false;

    public EggAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.duration = DEFAULT_DURATION;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (duration == DEFAULT_DURATION) {
            for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo.isDeadOrEscaped()) continue;
                this.addToTop(new ApplyPowerAction(mo, AbstractDungeon.player, new WeakPower(mo, amount, false), amount));
                AbstractPower artifactPower = mo.getPower(ArtifactPower.POWER_ID);
                if (artifactPower != null) {
                    this.addToTop(new RemoveSpecificPowerAction(mo, AbstractDungeon.player, artifactPower));
                }
            }
        }
        if (duration <= 0.4F && !animated) {
            AbstractDungeon.effectList.add(new IntenseZoomEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, false));
            animated = true;
        }
        this.tickDuration();
    }
}

