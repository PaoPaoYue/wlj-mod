package com.github.paopaoyue.wljmod.action;

import com.github.paopaoyue.wljmod.effect.GoldTextOnPlayerEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;

public class DiceAction extends AbstractGameAction {
    private int damage;
    private int diceThreshold;
    private int gold;
    private int energyOnUse;
    private boolean freeToPlayOnce;

    public DiceAction(AbstractMonster target, int damage, int diceThreshold, int gold, int energyOnUse, boolean freeToPlayOnce) {
        this.source = AbstractDungeon.player;
        this.target = target;
        this.damage = damage;
        this.diceThreshold = diceThreshold;
        this.gold = gold;
        this.energyOnUse = energyOnUse;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        if (gold > 0) {
            AbstractGameAction rollDiceAction = new RollDiceAction();
            if (rollDiceAction.amount >= diceThreshold) {
                AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(gold, true));
                AbstractDungeon.effectList.add(new RainingGoldEffect(4 * gold, true));
                AbstractDungeon.player.gainGold(gold);
            } else {
                PurchaseAction.sfxUtil.playSFX();
                AbstractDungeon.effectList.add(new GoldTextOnPlayerEffect(-gold, true));
                AbstractDungeon.player.loseGold(gold);
            }
            this.addToTop(rollDiceAction);
        }
        if (damage > 0 && !target.isDeadOrEscaped())
            this.addToTop(new DamageAction(target, new DamageInfo(source, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (!freeToPlayOnce) {
            AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
        }
        this.isDone = true;
    }
}

