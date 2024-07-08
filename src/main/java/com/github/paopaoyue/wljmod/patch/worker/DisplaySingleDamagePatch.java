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
public class DisplaySingleDamagePatch {

    @SpireInsertPatch(
            locator = SingleDamageLocator.class,
            localvars = {"tmp"}
    )
    public static void Insert(AbstractCard __instance, @ByRef float[] tmp) {
        WljMod.workerManager.iterOutsideDiscardPile(c -> {
            if (c instanceof Performer) {
                tmp[0] = ((Performer) c).atDamageGiveOutsideDiscardPile(tmp[0], __instance.damageTypeForTurn, __instance);
            }
        });
    }

    private static class SingleDamageLocator extends SpireInsertLocator {
        private SingleDamageLocator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "stance");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }

}
