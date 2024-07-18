package com.github.paopaoyue.wljmod.patch.relic;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.select.BossRelicSelectScreen;

@SpirePatch(
        clz = BossRelicSelectScreen.class,
        method = "relicObtainLogic"
)
public class BossRelicObtain {

    @SpirePostfixPatch
    public static void Postfix(BossRelicSelectScreen __instance, AbstractRelic r) {
        if (r.relicId.equals("Wlj:Tax")) {
            r.instantObtain(AbstractDungeon.player, 0, true);
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25f;
        }
    }
}