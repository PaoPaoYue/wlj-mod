package com.github.paopaoyue.wljmod.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AfeiAction extends AbstractGameAction {
    private static final float DURATION = 0.0F;
    private DamageInfo info;
    private AbstractCard card;

    public AfeiAction(AbstractCreature target, AbstractCreature source, AbstractCard card) {
        this.setValues(target, source);
        this.actionType = ActionType.DAMAGE;
        this.duration = DURATION;
        this.card = card;
    }

    @Override
    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
            return;
        }
        if (this.card != null) {
            if (this.target == null || this.target.isDeadOrEscaped()) {
                this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            }
            this.card.calculateCardDamage((AbstractMonster) this.target);
            AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_HEAVY));
        }
        this.tickDuration();
    }
}
