package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = DrawCardAction.class,
        method = "update"
)
public class DrawCardActionPatch {

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"discardSize"}
    )
    public static void Insert(DrawCardAction __instance, @ByRef int[] discardSize) {
        discardSize[0] = AbstractDungeon.player.discardPile.group.stream().mapToInt(
                c -> c instanceof AbstractWorkerCard ? 0 : 1
        ).sum();
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(SoulGroup.class, "isActive");
            return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }

}
