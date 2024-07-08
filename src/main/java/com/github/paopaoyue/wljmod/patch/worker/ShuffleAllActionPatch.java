package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.github.paopaoyue.wljmod.card.AbstractWorkerCard;
import com.megacrit.cardcrawl.actions.defect.ShuffleAllAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpirePatch(
        clz = ShuffleAllAction.class,
        method = "update"
)
public class ShuffleAllActionPatch {
    private static final List<AbstractCard> tempCardList = new ArrayList<>();

    @SpirePrefixPatch
    public static void Prefix(ShuffleAllAction __instance) {
        Iterator<AbstractCard> iterator = AbstractDungeon.player.discardPile.group.iterator();
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card instanceof AbstractWorkerCard) {
                tempCardList.add(card);
                iterator.remove();
            }
        }
    }

    @SpirePostfixPatch
    public static void Postfix(ShuffleAllAction __instance) {
        AbstractDungeon.player.discardPile.group.addAll(tempCardList);
        tempCardList.clear();
    }
}
