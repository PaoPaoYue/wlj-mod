package com.github.paopaoyue.wljmod.patch.relic;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.relic.Ship;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = "update"
)
public class ApplyPowerActionPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(ApplyPowerAction __instance) {
        if (AbstractDungeon.player.hasRelic(Ship.ID) && __instance.source != null && __instance.source.isPlayer && __instance.target != __instance.source && !__instance.target.hasPower("Artifact")) {
            AbstractPower powerToApply = Reflect.getPrivate(ApplyPowerAction.class, __instance, "powerToApply", AbstractPower.class);
            if (powerToApply != null && powerToApply.amount > 0 && powerToApply.type == AbstractPower.PowerType.DEBUFF && __instance.target.powers.stream().anyMatch(p -> !p.ID.equals(powerToApply.ID) && p.type == AbstractPower.PowerType.DEBUFF)) {
                powerToApply.amount++;
                __instance.amount++;
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "hasPower");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}

