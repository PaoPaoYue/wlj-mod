package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.card.Dice;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

public class DiceAction extends AbstractGameAction
{
    private Dice card;

    public DiceAction(Dice card,  AbstractMonster target) {
        this.source = AbstractDungeon.player;
        this.card = card;
        this.target = target;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (card.energyOnUse != -1) {
            effect = card.energyOnUse;
        }
        if (AbstractDungeon.player.hasRelic("Chemical X")) {
            effect += 2;
            AbstractDungeon.player.getRelic("Chemical X").flash();
        }
        if (effect > 0) {
            card.baseDamage = effect * 9;
            card.calculateCardDamage((AbstractMonster) target);
            AbstractGameAction rollDiceAction = new RollDiceAction();
            if (rollDiceAction.amount >= card.magicNumber) {
                this.addToTop(new GainGoldAction(5 * effect));
                this.addToTop(new VFXAction((new RainingGoldEffect(5 * effect, true))));
            }
            this.addToTop(rollDiceAction);
            if (!target.isDeadOrEscaped())
                this.addToTop(new DamageAction(target, new DamageInfo(source, card.damage, card.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            if (!card.freeToPlayOnce) {
                AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
            }
        }
        this.isDone = true;
    }
}

