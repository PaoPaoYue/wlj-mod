package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.card.Sts;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = Soul.class,
        method = "obtain"
)
public class StsPatch {

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"card"}
    )
    public static void Insert(AbstractCard card) {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof Sts) {
                ((Sts) c).triggerWhenObtainCards(card);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof Sts) {
                ((Sts) c).triggerWhenObtainCards(card);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof Sts) {
                ((Sts) c).triggerWhenObtainCards(card);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof Sts) {
                ((Sts) c).triggerWhenObtainCards(card);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(Soul.class, "card");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
