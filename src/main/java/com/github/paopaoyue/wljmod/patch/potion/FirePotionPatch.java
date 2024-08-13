package com.github.paopaoyue.wljmod.patch.potion;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.FirePotion;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = FirePotion.class,
        method = "initializeData"
)
public class FirePotionPatch {

    public static final String POTION_ID = "Wlj:Fire";
    private static final PotionStrings potionStrings;

    static {
        potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(FirePotion __instance) {
        FlavorText.PotionFlavorFields.flavor.set(__instance, FlavorText.PotionStringsFlavorField.flavor.get(potionStrings));
        FlavorText.PotionFlavorFields.textColor.set(__instance, Color.MAROON);
        FlavorText.PotionFlavorFields.boxColor.set(__instance, Color.GOLD);
        FlavorText.PotionFlavorFields.flavorBoxType.set(__instance, FlavorText.boxType.WHITE);
        __instance.tips.clear();
        __instance.tips.add(new PowerTip(potionStrings.NAME, __instance.description));
        return SpireReturn.Return(null);
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "clear");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
