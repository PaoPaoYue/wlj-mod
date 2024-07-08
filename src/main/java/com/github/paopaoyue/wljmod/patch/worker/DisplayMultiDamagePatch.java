package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.Performer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractCard.class,
        method = "applyPowers"
)
public class DisplayMultiDamagePatch {

    @SpireInsertPatch(
            locator = MultiDamageLocator.class,
            localvars = {"tmp", "i"}
    )
    public static void Insert(AbstractCard __instance, float[] tmp, int i) {
        WljMod.workerManager.iterOutsideDiscardPile(c -> {
            if (c instanceof Performer) {
                tmp[i] = ((Performer) c).atDamageGiveOutsideDiscardPile(tmp[i], __instance.damageTypeForTurn, __instance);
            }
        });
    }

    private static class MultiDamageLocator extends SpireInsertLocator {
        private MultiDamageLocator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "stance");
            int[] retVal = LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            return new int[]{retVal[retVal.length - 1]};
        }
    }

}
