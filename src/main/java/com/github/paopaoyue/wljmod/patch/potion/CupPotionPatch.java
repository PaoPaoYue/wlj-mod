package com.github.paopaoyue.wljmod.patch.potion;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.power.CupPower;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = PoisonLoseHpAction.class,
        method = "update"
)
public class CupPotionPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(PoisonLoseHpAction __instance) {
        AbstractPower power = __instance.target.getPower(CupPower.POWER_ID);
        if (power != null) {
            ((CupPower) power).onPoisonDeath();
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "isDying");
            return Inject.insertAfter(LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher), 1);
        }
    }
}
