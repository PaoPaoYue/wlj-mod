package com.github.paopaoyue.wljmod.patch.worker;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.github.paopaoyue.wljmod.WljMod;
import com.github.paopaoyue.wljmod.card.Rabble;
import com.megacrit.cardcrawl.actions.GameActionManager;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndOfTurnActions"
)
public class GameActionManagerPatch {

    @SpirePostfixPatch
    public static void Postfix(GameActionManager __instance) {
        WljMod.workerManager.iterOutsideDiscardPile(c -> {
            if (c instanceof Rabble) {
                ((Rabble) c).triggerOnEndOfPlayerTurnOutsideDiscardPile();
            }
        });
    }
}
