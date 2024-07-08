package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "onVictory"
)
public class OnVictoryPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractPlayer __instance) {
        WljMod.avatarManager.onBattleEnd();
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDying");
            return Inject.insertAfter(LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher), 1);
        }
    }

}
