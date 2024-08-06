package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ZhaoritianPatch {

    @SpirePatch(
            clz = IntangiblePlayerPower.class,
            method = SpirePatch.CLASS
    )
    public static class IsAppliedAtMonsterTurnField {
        public static SpireField<Boolean> value = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = IntangiblePlayerPower.class,
            method = "atEndOfRound"
    )
    public static class IntangiblePlayerPowerPatch {

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(IntangiblePlayerPower __instance) {
            if (IsAppliedAtMonsterTurnField.value.get(__instance)) {
                IsAppliedAtMonsterTurnField.value.set(__instance, false);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class IntangiblePlayerPowerStackPatch {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"p"}
        )
        public static void Insert(ApplyPowerAction __instance, AbstractPower ___powerToApply, AbstractPower p) {
            if (p instanceof IntangiblePlayerPower && IsAppliedAtMonsterTurnField.value.get(___powerToApply)) {
                IsAppliedAtMonsterTurnField.value.set(p, true);
            }
        }

        private static class Locator extends SpireInsertLocator {

            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "stackPower");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }

}
