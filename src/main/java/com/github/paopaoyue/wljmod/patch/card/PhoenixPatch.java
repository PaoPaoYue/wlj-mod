package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.github.paopaoyue.wljmod.card.Phoenix;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.List;
import java.util.stream.Collectors;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyStartOfCombatPreDrawLogic"
)
public class PhoenixPatch {

    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance) {
        List<AbstractCard> tmpGroup = __instance.drawPile.group.stream().filter(card -> card instanceof Phoenix).collect(Collectors.toList());
        __instance.discardPile.group.addAll(tmpGroup);
        __instance.drawPile.group.removeAll(tmpGroup);
    }
}
