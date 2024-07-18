package com.github.paopaoyue.wljmod.patch.gold;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "loseGold"
)
public class LoseGoldPatch {

    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance, int goldAmount) {
        if (WljMod.tempGold > 0) {
            int goldToBorrow = Math.min(goldAmount, WljMod.tempGold);
            WljMod.tempGold -= goldToBorrow;
            __instance.gold += goldToBorrow;
        }
    }

}