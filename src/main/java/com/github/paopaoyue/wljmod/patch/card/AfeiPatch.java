package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.Afei;
import com.github.paopaoyue.wljmod.utility.Inject;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SpirePatch(
        clz = CardLibrary.class,
        method = "getCopy",
        paramtypez = {String.class, int.class, int.class}
)
public class AfeiPatch {

    private static final Logger logger = LogManager.getLogger(CardLibrary.class);

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"retVal", "misc"}
    )
    public static void Insert(AbstractCard retVal, int misc) {
        if (retVal.cardID.equals(Afei.ID)) {
            retVal.damage = misc;
            retVal.baseDamage = misc;
            retVal.initializeDescription();
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "misc");
            return Inject.insertAfter(LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher), 1);
        }
    }
}
