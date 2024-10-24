package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
    clz = AbstractMonster.class,
    method = "damage"
)
public class OnAttackPatch {

    @SpireInsertPatch(
        locator = Locator.class
    )
    public static void Insert(AbstractMonster __instance, DamageInfo info) {
         if (WljMod.avatarManager.isHasAvatar())
            WljMod.avatarManager.getCurrentAvatar().onAttack(__instance, info);
    }


    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPower.class, "onAttacked");
            return Inject.insertBefore(LineFinder.findInOrder(ctMethodToPatch, finalMatcher), 1);
        }
    }
}
