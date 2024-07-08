package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class SpaceShipAction extends AbstractGameAction {

    private int damage;

    private boolean upgraded;

    public SpaceShipAction(int damage, boolean upgraded) {
        this.duration = 0.0f;
        this.actionType = ActionType.WAIT;
        this.damage = damage;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        this.amount = AbstractDungeon.player.discardPile.size();
        this.addToTop(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(this.damage * this.amount), DamageInfo.DamageType.NORMAL, AttackEffect.NONE));
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                this.addToTop(new VFXAction(new WeightyImpactEffect(mo.hb.cX, mo.hb.cY)));
            }
        }
        if (upgraded) {
            this.addToTop(new LayoffAction(this.amount, true, c -> true));
        } else {
            this.addToTop(new ExhaustCardGroupAction(AbstractDungeon.player.discardPile));
        }
        this.isDone = true;
    }
}