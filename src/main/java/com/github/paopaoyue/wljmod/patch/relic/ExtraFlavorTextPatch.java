package com.github.paopaoyue.wljmod.patch.relic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.google.gson.annotations.SerializedName;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ExtraFlavorTextPatch {
    @SpirePatch2(
            clz = AbstractRelic.class,
            method = SpirePatch.CLASS
    )
    public static class RelicExtraFlavorFields {
        public static SpireField<String> text = new SpireField<>(() -> null);
        public static SpireField<Color> boxColor = new SpireField<>(Color.WHITE::cpy);
        public static SpireField<Color> textColor = new SpireField<>(Color.BLACK::cpy);
        public static SpireField<FlavorText.boxType> flavorBoxType = new SpireField<>(() -> FlavorText.boxType.WHITE);
        public static SpireField<Texture> boxTop = new SpireField<>(() -> null);
        public static SpireField<Texture> boxMid = new SpireField<>(() -> null);
        public static SpireField<Texture> boxBot = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz = RelicStrings.class,
            method = SpirePatch.CLASS
    )
    public static class RelicStringsExtraFlavorField {
        @SerializedName("EXTRA_FLAVOR")
        public static SpireField<String> extraFlavor = new SpireField<>(() -> null);
    }

    @SpirePatch2(
            clz = AbstractRelic.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, AbstractRelic.RelicTier.class, AbstractRelic.LandingSound.class}
    )
    public static class FlavorIntoRelicStrings {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void postfix(AbstractRelic __instance) {
            RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(__instance.relicId);
            if (relicStrings == null || RelicStringsExtraFlavorField.extraFlavor.get(relicStrings) == null ||
                    RelicStringsExtraFlavorField.extraFlavor.get(relicStrings).isEmpty())
                return;

            RelicExtraFlavorFields.text.set(__instance, RelicStringsExtraFlavorField.extraFlavor.get(relicStrings));
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "initializeTips");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch2(
            clz = AbstractRelic.class,
            method = "renderTip"
    )
    public static class PassRelicTooltip {

        private static final String HEADER_STRING = "@STSLIB:FLAVOR@";

        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractRelic __instance) {
            if (__instance.tips.stream().anyMatch(tip -> tip.header.equals(HEADER_STRING)))
                return SpireReturn.Continue();

            if (RelicExtraFlavorFields.text.get(__instance) == null || RelicExtraFlavorFields.text.get(__instance) == null ||
                    RelicExtraFlavorFields.text.get(__instance).isEmpty())
                return SpireReturn.Continue();

            PowerTip tip = new PowerTip(HEADER_STRING, RelicExtraFlavorFields.text.get(__instance));
            FlavorText.PowerTipFlavorFields.textColor.set(tip, RelicExtraFlavorFields.textColor.get(__instance));
            FlavorText.PowerTipFlavorFields.boxColor.set(tip, RelicExtraFlavorFields.boxColor.get(__instance));
            FlavorText.PowerTipFlavorFields.flavorBoxType.set(tip, RelicExtraFlavorFields.flavorBoxType.get(__instance));
            FlavorText.PowerTipFlavorFields.boxBot.set(tip, RelicExtraFlavorFields.boxBot.get(__instance));
            FlavorText.PowerTipFlavorFields.boxMid.set(tip, RelicExtraFlavorFields.boxMid.get(__instance));
            FlavorText.PowerTipFlavorFields.boxTop.set(tip, RelicExtraFlavorFields.boxTop.get(__instance));
            __instance.tips.add(tip);

            return SpireReturn.Continue();
        }
    }

}
