package com.github.paopaoyue.wljmod.patch.relic;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.potion.Ball;
import com.github.paopaoyue.wljmod.relic.Useless;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward"
)
public class RewardDropPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(CombatRewardScreen __instance) {
        if (AbstractDungeon.player.hasRelic(Useless.ID)) {
            Useless relic = (Useless) AbstractDungeon.player.getRelic(Useless.ID);
            relic.onRewardDrop(__instance.rewards);
        }
        for (AbstractPotion potion : AbstractDungeon.player.potions) {
            if (potion instanceof Ball) {
                Ball ball = (Ball) potion;
                ball.onRewardDrop(__instance.rewards);
                AbstractDungeon.topPanel.destroyPotion(potion.slot);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ProceedButton.class, "show");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}

