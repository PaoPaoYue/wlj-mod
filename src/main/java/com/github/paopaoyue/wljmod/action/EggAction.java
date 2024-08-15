package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

public class EggAction extends AbstractGameAction {

    private boolean animated = false;

    public EggAction(int energyGain) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, energyGain);
        this.duration = DEFAULT_DURATION;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (duration == DEFAULT_DURATION) {
            boolean gainEnergy = false;
            for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo.isDeadOrEscaped()) continue;
                AbstractPower artifactPower = mo.getPower(ArtifactPower.POWER_ID);
                if (artifactPower != null) {
                    this.addToTop(new ReducePowerAction(mo, mo, ArtifactPower.POWER_ID, artifactPower.amount));
                } else {
                    gainEnergy = true;
                }
            }
            if (gainEnergy) {
                this.addToTop(new GainEnergyAction(amount));
            }
        }
        if (duration <= 0.4F && !animated) {
            AbstractDungeon.effectList.add(new IntenseZoomEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, false));
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo.isDeadOrEscaped()) continue;
                mo.useFastShakeAnimation(0.5F);
            }
            animated = true;
        }
        this.tickDuration();
    }
}

