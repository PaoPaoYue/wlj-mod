package com.github.paopaoyue.wljmod.patch.gold;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.github.paopaoyue.wljmod.utility.Reflect;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;

@SpirePatch(
        clz = GainGoldTextEffect.class,
        method = "render"
)
public class GoldTextEffectPatch {

    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(GainGoldTextEffect __instance, SpriteBatch sb) {
        if (!__instance.isDone) {
            Integer gold = Reflect.getPrivate(GainGoldTextEffect.class, __instance, "gold", Integer.class);
            Float x = Reflect.getPrivate(GainGoldTextEffect.class, __instance, "x", Float.class);
            Float y = Reflect.getPrivate(GainGoldTextEffect.class, __instance, "y", Float.class);
            Color color = Reflect.getPrivate(AbstractGameEffect.class, __instance, "color", Color.class);
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, (gold < 0 ? "- " : "+ ") + Integer.toString(Math.abs(gold)) + GainGoldTextEffect.TEXT[0], x, y, color);
        }
        return SpireReturn.Return();
    }
}
