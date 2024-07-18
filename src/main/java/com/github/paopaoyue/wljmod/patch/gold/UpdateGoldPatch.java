package com.github.paopaoyue.wljmod.patch.gold;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.github.paopaoyue.wljmod.WljMod;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

@SpirePatch(
        clz = TopPanel.class,
        method = "updateGold"
)
public class UpdateGoldPatch {

    @SpirePostfixPatch
    public static void Postfix() {
        if (WljMod.tempGold < WljMod.displayTempGold) {
            if (WljMod.displayTempGold - WljMod.tempGold > 99) {
                WljMod.displayTempGold -= 10;
            } else if (WljMod.displayTempGold - WljMod.tempGold > 9) {
                WljMod.displayTempGold -= 3;
            } else {
                --WljMod.displayTempGold;
            }
        } else if (WljMod.tempGold > WljMod.displayTempGold) {
            if (WljMod.tempGold - WljMod.displayTempGold > 99) {
                WljMod.displayTempGold += 10;
            } else if (WljMod.tempGold - WljMod.displayTempGold > 9) {
                WljMod.displayTempGold += 3;
            } else {
                ++WljMod.displayTempGold;
            }
        }
    }
}
