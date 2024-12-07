package com.github.paopaoyue.wljmod.patch.gold;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.power.GiantKingPower;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.stream.Collectors;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "loseGold"
)
public class LoseGoldPatch {

    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(AbstractPlayer __instance, int goldAmount) {
        if (AbstractDungeon.getCurrRoom().monsters != null) {
            List<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                    .filter(monster -> !monster.isDeadOrEscaped() && monster.hasPower(GiantKingPower.POWER_ID))
                    .collect(Collectors.toList());
            if (!monsters.isEmpty()) {
                __instance.gold += goldAmount;
                int goldToBorrowFromEach = Math.max(1, goldAmount / monsters.size());
                for (AbstractMonster monster: monsters) {
                    int goldToBorrow = goldAmount <= 0 ? 0: goldToBorrowFromEach;
                    goldAmount -= goldToBorrow;
                    monster.gold += goldToBorrow;
                    AbstractPower power = monster.getPower(GiantKingPower.POWER_ID);
                    DamageAction damageAction = new DamageAction(monster, new DamageInfo(__instance, power.amount, DamageInfo.DamageType.THORNS), goldToBorrow);
                    damageAction.attackEffect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
                    Reflect.setPrivate(DamageAction.class, damageAction, "skipWait", true);
                    AbstractDungeon.actionManager.addToTop(damageAction);
                }
                return SpireReturn.Continue();
            }
        }

        if (WljMod.tempGold > 0) {
            int goldToBorrow = Math.min(goldAmount, WljMod.tempGold);
            WljMod.tempGold -= goldToBorrow;
            __instance.gold += goldToBorrow;
        }
        return SpireReturn.Continue();
    }

}