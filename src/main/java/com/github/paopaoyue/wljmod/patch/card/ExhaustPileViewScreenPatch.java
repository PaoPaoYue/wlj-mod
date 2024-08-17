package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.ExhaustPileViewScreen;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpirePatch(
        clz = ExhaustPileViewScreen.class,
        method = "open"
)
public class ExhaustPileViewScreenPatch {

    private static Logger logger = LogManager.getLogger(ExhaustPileViewScreenPatch.class);

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(ExhaustPileViewScreen __instance) {
        CardGroup exhaustPileCopy = Reflect.getPrivate(ExhaustPileViewScreen.class, __instance, "exhaustPileCopy", CardGroup.class);
        if (exhaustPileCopy == null) {
            logger.error("ExhaustPileViewScreenPatch: exhaustPileCopy is null");
            return SpireReturn.Return(null);
        }
        for (final AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            final AbstractCard toAdd = c.makeStatEquivalentCopy();
            toAdd.setAngle(0.0f, true);
            toAdd.targetDrawScale = 0.75f;
            toAdd.drawScale = 0.75f;
            toAdd.lighten(true);
            exhaustPileCopy.addToTop(toAdd);
        }
        Reflect.invokePrivateMethod(__instance, "calculateScrollBounds");
        if (exhaustPileCopy.group.size() <= 5) {
            Reflect.setStaticPrivate(ExhaustPileViewScreen.class, "drawStartY", Settings.HEIGHT * 0.5f);
        } else {
            Reflect.setStaticPrivate(ExhaustPileViewScreen.class, "drawStartY", Settings.HEIGHT * 0.66f);
        }
        Reflect.invokePrivateMethod(__instance, "calculateScrollBounds");
        return SpireReturn.Return(null);
    }

    private static class Locator extends SpireInsertLocator {

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "clear");
            return Inject.insertAfter(LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher), 1);
        }
    }
}
