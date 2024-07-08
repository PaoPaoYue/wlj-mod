package com.github.paopaoyue.wljmod.patch.card;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.github.paopaoyue.wljmod.action.LapseAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyPreCombatLogic"
)
public class LapsePatch {

    @SpirePrefixPatch
    public static void prefix(AbstractPlayer __instance) {
        LapseAction.reset();
    }

}
