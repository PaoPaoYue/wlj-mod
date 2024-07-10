package com.github.paopaoyue.wljmod.patch.avatar;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.GiantSword;
import com.github.paopaoyue.wljmod.component.Xiangdangdang;
import com.github.paopaoyue.wljmod.patch.CardTagEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "onCardDrawOrDiscard"
)
public class OnCardDrawOrDiscardPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractPlayer __instance) {
        boolean isXiangdangdang = WljMod.avatarManager.getCurrentAvatar() instanceof Xiangdangdang;
        for (AbstractCard c : __instance.hand.group) {
            if (isXiangdangdang &&
                    c.hasTag(CardTagEnum.TAIWU) &&
                    c != __instance.cardInUse &&
                    c.costForTurn > c.cost -1) {
                c.setCostForTurn(c.cost - 1);
            }
            if (c instanceof GiantSword) {
                c.triggerWhenDrawn();
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CardGroup.class, "applyPowers");
            return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }

}