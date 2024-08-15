package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
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
        Reflect.invokePrivateMethod(__instance, "calculateScrollBounds");
        CardGroup exhaustPileCopy = Reflect.getPrivate(ExhaustPileViewScreen.class, __instance, "exhaustPileCopy", CardGroup.class);
        if (exhaustPileCopy == null) {
            logger.error("ExhaustPileViewScreenPatch: exhaustPileCopy is null");
            return SpireReturn.Return(null);
        }
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
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
