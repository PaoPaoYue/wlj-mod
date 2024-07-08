package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.Leidongxuan;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class DecrementBlockPatch {

    @SpireInsertPatch(
            locator = BeforeDecrementLocator.class,
            localvars = {"damageAmount"}
    )
    public static void Insert(AbstractCreature __instance, DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.HP_LOSS && __instance.currentBlock > 0) {
            int blockedDamage = Math.min(__instance.currentBlock, damageAmount);
            WljMod.workerManager.iterOutsideDiscardPile(c -> {
                if (c instanceof Leidongxuan) {
                    ((Leidongxuan) c).onBlockDecrement(blockedDamage);
                }
            });
        }
    }

    private static class BeforeDecrementLocator extends SpireInsertLocator {
        private BeforeDecrementLocator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
