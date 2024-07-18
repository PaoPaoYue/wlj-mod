package com.github.paopaoyue.wljmod.patch.gold;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "onVictory"
)
public class OnVictoryPatch {

    @SpirePostfixPatch
    public static void Postfix(AbstractPlayer __instance) {
        WljMod.displayTempGold = 0;
        WljMod.tempGold = 0;
    }

}