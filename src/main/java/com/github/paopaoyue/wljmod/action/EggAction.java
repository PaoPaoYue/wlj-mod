package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.power.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;

public class EggAction extends AbstractGameAction {

    private int times;
    private boolean animated = false;

    private static final String[] DEBUFF_LIST = {
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            PoisonPower.POWER_ID,
            StrengthPower.POWER_ID,
            StrengthPower.POWER_ID,
            WeakPower.POWER_ID,
            WeakPower.POWER_ID,
            WeakPower.POWER_ID,
            WeakPower.POWER_ID,
            WeakPower.POWER_ID,
            VulnerablePower.POWER_ID,
            VulnerablePower.POWER_ID,
            VulnerablePower.POWER_ID,
            VulnerablePower.POWER_ID,
            VulnerablePower.POWER_ID,
            BlockReturnPower.POWER_ID,
            BlockReturnPower.POWER_ID,
            CorpseExplosionPower.POWER_ID,
            ConstrictedPower.POWER_ID,
            ConstrictedPower.POWER_ID,
            ConstrictedPower.POWER_ID,
            ChokePower.POWER_ID,
            ChokePower.POWER_ID,
            ChokePower.POWER_ID,
            LockOnPower.POWER_ID,
            MarkPower.POWER_ID,
            BrotherhoodPower.POWER_ID,
            BrotherhoodPower.POWER_ID,
            BrotherhoodPower.POWER_ID,
            LovePower.POWER_ID,
            LovePower.POWER_ID,
            LovePower.POWER_ID,
            BaitPower.POWER_ID,
            BaitPower.POWER_ID,
            BaitPower.POWER_ID,
            BaitPower.POWER_ID,
            GiantKingPower.POWER_ID,
            GiantKingPower.POWER_ID,
            CupPower.POWER_ID,
    };

    public EggAction(int amount, int times) {
        this.setValues(null, AbstractDungeon.player, amount);
        this.duration = DEFAULT_DURATION;
        this.actionType = ActionType.DEBUFF;
        this.times = times;
    }

    @Override
    public void update() {
        if (duration == DEFAULT_DURATION) {
            String[] powers = new String[this.amount];
            for (int i = 0; i < this.amount; i++) {
                powers[i] = DEBUFF_LIST[AbstractDungeon.cardRandomRng.random(DEBUFF_LIST.length - 1)];
            }
            for (int i = 0; i < times; i++) {
                for (String powerId : powers) {
                    for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (mo.isDeadOrEscaped())
                            continue;
                        AbstractPower p = this.constructPower(powerId, mo);
                        System.out.println("Applying power: " + p.ID + " to " + mo.name + " with amount: " + p.amount);
                        this.addToBot(new ApplyPowerAction(
                                mo, AbstractDungeon.player, p,p.amount, true
                        ));
                    }
                }
            }
        }
        if (duration <= 0.4F && !animated) {
            AbstractDungeon.effectList.add(new IntenseZoomEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, false));
            animated = true;
        }
        this.tickDuration();
    }

    private AbstractPower constructPower(String powerId, AbstractMonster target) {
        switch (powerId) {
            case PoisonPower.POWER_ID:
                return new PoisonPower(target, AbstractDungeon.player, 1);
            case StrengthPower.POWER_ID:
                return new StrengthPower(target, -1);
            case WeakPower.POWER_ID:
                return new WeakPower(target, 1, false);
            case VulnerablePower.POWER_ID:
                return new VulnerablePower(target, 1, false);
            case BlockReturnPower.POWER_ID:
                return new BlockReturnPower(target, 1);
            case CorpseExplosionPower.POWER_ID:
                return new CorpseExplosionPower(target);
            case ConstrictedPower.POWER_ID:
                return new ConstrictedPower(target, AbstractDungeon.player, 1);
            case ChokePower.POWER_ID:
                return new ChokePower(target, 1);
            case LockOnPower.POWER_ID:
                return new LockOnPower(target, 1);
            case MarkPower.POWER_ID:
                return new MarkPower(target, 1);
            case BrotherhoodPower.POWER_ID:
                return new BrotherhoodPower(target, 1);
            case LovePower.POWER_ID:
                return new LovePower(target, 1);
            case BaitPower.POWER_ID:
                return new BaitPower(target, 1);
            case GiantKingPower.POWER_ID:
                return new GiantKingPower(target, 1);
            case CupPower.POWER_ID:
                return new CupPower(target, 1);
            default:
                throw new IllegalArgumentException("Unknown power ID: " + powerId);
        }
    }
}

