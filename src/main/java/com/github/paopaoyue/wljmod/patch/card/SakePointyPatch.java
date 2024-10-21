package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.github.paopaoyue.wljmod.card.Sake;
import com.megacrit.cardcrawl.monsters.city.BanditPointy;

@SpirePatch(
        clz = BanditPointy.class,
        method = "takeTurn"
)
public class SakePointyPatch {

    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(BanditPointy __instance) {
        if (__instance.nextMove == Sake.MOCKED_MOVE) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
