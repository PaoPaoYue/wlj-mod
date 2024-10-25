package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.github.paopaoyue.wljmod.card.Koro;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

import java.util.concurrent.atomic.AtomicInteger;

@SpirePatch(
    clz = AbstractPlayer.class,
    method = "damage"
)
public class onLoseHPPatch {

    @SpireInsertPatch(
        locator = Locator.class,
        localvars = {"damageAmount"}
    )
    public static void Insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
        AbstractDungeon.player.drawPile.group.forEach(c -> {
            if (c instanceof Koro) {
                damageAmount[0] = ((Koro) c).onLoseHP(damageAmount[0]);
            }
        });
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "onLoseHpLast");
            return Inject.insertAfter(LineFinder.findInOrder(ctMethodToPatch, finalMatcher), 3);
        }
    }
}
