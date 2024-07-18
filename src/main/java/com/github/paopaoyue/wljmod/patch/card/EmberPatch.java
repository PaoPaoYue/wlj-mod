package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.github.paopaoyue.wljmod.card.Ember;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyStartOfCombatPreDrawLogic"
)
public class EmberPatch {

    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance) {
        List<AbstractCard> tmpGroup =
                Stream.concat(
                                __instance.drawPile.group.stream().filter(card -> card instanceof Ember && !card.upgraded),
                                __instance.drawPile.group.stream().filter(card -> !(card instanceof Ember && !card.upgraded)))
                        .collect(Collectors.toList());
        __instance.drawPile.group = (ArrayList<AbstractCard>) tmpGroup;
    }
}
